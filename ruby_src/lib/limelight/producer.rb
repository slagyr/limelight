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
      if @loader.exists?("production.rb")
        @stages = load_stages
      else
        stage = Stage.new(self, "Limelight")
        stage.default_scene = "."
        @stages = [stage]
      end
      @stages.each { |stage| open_scene(stage.default_scene, stage) }
    end

    def open_scene(path, stage = nil)
      stage ||= @stages[0]
      styles = load_styles(path)
      merge_with_stage_styles(styles, stage)
      casting_director = CastingDirector.new(loader)

      scene = load_props(path, :styles => styles, :casting_director => casting_director, :loader => @loader, :path => path)

      stage.open(scene)
    end
    
    def load_stages
      content = @loader.load("production.rb")
      stages = Limelight.build_stages(self) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new("production.rb", content, e)
        end
      end
      
      if @loader.exists?('styles.rb')
        root_styles = load_styles('.')
        stages.each { |stage| stage.styles = root_styles }
      end
      
      return stages
    end
    
    def load_props(path, options = {})
      return Scene.new(options) if path.nil?
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
      return {} if path.nil?
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
    
    private ###############################################
    
    def merge_with_stage_styles(styles, stage)
      stage.styles.each_pair do |key, value|
        styles[key] = value if !styles.has_key?(key)
      end
    end

  end
  
end