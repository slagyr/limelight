require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/templates/production_templater'

describe Limelight::Templates::ProductionTemplater do

  it "should initialize settings" do
    templater = Limelight::Templates::ProductionTemplater.new("some_production", "default_scene")

    templater.target_root.should == "."
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
  end

  it "should initialize settings for deep dir name" do
    templater = Limelight::Templates::ProductionTemplater.new("dir1/dir2/some_production", "default_scene")

    templater.target_root.should == "."
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
  end

  it "should generate files" do
    templater = Limelight::Templates::ProductionTemplater.new("some_production", "default_scene")

    templater.should_receive(:file).with("some_production/production.rb", "production.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/init.rb", "init.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/stages.rb", "stages.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/styles.rb", "styles.rb.template", templater.tokens)

    templater.generate
  end

end