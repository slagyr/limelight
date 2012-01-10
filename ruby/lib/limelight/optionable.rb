#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight
  module Optionable

    def apply_options(opts)
      options = Util::Hashes.for_ruby(opts)
      options.keys.each do |key|
        setter_sym = "#{key.to_s}=".to_sym
        if self.respond_to?(setter_sym)
          self.send(setter_sym, options.delete(key))
        elsif self.style.respond_to?(setter_sym)
          self.style.send(setter_sym, options.delete(key).to_s)
        elsif is_event_setter(key)
          add_event_action(key, options.delete(key))
        end
      end
      opts
    end

    alias :applyOptions :apply_options

    private ###############################################

    def is_event_setter(symbol)
      string_value = symbol.to_s
      return string_value[0..2] == "on_" && self.respond_to?(symbol)
    end

    def add_event_action(symbol, value)
      if value.is_a? String
        self.send(symbol) { eval(value) }
      elsif value.is_a? Proc
        self.send(symbol, & value)
      else
        raise "don't how to create event action from: #{value}"
      end
    end

  end
end