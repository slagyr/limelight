require "drb"
class MouseEvent
  include DRb::DRbUndumped

  attr_accessor :x, :y

  def initialize(x, y)
    @x = x
    @y = y
  end
end

Before do
end

at_exit do
  $production.close
end

def scene
  return $production.theater.stages[0].current_scene
end

Given /^I have the sanbox production opened$/ do
  if $production.nil?
    prod_dir = File.expand_path(File.dirname(__FILE__) + "/../../")
    @ll_thread = Thread.new { system "jruby -S limelight open --drb_port=9000 #{prod_dir}" }
    require "drb"
    DRb.start_service
    $production = DRbObject.new(nil, "druby://localhost:9000")
    begin
      $production.name
    rescue Exception => e
      sleep(1)
      retry
    end
  end
end

When /^I do nothing$/ do
end

Then /^The active stage title should be "([^\"]*)"$/ do |title|
  active_stage = $production.theater.stages[0]
  active_stage.title.should == title
end

When /^I click on "([^\"]*)"$/ do |id|
  scene.find(id).mouse_clicked(nil)
  sleep(0.25)
end

Then /^The text of "([^\"]*)" should include "([^\"]*)"$/ do |id, text|
  scene.find(id).text.include?(text).should == true
end

When /^I hover over "([^\"]*)"$/ do |id|
  $hovered_prop.mouse_exited(nil) if $hovered_prop
  $hovered_prop = scene.find(id)
  $hovered_prop.mouse_entered(nil) if $hovered_prop
  sleep(2.5)
end

When /^I scroll "([^\"]*)" in "([^\"]*)"$/ do |direction, id|
  prop = scene.find(id)
  scrollbar = direction == "right" ? prop.panel.horizontal_scrollbar : prop.panel.vertical_scrollbar
  max = scrollbar.getMaximumValue()
  unit = max / 40.0
  begin
    40.times do
      scrollbar.setValue(scrollbar.getValue() + unit)
      sleep(0.05)
    end
  rescue
  end
end

When /^I set value of "([^\"]*)" to "([^\"]*)"$/ do |id, value|
  prop = scene.find(id)
  begin
    prop.checked = true
  rescue
  end 
  prop.text = value
  begin
    prop.focus_lost(nil)
  rescue
  end
  sleep(0.5)
end

When /I click on all the rounded corners/ do
  3.times do |row|
    5.times do |col|
      prop = scene.find("rounded_corner_#{row}_#{col}")
      5.times do
        prop.mouse_clicked(nil)
        sleep(0.05)
      end
    end
  end
end

When /^I move the mouse over "([^\"]*)" at (\d+), (\d+)$/ do |id, x, y|
  prop = scene.find(id)
  e = MouseEvent.new(x.to_i, y.to_i)
  prop.mouse_moved(e)
  sleep(1.5)
end

When /^I press the mouse on "([^\"]*)" at (\d+), (\d+)$/ do |id, x, y|
  prop = scene.find(id)
  e = MouseEvent.new(x.to_i, y.to_i)
  prop.mouse_pressed(e)
  sleep(1)
end

When /^I drag the mouse on "([^\"]*)" to (\d+), (\d+)$/ do |id, x, y|
  prop = scene.find(id)
  e = MouseEvent.new(x.to_i, y.to_i)
  prop.mouse_dragged(e)
end

When /^I press "([^\"]*)"$/ do |id|
  prop = scene.find(id)
  begin
    prop.checked = !prop.checked?
  rescue
  end
  prop.button_pressed(nil)
  sleep(1)
end






