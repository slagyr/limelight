#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
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

  it "should be find unique filenames" do
    test_file = TestDir.create_file("test_dir/test_file.txt", "a test file")

    path1 = @downloader.download("file://#{test_file}")
    path2 = @downloader.download("file://#{test_file}")
    path3 = @downloader.download("file://#{test_file}")

    File.basename(path1).should == "test_file.txt"
    File.basename(path2).should == "test_file_2.txt"
    File.basename(path3).should == "test_file_3.txt"
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

#  it "should downaload via HTTP" do
#    path = @downloader.download("http://limelight.8thlight.com/images/logo.png")
#
#    File.basename(path).should == "logo.png"
#    File.dirname(path).should == Limelight::Data.downloads_dir
#    File.exists?(path).should == true
#  end
#
#  it "should handle 404s from HTTP" do
#    url = "http://limelight.8thlight.com/blah.blah"
#    lambda { @downloader.download(url) }.should raise_error(Limelight::LimelightException, "Download failed.  404: Not Found")
#
#    path = File.join(Limelight::Data.downloads_dir, "blah.blah")
#    File.exists?(path).should == false
#  end
#
#  it "should downaload via HTTPS" do
#    path = @downloader.download("https://www.8thlight.com/images/header.jpg")
#
#    File.basename(path).should == "header.jpg"
#    File.dirname(path).should == Limelight::Data.downloads_dir
#    File.exists?(path).should == true
#  end
#
#  it "should know how to get the filename from Content-Disposition" do
#    path = @downloader.download("http://localhost:3000/playbills/1.llp")
#
#    File.basename(path).should == "simon.llp"
#  end
#
#  it "should handle HTTP redirects" do
#    path = @downloader.download("http://rubyforge.org/frs/download.php/37521/limelight-0.0.1-java.gem")
#
#    File.basename(path).should == "limelight-0.0.1-java.gem"
#  end
  
end
