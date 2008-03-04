require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/producer'

describe Limelight::Producer do
  
  before(:each) do
    @producer = Limelight::Producer.new("/tmp")
    @loader = @producer.loader
  end
  
  it "should have loader on creation" do
    @producer.loader.current_dir.should == "/tmp"
  end
  
  it "should load props" do
    @loader.should_receive(:load).with("./props.rb").and_return("child :id => 321")
    
    page = @producer.load_props(".", :illuminator => make_mock("casting_director", :fill_cast => nil))
    page.children.size.should == 1
    page.children[0].class_name.should == "child"
    page.children[0].id.should == 321
  end
  
  it "should load styles" do
    @loader.should_receive(:load).with("./styles.rb").and_return("alpha { width 100 }")
    
    styles = @producer.load_styles(".")
    styles.size.should == 1
    styles["alpha"].width.should == "100"
  end
  
  it "should format prop errors well" do
    @loader.should_receive(:load).with("./props.rb").and_return("one\n+\nthree")
    
    begin
      result = @producer.load_props(".", :illuminator => make_mock("casting_director", :fill_cast => nil))
      result.should == nil # should never execute
    rescue Limelight::BuildException => e
      e.line_number.should == 3
      e.filename.should == "./props.rb"
      e.message.should include("./props.rb:3: undefined method `+@' for ")
    end
  end
  
  it "should format styles errors well" do
    @loader.should_receive(:load).with("./styles.rb").and_return("one {}\ntwo {}\n-\nthree {}")
    
    begin
      result = @producer.load_styles(".")
      result.should == nil # should never execute
    rescue Limelight::BuildException => e
      e.line_number.should == 4
      e.filename.should == "./styles.rb"
      e.message.should include("./styles.rb:4: undefined method `-@' for #<Java::LimelightUi::FlatStyle:0x")
    end
  end
  
  it "should load a book when index.rb exists" do
    @loader.should_receive(:exists?).with("index.rb").and_return true
    @producer.should_receive(:open_book).and_return(make_mock("book", :default_page => "abc"))
    @producer.should_receive(:open_page).with("abc")
    
    @producer.open
  end
  
  it "should load a page when index.rb doesn't exists" do
    @loader.should_receive(:exists?).with("index.rb").and_return false
    @producer.should_not_receive(:open_book)
    @producer.should_receive(:open_page).with(".")
    
    @producer.open
  end
  
  
  
end
