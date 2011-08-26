#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight
  module Optionable

    def apply_options(options)    
      options = Util::Hashes.for_ruby(options)
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
    end

    alias :applyOptions :apply_options

    private ###############################################

    def is_event_setter(symbol)
      string_value = symbol.to_s
      return string_value[0..2] == "on_" && self.respond_to?(symbol)
    end

    def add_event_action(symbol, value)
      self.send(symbol) { eval(value) };
    end
    
  end
end