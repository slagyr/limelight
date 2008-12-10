#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # Prints limelight command help
    #
    class HelpCommand < Command

      install_as "help"

      def self.description #:nodoc:
        return "Prints limelight command help"
      end

      protected ###########################################

      def process #:nodoc:
        @options = []
        @commands = []
        sort_commands
        puts ""
        puts "Usage: limelight #{@options.join(' ')} <command> [options] [params]"
        puts "  commands:"
        @commands.each do |command|
          puts command
        end
      end

      private #############################################

      def sort_commands
        Commands::LISTING.each_pair do |key, command|
          if key[0...1] == "-"
            @options << "[#{key}]"
          else
            @commands << "\t#{key}\t\t#{command.description}"
          end
        end
      end

    end

  end


end
