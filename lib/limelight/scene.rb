#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/java_util'
require 'limelight/prop'
require 'limelight/button_group_cache'
require 'limelight/limelight_exception'
require 'limelight/file_loader'


module Limelight

  # A Scene is a root Prop.  Scenes may be loaded onto a Stage.  In addition to being a Prop object, Scenes have a
  # few extra attributes and behaviors.
  #
  class Scene < Prop    
    include UI::Api::Scene

    attr_reader :button_groups, :casting_director, :cast
    attr_accessor :stage, :visible, :production, :styles
    getters :stage, :loader, :styles
    setters :stage
    event :scene_opened

    alias :visible? :visible

    def initialize(options={})
      path = options.delete(:path) || ""
      @root = FileLoader.for_root(path)
      super(options)
      @button_groups = ButtonGroupCache.new
      @prop_index = {}
      @cast = Module.new
#      illuminate
    end

    # Returns self.  A Scene is it's own scene.
    #
    def scene 
      return self
    end

    # Returns the path to the root directory of the Scene
    #
    def path
      return @root.root
    end

    # Returns the path to the Scene's props file
    #
    def props_file
      return @root.path_to("props.rb")
    end

    # Returns the path to the Scene's props file
    #
    def styles_file
      return @root.path_to("styles.rb")
    end

    #    def add_options(options) #:nodoc:
    #      @options = options
    #      illuminate
    #    end

    # Creates the menu bar for the Scene 
    #
    def menu_bar
      return DSL::MenuBar.build(self) do
        menu("File") do
          item("Open", :open_chosen_production)
          item("Refresh", :reload)
        end
      end
    end

    # Opens a FileChooser for a new Production.  Loads the chosen Production.
    #
    def open_chosen_production
      options = { :title => "Open New Limelight Production", :description => "Limelight Production", :directory => @directory }
      chosen_file = stage.choose_file(options) { |file| Util.is_limelight_scene?(file) || Util.is_limelight_production?(file) }
      if chosen_file
        @directory = File.dirname(chosen_file)
        open_production(chosen_file)
      end
    end

    # Creates a new Producer to open the specified Production.
    #
    def open_production(production_path)
      Thread.new { Context.instance.studio.open(production_path) }
    end

    # Opens the specified Scene on the Stage currently occupied by this Scene.
    # TODO It doesn't quite make sense that a scene loads other scene.  It has to replace itself?
    #
    def load(scene_name)
      @production.producer.open_scene(scene_name, @stage)
    end

    # Add the Prop to the index.  Provides fast lookup by id.
    #
    def index_prop(prop)
      return if prop.id.nil? || prop.id.empty?
      indexee = @prop_index[prop.id]
      if indexee.nil?
        @prop_index[prop.id] = prop
      else
        raise LimelightException.new("Duplicate id: #{prop.id}") if indexee != prop
      end
    end

    # Removed the Prop from the index.
    #
    def unindex_prop(prop)      
      unindex_child_props(prop)
      @prop_index.delete(prop.id) if prop.id
    end

    # Returns a Prop with the specified id.  Returns nil id the Prop doesn't exist in the Scene. 
    #
    def find(id)
      return @prop_index[id.to_s]
    end
        
    def illuminate #:nodoc:
      @styles = @options.delete(:styles_hash) || @styles || {}
      @casting_director = @options.delete(:casting_director) if @options.has_key?(:casting_director)
      @path = @options.delete(:path) if @options.has_key?(:path)
      @production = @options.delete(:production) if @options.has_key?(:production)
      super
    end

    private ###############################################
    
    def unindex_child_props(prop)
      prop.children.each do |child|
        if child.children.empty? 
          @prop_index.delete(child.id)
        else
          unindex_prop(child)
        end
      end
    end
    
    def reload
      load(File.basename(path))
    end

  end
end