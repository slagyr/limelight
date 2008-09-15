#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'optparse'

module Limelight

  DEFAULT_PRODUCTION = File.expand_path(File.dirname(__FILE__) + "/../../productions/startup")

  module Commands

    class OpenProduction
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

    class PackProduction
      def self.description
        return "Pack the designated limelight production into a .llp file."
      end

      def run(args)
        packer = Limelight::Util::Packer.new
        packer.pack(args.shift)
      end
    end

    COMMANDS = {
        "open" => OpenProduction,
        "pack" => PackProduction
    }

  end


end
