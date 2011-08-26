#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight
  module Util

    class StringHash < Hash

      alias_method :put, :[]=
      alias_method :get, :[]

      def []=(key, value)
        put(as_string(key), value)
      end

      def [](key)
        return get(as_string(key))
      end

      def update(other)
        other.each_pair do |key, value|
          my_value = self[key]
          if value && block_given?
            value = yield(key, my_value, value)
          end
          self[key] = value          
        end
        return self
      end
      alias_method :merge!, :update

      def merge(other, &block)
        return dup.merge!(other, &block)  
      end

      def key?(key)
        return super(as_string(key))
      end

      def fetch(key, *args)
        return super(as_string(key), *args)
      end

      def delete(key)
        return super(as_string(key))
      end

      private

      def as_string(key)
        case key
          when nil
            return nil
          when String
            return key
          else
            return key.to_s
        end
      end

    end

  end
end
