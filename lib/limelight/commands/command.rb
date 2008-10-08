require 'optparse'

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

        attr_accessor :alias

        def install_as(name)
          raise "Command already installed: #{name}." if LISTING[name]
          self.alias = name
          LISTING[name] = self
        end

      end

      attr_accessor :print_backtrace

      def run(args)
        parse(args)
        do_requires
        begin
          process
        rescue Exception => e
          usage(e)
        end
      end

      def parse(args)
        begin
          remainder = options_spec.parse(args)
          parse_remainder(remainder)
        rescue Object => e
          usage e
        end
      end

      protected ###########################################

      def usage(exception = nil)
        puts exception if exception    
        puts exception.backtrace if @print_backtrace
        puts "Usage: limelight #{self.class.alias} #{parameter_description}"
        puts options_spec.summarize
        exit -1
      end

      def options_spec
        if @options_spec.nil?
          @options_spec = OptionParser.new
          build_options(@options_spec)
        end
        return @options_spec
      end

      def do_requires
        #override me
      end

      def build_options(spec)
        #override me
      end

      def parse_remainder(args)
        #override me
      end

    end

  end
end