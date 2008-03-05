require 'limelight/loaders/file_scene_loader'
require 'limelight/prop_builder'
require 'limelight/styles_builder'
require 'limelight/casting_director'
require 'limelight/stage'
require 'limelight/build_exception'
require 'limelight/stage_builder'

module Limelight

  class Producer

    def self.open(scene_name)
      producer = new(scene_name)
      producer.open
    end
    
    attr_reader :loader
    
    def initialize(root_path)
      @loader = Loaders::FileSceneLoader.for_root(root_path)
    end
    
    def open()
      if @loader.exists?("index.rb")
        @stage = open_stage
        open_scene(@stage.default_scene)
      else
        @stage = Stage.new(self)
        open_scene(".")
      end
    end
    
    def open_stage
      content = @loader.load("index.rb")
      stage = Limelight.build_stage(self) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new("index.rb", content, e)
        end
      end
      
      stage.styles = load_styles('.')
      
      return stage
    end
    
    def open_scene(path)
      scene_specific_loader = Loaders::FileSceneLoader.for_root(loader.path_to(path)) #TODO - MDM - Shouldn't really be needed
      styles = load_styles(path)
      merge_with_stage_styles(styles)
      casting_director = CastingDirector.new(scene_specific_loader)
      
      scene = load_props(path, :styles => styles, :casting_director => casting_director, :loader => @loader)
      
      @stage.open(scene)
    end
    
    def load_props(path, options = {})
      filename = File.join(path, "props.rb")
      content = @loader.load(filename)
      options[:build_loader] = @loader
      return Limelight.build_scene(options) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end
    
    def load_styles(path)
      filename = File.join(path, "styles.rb")
      content = @loader.load(filename)
      return  Limelight.build_styles do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end
    
    def merge_with_stage_styles(styles)
      @stage.styles.each_pair do |key, value|
        styles[key] = value if !styles.has_key?(key)
      end
    end

  end
  
end