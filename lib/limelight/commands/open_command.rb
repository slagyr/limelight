require 'limelight/commands/command'

module Limelight
  module Commands

    # Opens a Production
    #
    #   jruby -S limelight open <production_name>
    #
    class OpenCommand < Command

      install_as "open"

      DEFAULT_PRODUCTION = File.expand_path($LIMELIGHT_HOME + "/productions/startup")

      def self.description
        return "Open a limelight production or scene."
      end

      def run(args)
        Main.initialize_context
        require 'limelight/producer'
        production = args.length > 0 ? args[0] : DEFAULT_PRODUCTION
        Limelight::Producer.open(production)
      end
    end
    
  end
end