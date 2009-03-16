#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../lib/init")
require 'limelight/composer/lethargy'

module Inspector
  
  def self.extended(block)
    puts "Inspector extended"
    block.production.inspector = block
  end
  
  def new
  end
  
  def open
    # result = scene.stage.choose_file(:title => "Open Limelight Scene", :description => "Limelight Scene", :directory => @directory) { |file| Limelight::Util.is_limelight_scene?(file) }
    # result = "/Users/micahmartin/Projects/limelight/trunk/examples/sandbox/click_me"
    result = "Y:\\Projects\\limelight\\trunk\\examples\\sandbox\\click_me"
    if result
      if Limelight::Util.is_limelight_production?(File.dirname(result))
        load_scene(File.dirname(result), File.basename(result))
      else
        load_scene(result, ".")
      end
      
      devitalize(viewer_stage.current_scene)
      prop_tree.build_tree(viewer_stage.current_scene)
    end
  end
  
  def inspect_prop(prop)
    style_table.populate(prop.style)
    prop_tree.highlight(prop)
  end
  
  def style_table
    return find('style_table')
  end
  
  def prop_tree
    return find('prop_tree')
  end
  
  private #################################################
  
  def load_scene(root, scene_path)
    producer = Limelight::Producer.new(root, theater, scene.production)
    scene_path = scene_path
    
    producer.open_scene(scene_path, viewer_stage)
  end
  
  def theater
    return scene.production.theater
  end
  
  def viewer_stage
    return theater['viewer']
  end
  
  def devitalize(prop)
    lethargize(prop)
    prop.include_player(Limelight::Composer::Lethargy)
    prop.children.each { |child| devitalize(child) }
  end
  
  def lethargize(prop)
    class << prop
      Limelight::Prop::EVENTS.each do |sym| 
        begin 
          remove_method sym 
        rescue Exception
        end
      end
    end
  end
  
end