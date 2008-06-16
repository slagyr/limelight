#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
    result["root"].class.should == Limelight::Styles::RichStyle
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
  
  it "should allow hover styles" do
    styles = Limelight.build_styles do
      root do
        width 100
        hover do
          width 50
        end
      end
    end
    
    styles.size.should == 2
    styles["root"].width.should == "100"
    styles["root.hover"].width.should == "50"
  end

  it "should allow styles to extend other styles" do
    styles = Limelight.build_styles do
      one { width 100 }
      two {
        extends "one"
        height 200
      }
    end

    styles.size.should == 2
    one = styles["one"]
    two = styles["two"]
    two.has_extension(one).should == true
    two.height.should == "200"
    two.width.should == "100"
  end

  it "should allow multiple extensions" do
    styles = Limelight.build_styles do
      one { width 100 }
      two { x 100 }
      three {
        extends :one, :two
        height 200
      }
    end

    styles.size.should == 3
    one = styles["one"]
    two = styles["two"]
    three = styles["three"]
    three.has_extension(one).should == true
    three.has_extension(two).should == true
    three.height.should == "200"
    three.width.should == "100"
    three.x.should == "100"
  end

  it "should raise an exception when attempting to extend a missing style" do
    lambda { Limelight.build_styles { one { extends :blah } } }.should raise_error(Limelight::StyleBuilderException, "Can't extend missing style: 'blah'")
    end

end

