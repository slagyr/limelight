require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/styles_builder'

describe Limelight::StylesBuilder do

  before(:each) do
  end
  
  it "should build a hash" do
    result = Limelight.build_styles
    
    result.class.should == Hash
    result.size.should == 0
  end
  
  it "should build one style" do
    result = Limelight.build_styles do
      root
    end
    
    result.size.should == 1
    result["root"].class.should == Java::limelight.ui.FlatStyle
  end
  
  it "should build one style with styling" do
    result = Limelight.build_styles do
      root do
        width 100
        top_border_color "blue"
        transparency "50%"
      end
    end
    
    result.size.should == 1
    style = result["root"]
    style.width.should == "100"
    style.top_border_color.should == "blue"
    style.transparency.should == "50%"
  end
  
  it "should raise an error on invalid styles" do
    lambda do
      Limelight.build_styles do
        root do
          blah 100
        end
      end
    end.should raise_error(Limelight::StyleBuilderException)
  end
  
  it "should build multiple styles" do
    result = Limelight.build_styles do
      one { width 100 }
      two { width 50 }
    end
    
    result.size.should == 2
    result["one"].width.should == "100"
    result["two"].width.should == "50"
  end

end

