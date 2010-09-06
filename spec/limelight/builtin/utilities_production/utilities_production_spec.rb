#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
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
    Java::java.awt.Frame.getFrames.each { |frame| frame.close; frame.dispose; }
#    Java::limelight.util.Threads.showAll
    Limelight::Context.instance.killThreads();
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

  def wait_for_stage(stage_name)
    stage = nil
    while stage.nil?
      stage = production.theater[stage_name]
      sleep(0.1)
    end
    raise "the stage '#{stage_name}' was never loaded" if stage.nil?
    return stage
  end

  it "should construct stage on load_with_incompatible_version_scene" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = wait_for_stage("Incompatible Version")
    stage.should_not == nil
    stage.location.should == ["center", "center"]
    stage.size.should == ["400", "auto"]
    stage.background_color.should == "#ffffffff"
    stage.framed?.should == false
    stage.always_on_top?.should == true
    stage.vital?.should == false
  end

  it "should not construct the incompatible_version stage twice" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = wait_for_stage("Incompatible Version")

    lambda { production.load_incompatible_version_scene("Some Production", "1.2.3") }.should_not raise_error
  end

  it "should load the incompatible_version scene" do
    production.load_incompatible_version_scene("Some Production", "1.2.3")
    stage = wait_for_stage("Incompatible Version")

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

    stage = wait_for_stage("Incompatible Version")
    scene = stage.current_scene
    mouse.click(scene.find("proceed_button"))
    wait_for { !@thread.alive? }

    @result.should == true
  end

  it "should return false when clicking cancel" do
    @result = nil
    start_proceed_with_incompatible_version()
    wait_for { scene_open?("Incompatible Version") }

    stage = wait_for_stage("Incompatible Version")
    scene = stage.current_scene
    mouse.click(scene.find("cancel_button"))
    wait_for { !@thread.alive? }

    @result.should == false
  end

  it "should construct alert stage" do
    production.load_alert_scene("Some Message")
    stage = production.theater["Alert"]
    stage.should_not == nil
    stage.location.should == ["center", "center"]
    stage.size.should == ["400", "auto"]
    stage.background_color.should == "#ffffffff"
    stage.framed?.should == false
    stage.always_on_top?.should == true
    stage.vital?.should == false
  end

  it "should load the alert scene" do
    production.load_alert_scene("Some Message")
    stage = wait_for_stage("Alert")
    stage.should_not == nil

    stage.current_scene.should_not == nil
    scene = stage.current_scene
    scene.find("title").text.should == "Limelight Alert"
    scene.find("message").text.should == "Some Message"
  end

  it "should close the alert when clicking OK" do
    start_alert
    wait_for { scene_open?("Alert") }

    stage = wait_for_stage("Alert")
    scene = stage.current_scene
    mouse.click(scene.find("ok_button"))

    wait_for { !@thread.alive? }
    @result.should == true
  end

end