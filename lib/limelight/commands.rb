#- Copyright 2008 8th Light, Inc.
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
        packer = Limelight::IO::Packer.new
        packer.pack(args.shift)
      end
    end

    COMMANDS = {
        "open" => OpenProduction,
        "pack" => PackProduction
    }

    class << self
      def run(args)
        command_name = args.shift
        command = COMMANDS[command_name]
        if command
          command.new.run(args)
        else
          usage
        end
      end

      def usage
        puts "Usage: limelight <command> [options] [params]"
        puts "commands:"
        COMMANDS.keys.sort.each do |key|
          command = COMMANDS[key]
          puts "\t#{key}\t\t#{command.description}"
        end
      end
    end


  end


end
