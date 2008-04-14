require File.expand_path(File.dirname(__FILE__) + "/../../lib/init")
require 'limelight/composer/lethargy'

module Inspector
  
  def self.extended(block)
    puts "Inspector extended"
  end
  
  def new
  end
  
  def open
    # result = scene.stage.choose_file(:title => "Open Limelight Scene", :description => "Limelight Scene", :directory => @directory) { |file| Limelight::Util.is_limelight_scene?(file) }
    result = "/Users/micahmartin/Projects/limelight/trunk/examples/sandbox/click_me"
    if result
      if Limelight::Util.is_limelight_production?(File.dirname(result))
        load_scene(File.dirname(result), File.basename(result))
      else
        load_scene(result, ".")
      end
      
      devitalize(viewer_stage.current_scene)
    end
  end
  
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