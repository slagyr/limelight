#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../lib/init")

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
