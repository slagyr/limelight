require 'limelight/loaders/file_scene_loader'
require 'limelight/prop_builder'
require 'limelight/styles_builder'
require 'limelight/stage_builder'
require 'limelight/production_builder'
require 'limelight/casting_director'
require 'limelight/stage'
require 'limelight/build_exception'
require 'limelight/theater'
require 'limelight/production'

module Limelight

  class Producer

    def self.open(scene_name)
      producer = new(scene_name)
      producer.open
    end
    
    attr_reader :loader, :theater, :production
    
    def initialize(root_path, theater=nil, production=nil)
      @loader = Loaders::FileSceneLoader.for_root(root_path)
      @theater = theater.nil? ? Theater.new : theater
      @production = production
      @casting_director = CastingDirector.new(loader)
    end
    
    def open()
      establish_production
      Kernel.load(@loader.path_to("init.rb")) if @loader.exists?("init.rb")
      if @loader.exists?("stages.rb")
        load_stages.each { |stage| open_scene(stage.default_scene, stage) }
      else
        open_scene('.', @theater.default_stage)
      end
    end

    def open_scene(path, stage)
      styles = load_styles(path)
      merge_with_root_styles(styles)

      scene = load_props(path, :styles => styles, :casting_director => @casting_director, :loader => @loader, :path => path)     
      scene.production = @production

      stage.open(scene)
    end
    
    def load_stages
      content = @loader.load("stages.rb")
      stages = Limelight.build_stages(@theater) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new("stages.rb", content, e)
        end
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
      return {} if not @loader.exists?(filename)
      content = @loader.load(filename)
      return  Limelight.build_styles do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end
    
    def establish_production
      return if @production
      if @loader.exists?("production.rb")
        content = @loader.load("production.rb")
        @production = Limelight.build_production(@producer, @theater) do
          begin
            eval content
          rescue Exception => e
            raise BuildException.new("production.rb", content, e)
          end
        end
      else
        @production = Production.new(@producer, @theater)
      end
    end

    def root_styles
      return @root_syles if @root_syles
      if @loader.exists?('styles.rb')
        @root_styles = load_styles('.')
      else
        @root_styles = {}
      end
      return @root_styles
    end
    
    private ###############################################
    
    def merge_with_root_styles(styles)
      root_styles.each_pair do |key, value|
        styles[key] = value if !styles.has_key?(key)
      end
    end

  end
  
end