require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/specs/spec_helper'

module TestModule

  def my_extended_method
    true
  end
end

describe "The Uses Player Directive" do
  uses_player :test_module

  before(:all) do
    $PRODUCTION_PATH = File.dirname(__FILE__) unless defined?($PRODUCTION_PATH)
  end

  it "should have a prop named player" do
    player.should be_instance_of(Limelight::Prop)
  end

  it "should only return one player" do
    player.object_id.should == player.object_id
  end

  it "should return a scene with that player" do
    scene.children.should == [player]
  end

  it "should extend the prop by the player" do
    player.my_extended_method.should be_true 
  end
  
  it "should allow setting on the scene variables that don't otherwise exist" do
    scene.rdoc = {"this" => "is why I need this feature"}
    
    scene.rdoc.should == {"this" => "is why I need this feature"}
  end
  
  it "should not add accessor for non-accessor methods" do
    lambda{scene.method_should_not_be_created}.should raise_error(NoMethodError)
  end
  
  it "should illuminate the scene" do
    scene.should be_illuminated
  end
  
  it "should have a production" do
    scene.production.should_not be_nil
  end
  
  it "should illuminate the player" do
    player.should be_illuminated
  end
end