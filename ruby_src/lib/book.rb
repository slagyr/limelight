require 'limelight_java'
require 'llm_parser'

class Book

  def load(llm)
    parser = LlmParser.new
    page = parser.parse(IO.read(llm))
    self.open(page)
  end

end
