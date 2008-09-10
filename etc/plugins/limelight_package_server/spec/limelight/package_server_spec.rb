#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.dirname(__FILE__) + '/../spec_helper'

class MockController < ApplicationController
  serve_limelight_packages :production_path => "/path/to/productions"
end

describe "LimelightPackageServer" do

  before(:each) do
    @controller = MockController.new
  end

  it "should serve a llp file" do
    mock_package = mock("mock package", :contents => "Limelight Package", :file_name => "the file")
    Limelight::PackageServer::Package.should_receive(:find_by_name).with("MyPackage", "/path/to/productions").and_return(mock_package)
    @controller.params = {:package => "MyPackage"}
    @controller.should_receive(:send_data).with("Limelight Package", :type => "application/x-limelight", :filename => "the file")
    
    @controller.download_package
  end
  
  it "should render 404 if no content found" do
    mock_package = mock("mock package", :contents => nil)
    Limelight::PackageServer::Package.should_receive(:find_by_name).with("MyPackage", "/path/to/productions").and_return(mock_package)
    @controller.params = {:package => "MyPackage"}
    @controller.should_receive(:render).with(:file => "#{RAILS_ROOT}/public/404.html", :status => 404)
    
    @controller.download_package
  end
  
end
