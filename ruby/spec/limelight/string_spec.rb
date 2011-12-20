#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/string'

describe String do

  it "should convert into camel case" do
    "class_name".camelized.should == "ClassName"
    "once_upon_a_time".camelized.should == "OnceUponATime"
    "with spaces".camelized.should == "WithSpaces"
  end

  it "should convert into camel case" do
    "class_name".camelized(:lower).should == "className"
    "once_upon_a_time".camelized(:lower).should == "onceUponATime"
    "with spaces".camelized(:lower).should == "withSpaces"
  end

  it "should underscore a name" do
    "ClassName".underscored.should == "class_name"
    "OneTwoThree".underscored.should == "one_two_three"
    "One".underscored.should == "one"
  end

  it "should convert a string to title" do
    "class_name".titleized.should == "Class Name"
    "once_upon_a_time".titleized.should == "Once Upon A Time"
    "AbC_eFg_hiJ".titleized.should == "Ab C E Fg Hi J"
    "with spaces".titleized.should == "With Spaces"
    "Some Title".titleized.should == "Some Title"
    "SomeTitle".titleized.should == "Some Title"
  end

end