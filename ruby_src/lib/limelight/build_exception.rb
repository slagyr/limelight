module Limelight
  class BuildException < Exception

    attr_reader :filename, :line_number

    def initialize(filename, file_contents, e)
      @filename = filename
      @file_contents = file_contents
      @original_exception = e
      @line_number = find_line_number
      super(build_error_message)
    end

    def find_line_number
      lines = [@original_exception.message].concat @original_exception.backtrace
      line_number = nil
      lines.each do |line|
        match = line.match(/\(eval\):(\d+):/)
        if(match)
          line_number = match[1].to_i
          break
        end
      end
      return line_number
    end

    def build_error_message
      lines = @file_contents.split("\n")
      start_line = @line_number - 4 < 0 ? 0 : @line_number - 4
      end_line = @line_number + 2 >= lines.size ? lines.size - 1: @line_number + 2
      message = "#{@filename}:#{@line_number}: #{@original_exception.message}"
      (start_line..end_line).each do |i|
        message << "\n\t#{i+1}: #{lines[i]}"
      end  
      message << "\n"
      return message
    end
  end

end