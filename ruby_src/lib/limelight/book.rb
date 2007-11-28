require 'limelight/limelight_java'
require 'limelight/llm_parser'

module Limelight
  
  class Book < JBook
  
    def load2(llm_file)
      load(llm_file)
    end
  
    def reload
      load(@current_file)
    end

  end

  class JBook
    def load(llm_file)
      @current_file = llm_file
      parser = LlmParser.new
      dir = File.expand_path(File.dirname(llm_file))
      Dir.chdir(dir)
      $: << dir
      page = parser.parse(IO.read(File.basename(llm_file)))
      self.open(page)
    end
  end
    
end
