#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")

describe "Utilitites Production" do

  uses_limelight

  before(:each) do
    @result = nil
    production.production_opening
  end

  after(:each) do
    @thread.kill if @thread
    production.theater.stages.each { |stage| stage.close }
  end

  after(:all) do
#    Java::java.awt.Frame.getFrames.each { |frame| frame.close; frame.dispose; puts frame }
#    Java::limelight.util.Threads.showAll
  end

  def start_alert()
    @thread = Thread.new do
      begin
        @result = production.alert("Some Message")
      rescue Exception => e
        puts e
        puts e.backtrace
      end
    end
  end

  def start_proceed_with_incompatible_version()
    @thread = Thread.new do
      begin
        @result = production.proceed_with_incompatible_version?("Some Production", "1.2.3")
      rescue Exception => e
        puts e
        puts e.backtrace
      end
    end
  end

  def wait_for
    10.times do
      return if yield
      sleep(0.1)                                                                  
    end
    raise "the desired condition was not met on time"
  end

  def scene_open?(stage_name)
    stage = production.theater[stage_name]
    return stage != nil && stage.current_scene != nil
  end

  it "should construct stage on load_with_incomptible_version_scene" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = production.theater["Incompatible Version"]
    stage.should_not == nil
    stage.location.should == ["center", "center"]
    stage.size.should == ["400", "auto"]
    stage.background_color.should == "#FFFFFF"
    stage.framed?.should == false
    stage.always_on_top?.should == true
    stage.vital?.should == false
  end

  it "should not construct the incomptible_version stage twice" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = production.theater["Incompatible Version"]

    lambda { production.load_incompatible_version_scene("Some Production", "1.2.3") }.should_not raise_error
  end

  it "should load the incomptible_version scene" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = production.theater["Incompatible Version"]

    stage.current_scene.should_not == nil
    scene = stage.current_scene
    scene.find("production_name_label").text.should == "Some Production"
    scene.find("required_version_label").text.should == "1.2.3"
    scene.find("current_version_label").text.should == Limelight::VERSION::STRING
  end

  it "should return true when clicking proceed" do
    @result = nil
    start_proceed_with_incompatible_version()
    wait_for { scene_open?("Incompatible Version") }

    scene = production.theater["Incompatible Version"].current_scene
    scene.find("proceed_button").button_pressed(nil)
    wait_for { !@thread.alive? }

    @result.should == true
  end

  it "should return false when clicking cancel" do
    @result = nil
    start_proceed_with_incompatible_version()
    wait_for { scene_open?("Incompatible Version") }

    scene = production.theater["Incompatible Version"].current_scene
    scene.find("cancel_button").button_pressed(nil)
    wait_for { !@thread.alive? }

    @result.should == false
  end

  it "should construct alert stage" do
    production.load_alert_scene("Some Message")
    stage = production.theater["Alert"]
    stage.should_not == nil
    stage.location.should == ["center", "center"]
    stage.size.should == ["400", "auto"]
    stage.background_color.should == "#FFFFFF"
    stage.framed?.should == false
    stage.always_on_top?.should == true
    stage.vital?.should == false
  end

  it "should load the incomptible_version scene" do
    production.load_alert_scene("Some Message")
    stage = production.theater["Alert"]

    stage.current_scene.should_not == nil
    scene = stage.current_scene
    scene.find("title").text.should == "Limelight Alert"
    scene.find("message").text.should == "Some Message"
  end

  it "should close the alert when clicking OK" do
    start_alert
    wait_for { scene_open?("Alert") }

    scene = production.theater["Alert"].current_scene
    scene.find("ok_button").button_pressed(nil)

    wait_for { !@thread.alive? }
    @result.should == true
  end

end