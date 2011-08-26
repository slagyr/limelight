#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight
  module Util

    class MapHash

      include Enumerable

      attr_accessor :default
      attr_reader :map

      def initialize(map)
        @map = map
        @default = nil
      end

      def []=(key, value)
        @map.put(desymbolize(key), value)
      end
      alias_method :store, :[]=

      def [](key)
        key = desymbolize(key)
        return @map.contains_key(key) ? @map.get(key) : @default
      end

      def ==(other)
        return false if !other.respond_to?(:to_hash)
        other = other.to_hash
        return false if other == nil
        return false if other.size != @map.size
        other.each { |key, value| return false if self[key] != value }
        return true
      end
      alias_method :eql?, :==

      def clear
        @map.clear
        return self
      end

      def default_proc
        return nil
      end

      def delete(key)
        return @map.remove(desymbolize(key))
      end

      def delete_if
        self.keys.each do |key|
          value = @map.get(key)
          @map.remove(key) if yield(key, value)
        end
        return self
      end
      alias_method :reject!, :delete_if

      def dup
        return MapHash.new(@map.clone)        
      end

      def each(&block)
        @map.each &block
        return self
      end
      alias_method :each_pair, :each

      def each_key(&block)
        @map.key_set.each &block
        return self
      end

      def each_value(&block)
        @map.values.each &block
        return self
      end

      def empty?
        return @map.isEmpty()
      end

      def fetch(key, option=nil)
        desymbolized_key = desymbolize(key)
        return @map.get(desymbolized_key) if @map.contains_key(desymbolized_key)
        return yield(key) if block_given?
        return option if option != nil
        raise IndexError.new("key not found")
      end

      def has_key?(key)
        return @map.contains_key(desymbolize(key))
      end
      alias_method :key?, :has_key?
      alias_method :member?, :has_key?
      alias_method :include?, :has_key?

      def has_value?(value)
        return @map.contains_value(value)
      end
      alias_method :value?, :has_value?

      def index(search_value)
        @map.each { |key, value| return key if value == search_value }
        return nil
      end

      def invert
        result = dup.clear
        @map.each { |key, value| result[value] = key }
        return result
      end

      def keys
        result = []
        @map.key_set.each { |key| result << key}
        return result
      end

      def length
        return @map.size
      end
      alias_method :size, :length

      def merge(other, &block)
        return dup.merge!(other, &block)
      end

      def merge!(other)
        other.each do |key, value|
          key = desymbolize(key)
          value = yield(key, @map.get(key), value) if(@map.contains_key(key) && block_given?)
          @map.put(key, value)
        end
        return self
      end
      alias_method :update, :merge!

      def rehash
        return self
      end

      def reject(&block)
        return dup.reject!(&block)
      end

      def replace(other)
        clear
        return merge!(other)
      end

      def select
        results = []
        @map.each { |key, value| results << [key, value] if yield(key, value) }
        return results
      end

      def shift
        return @default if @map.isEmpty
        key = keys[0]
        return [key, @map.remove(key)]
      end

      def to_hash
        hash = {}
        @map.each { |key, value| hash[key] = value }
        return hash
      end

      def values
        result = []
        @map.values.each { |key| result << key}
        return result
      end

      def values_at(*keys)
        return keys.map { |key| self[key] }
      end

      private

      def desymbolize(key)
        return key.is_a?(Symbol) ? key.to_s : key
      end

    end

  end
end