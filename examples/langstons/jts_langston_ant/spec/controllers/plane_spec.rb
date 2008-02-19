require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'langston_ant/controllers/plane'

class MockPlane

  def initialize
    extend Plane
  end
  
end

describe Plane do

  before(:each) do
    @plane = MockPlane.new
  end
  
end
