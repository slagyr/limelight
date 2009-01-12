require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/util/downloader'

describe Limelight::Util::Downloader do

  before(:each) do
    Limelight::Data.stub!(:root).and_return(File.expand_path(File.join(TestDir.root, "Limelight")))
    @downloader = Limelight::Util::Downloader.new
  end

  after(:each) do
    TestDir.clean
  end

  it "should download a local file with the file protocol" do
    test_file = TestDir.create_file("test_dir/test_file.txt", "a test file")

    path = @downloader.download("file://#{test_file}")

    File.basename(path).should == "test_file.txt"
    File.dirname(path).should == Limelight::Data.downloads_dir
    File.exists?(path).should == true
    IO.read(path).should == "a test file"
  end

  it "should raise not found exception whe bad URLs" do
    test_file = TestDir.path("test_dir/test_file.txt")

    lambda { @downloader.download("file://#{test_file}") }.should raise_error(Limelight::LimelightException, "Download failed.  Not found: #{test_file}")
  end

  it "should raise error on failed download" do
    lambda { @downloader.download("http://blah.blah/blah.blah") }.should raise_error(Limelight::LimelightException)

    path = File.join(Limelight::Data.downloads_dir, "blah.blah")
    File.exists?(path).should == false
  end

  # The following tests should not run as part of the normal suite.  They are expensive and require the net.

  it "should downaload via HTTP" do
    path = @downloader.download("http://limelight.8thlight.com/images/logo.png")

    File.basename(path).should == "logo.png"
    File.dirname(path).should == Limelight::Data.downloads_dir
    File.exists?(path).should == true
  end

  it "should handle 404s from HTTP" do
    url = "http://limelight.8thlight.com/blah.blah"
    lambda { @downloader.download(url) }.should raise_error(Limelight::LimelightException, "Download failed.  404: Not Found")

    path = File.join(Limelight::Data.downloads_dir, "blah.blah")
    File.exists?(path).should == false
  end
  
  it "should downaload via HTTP" do
    path = @downloader.download("https://www.8thlight.com/images/header.jpg")

    File.basename(path).should == "header.jpg"
    File.dirname(path).should == Limelight::Data.downloads_dir
    File.exists?(path).should == true
  end
  
end
