module Limelight
  module Templates

    # Templaters uses this class to log activity.  
    #
    class TemplaterLogger

      # An accessor to the output IO. Defaults to STDOUT
      #
      attr_accessor :output

      def initialize
        @output = STDOUT
      end

      def creating_directory(name)
        @output.puts "\tcreating directory:  #{name}"
      end

      def creating_file(name)
        @output.puts "\tcreating file:       #{name}"
      end

      def file_already_exists(name)
        @output.puts "\tfile already exists: #{name}"
      end

      def log(message, name)
        spaces = 21 - message.length - 1
        @output.puts "\t#{message}:#{' '*spaces}#{name}"
      end

    end
    
  end
end