#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/java_util'

describe Class do
  
  class TestClass
    attr_accessor :foo, :bar
    getters :foo, :bar
    setters :foo, :bar
  end
  
  before(:each) do
    @obj = TestClass.new
  end
  
  it "should have java setters" do
    @obj.setFoo("foo value")
    @obj.setBar("bar value")
    
    @obj.foo.should == "foo value"
    @obj.bar.should == "bar value"
  end
  
  it "should have java getters" do
    @obj.foo = "foo value"
    @obj.bar = "bar value"
    
    @obj.getFoo().should == "foo value"
    @obj.getBar().should == "bar value"
  end

end


