#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/java_util'
require 'limelight/prop'
require 'limelight/button_group_cache'

module Limelight
  class Scene < Prop
    
    include Java::limelight.ui.Scene
  
    attr_reader :button_groups, :styles, :casting_director
    attr_accessor :stage, :loader, :visible, :path, :production
    getters :stage, :loader
    setters :stage
    event :scene_opened
    
    def initialize(options={})
      super(options)
      @scene = self
      @button_groups = ButtonGroupCache.new
      illuminate
    end
  
    def add_options(options)
      @options = options
      illuminate
    end
    
    def illuminate
      @styles = @options.has_key?(:styles) ? @options.delete(:styles) : (@styles || {})
      @casting_director = @options.delete(:casting_director) if @options.has_key?(:casting_director)
      super
    end
    
    def has_static_size?
      return is_static?(style.get_width) && is_static?(style.get_height)
    end
    
    def menu_bar
      return MenuBar.build(self) do
        menu("File") do
          item("Open", :open_chosen_scene)
          item("Refresh", :reload)
        end
      end
    end
    
    def load(path)
      @production.producer.open_scene(path, @stage)
    end
    
    private ###############################################
    
    def is_static?(value)
      return !(value.to_s.include?("%")) && !(value.to_s == "auto")
    end
    
    def open_chosen_scene
      options = { :title => "Open New Limelight Scene", :description => "Limelight Scene", :directory => @directory }
      chosen_file = stage.choose_file(options) { |file| Util.is_limelight_scene?(file) || Util.is_limelight_production?(file) }
      if chosen_file
        @directory = File.dirname(chosen_file)
        producer = Producer.new(chosen_file, @production.theater)
        producer.open
      end
    end
    
    def reload
      load(@path)
    end
  
  end
end