$:<< File.expand_path(File.dirname(__FILE__) + "/../../examples")

require 'spec'

def make_mock(name, stubs = {})
  the_mock = mock(name)
  the_mock.stubs!(stubs)
  return the_mock
end

class Object
  def stubs!(stubs = {})
    stubs.each_pair { |key, value| self.stub!(key).and_return(value) }
  end
end
