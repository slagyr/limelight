module Limelight
  module Templates

    class TemplaterLogger

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

    end
    
  end
end