require 'limelight_java'
require 'llm_parser'

class Book

  def load(llm_file)
    parser = LlmParser.new
    dir = File.expand_path(File.dirname(llm_file))
    Dir.chdir(dir)
    $: << dir
    page = parser.parse(IO.read(File.basename(llm_file)))
    self.open(page)
  end

end
