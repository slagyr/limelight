require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
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

describe String do
  
  it "should convert into camel case" do
    "class_name".camalized.should == "ClassName"
    "once_upon_a_time".camalized.should == "OnceUponATime"
    "AbC_eFg_hiJ".camalized.should == "AbcEfgHij"
    "with spaces".camalized.should == "WithSpaces"
  end
  
  it "should convert into camel case" do
    "class_name".camalized(:lower).should == "className"
    "once_upon_a_time".camalized(:lower).should == "onceUponATime"
    "AbC_eFg_hiJ".camalized(:lower).should == "abcEfgHij"
    "with spaces".camalized(:lower).should == "withSpaces"
  end
  
end
