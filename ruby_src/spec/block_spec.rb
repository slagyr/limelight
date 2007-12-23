require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/block'

describe Limelight::Block do

  before(:each) do
    @block = Limelight::Block.new
  end
  
  it "should extend added controllers and invoke the extended hook" do
    module TestController
      class << self
        attr_reader :extended_block
        def extended(block)
          @extended_block = (block)
        end
      end
      
      def test_method
      end
    end
    
    @block.add_controller(TestController)
    
    TestController.extended_block.should == @block
    @block.respond_to?(:test_method).should == true
  end

end

