#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/templates/production_templater'

describe Limelight::Templates::ProductionTemplater do

  it "should initialize settings" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "some_production", :scene_path => "default_scene", :spec_path => "spec")

    templater.target_root.should == "."
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
  end

  it "should initialize settings for deep dir name" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "dir1/dir2/some_production", :scene_path => "default_scene", :spec_path => "spec")

    templater.target_root.should == "./dir1/dir2"
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
  end

  it "should initialize settings for root path" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "/some_production", :scene_path => "default_scene", :spec_path => "spec")

    templater.target_root.should == "/"
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
  end

  it "should generate files" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "some_production", :scene_path => "default_scene", :spec_path => "spec")

    templater.should_receive(:file).with("some_production/production.rb", "production/production.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/stages.rb", "production/stages.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/styles.rb", "production/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/spec/spec_helper.rb", "production/spec/spec_helper.rb.template", templater.tokens)

    templater.generate
  end
  
  it "should generate files with different paths" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "some_production", :scene_path => "default_scene", :spec_path => "../spec")

    templater.should_receive(:file).with("some_production/production.rb", "production/production.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/stages.rb", "production/stages.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/styles.rb", "production/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_production/../spec/spec_helper.rb", "production/spec/spec_helper.rb.template", templater.tokens)

    templater.generate
  end

  it "should have tokens" do
    templater = Limelight::Templates::ProductionTemplater.new(:production_path => "some_production", :scene_path => "default_scene", :spec_path => "spec")

    templater.tokens[:PRODUCTION_NAME].should == "Some Production"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
    templater.tokens[:CURRENT_VERSION].should == Limelight::VERSION::STRING
  end

end