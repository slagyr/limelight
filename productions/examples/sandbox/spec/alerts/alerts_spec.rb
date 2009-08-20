#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")

describe "Alerts" do

  uses_scene "alerts", :hidden => true

  it "should have default text" do
    scene.children.size.should == 1
    root = scene.children[0]
    root.text.should == "This is the Alerts scene."
  end

end