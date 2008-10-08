require 'limelight/commands/command'

module Limelight
  module Commands

    # See the following usage summary.
    #
    #    Usage: limelight open <production_path>
    #        Open a limelight production.
    #        options:
    #        -h, --help                       Prints this usage summary.
    #
    class OpenCommand < Command

      install_as "open"

      DEFAULT_PRODUCTION = File.expand_path($LIMELIGHT_HOME + "/productions/startup")

      def initialize
        self.print_backtrace = true
      end

      def self.description
        return "Open a limelight production."
      end

      protected ###########################################

      def parameter_description
        return "<production_path>"
      end

      def do_requires
        Main.initialize_context
        require 'limelight/producer'
      end

      def parse_remainder(args)
        @production_path = args.shift || DEFAULT_PRODUCTION
      end

      def process
        Limelight::Producer.open(@production_path)
      end

    end
    
  end
end