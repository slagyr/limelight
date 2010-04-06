#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'optparse'

module Limelight
  module Commands

    LISTING = {}

    class << self

      def [](name) #:nodoc:
        name = "help" if name.to_s.empty?
        if LISTING[name].nil?
          load_command(name)
        end

        return LISTING[name]
      end

      def load_command(name)
        begin
          name = name.gsub('-', '') 
          require "limelight/commands/#{name}_command"
        rescue LoadError => e
        end
      end

      def output=(value) #:nodoc:
        @output = value
      end

      def output  #:nodoc:
        return @output || $stdout
      end

    end

    # The base class for all limelight commands.  The following is a list of them all.
    #
    #  Usage: limelight <command> [options] [params]
    #    commands:
    #    create		Creates the directories and files for a production and/or scene.
    #    freeze		freeze a gem into a production.
    #    open		Open a limelight production.
    #    pack		Pack a limelight production into a .llp file.
    #
    class Command

      class << self

        attr_accessor :alias

        # Sets the alias for the derivative command and installs it in the command listing.  All derivative much
        # call this method exactly once.
        #
        def install_as(name)
          raise "Command already installed: #{name}." if LISTING[name]
          self.alias = name
          LISTING[name] = self
        end

        # Abstract class level methof that returns a string description of the command.  Derivatives should
        # override this class level method.
        #
        def description
          return "Default Command description"
        end

      end

      # Flag.  The backtrace on parse errors will be printed if true.
      #
      attr_accessor :print_backtrace

      # Runs the command.  This is TemplateMethod.  It will first parse the arguments, then require files, and finally
      # process the command.  Derivative should not override this method.
      #
      def run(args)
        parse(args)
        do_requires
        begin
          process if !@has_parse_error
        rescue SystemExit
          #okay
        end
      end

      # Parse the arguments.  All options will be parsed first, then parse_remainder will be called on
      # remaining args.  Derivative should not override this method.
      #
      def parse(args)         
        begin
          remainder = options_spec.parse(args)
          parse_remainder(remainder)
        rescue SystemExit => e
          raise e
        rescue Exception => e
          @has_parse_error = true
          parse_error e
        end
      end

      protected ###########################################

      def puts(*args) #:nodoc:
        Commands.output.puts(*args)
      end

      # Prints an exception that occurs while parsing the arguments.  The usages summary will follow.
      #
      def parse_error(exception = nil)
        if exception
          puts ""
          puts "!!! #{exception}"
          puts exception.backtrace if @print_backtrace
        end
        usage
        exit -1
      end

      # Prints the usage summary for a command.
      #
      def usage
        puts ""
        puts "Usage: limelight #{self.class.alias} #{parameter_description}"
        puts "    #{self.class.description}"
        puts "    options:"
        puts options_spec.summarize
      end

      # Retreives the OptionParser instance for this command.  It will create it if it doesn't exist.
      #
      def options_spec
        if @options_spec.nil?
          @options_spec = OptionParser.new
          @options_spec.on("-h", "--help", "Prints this usage summary.") { usage; exit 0 }
          build_options(@options_spec)
        end
        return @options_spec
      end

      # Abstract method to require any files needed for processing the command.  Derivative should override this
      # method if they need to require any files.
      #
      def do_requires
        #override me
      end

      # Abstract method to define the command line options in the OptionParser.  Derivative should override
      # this method if they offer command line options 
      #
      def build_options(spec)
        #override me
      end

      # Abstract method to parse command line paramters.  Parameter are those command line arguments listed after
      # the options.  Derivatives should override this method they take command line paramters.
      #
      def parse_remainder(args)
        #override me
      end

    end

  end
end