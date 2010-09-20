require 'limelight/util/map_hash'
require 'limelight/util/string_hash'

module Limelight
  module Util

    module Hashes

      def self.select(hash)
        if hash.is_a?(StringHash) || hash.is_a?(MapHash)
          return hash
        elsif hash.is_a?(Java::java.util.Map)
          return MapHash.new(hash)
        else
          return StringHash.new.merge!(hash)
        end

      end

    end

  end
end