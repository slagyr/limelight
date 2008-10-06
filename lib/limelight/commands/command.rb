module Limelight
  module Commands

    LISTING = {}

    class << self

      def [](name)
        load_listing if !@listing_loaded
        return LISTING[name]
      end

      def load_listing
        Dir.entries(File.dirname(__FILE__)).each do |file|
          if file != "." && file != ".."
            require "limelight/commands/#{file.gsub('.rb', '')}"
          end
        end
        @listing_loaded = true
      end

    end

    class Command

      class << self

        def install_as(name)
          raise "Command already installed: #{name}." if LISTING[name]
          LISTING[name] = self
        end

      end

    end

  end
end