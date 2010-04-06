#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # Freeze a gem into the production.  Frozen gems are unpacked into the root level gem directory.
    # Limelight will automatically require a production's frozen gems when loaded.
    #
    #  Usage: limelight freeze [options] <gem_name|gem_file>
    #      freeze a gem into a production.
    #      options:
    #      -h, --help                       Prints this usage summary.
    #      -p, --production=<production>    Specify the production where the gem will be frozen.  Default is '.'.
    #      -v, --version=<version>          Specify the gem version. Defaults to latest. Ignored if file provided.
    #
    class FreezeCommand < Command

      install_as "freeze"

      def self.description #:nodoc:
        return "freeze a gem into a production."
      end

      attr_reader :gem_name, :production_path, :gem_version

      def initialize() #:nodoc:
        @production_path = "."
#        self.print_backtrace = true
      end

      def is_gem_file?(name) #:nodoc:
        return File.extname(name) == ".gem"
      end

      protected ###########################################

      def process  #:nodoc:
        check_production_path
        gem_path = is_gem_file?(@gem_name) ? @gem_name : find_system_gem
        raise "Gem file does not exist: #{gem_path}" unless File.exists?(gem_path)
        freeze_gem(gem_path)
      end

      def parse_remainder(args) #:nodoc:
        @gem_name = args.shift
        raise "Gem name paramter missing." if @gem_name.nil?
      end

      def build_options(spec) #:nodoc:
        spec.on("-p <production>", "--production=<production>", "Specify the production where the gem will be frozen.  Default is '.'.") { |value| @production_path = value }
        spec.on("-v <version>", "--version=<version>", "Specify the gem version. Defaults to latest. Ignored if file provided.") { |value| @gem_version = value }
      end

      def parameter_description #:nodoc:
        return "[options] <gem_name|gem_file>"
      end

      def do_requires #:nodoc:
        require 'limelight/util'
        require 'limelight/templates/templater'
        require 'rubygems'
        require 'rubygems/commands/unpack_command'
      end

      private #############################################

      def check_production_path
        if !(Util.is_limelight_production?(@production_path) || Util.is_limelight_scene?(@production_path))
          raise "The production path '#{@production_path}' doesn't appear to be a Limelight production directory."
        end
      end

      def find_system_gem
        if @gem_version
          @gem_spec = Gem.cache.find_name(@gem_name, "= #{@gem_version}").first
          raise "Could not find gem (#{@gem_name}-#{@gem_version})." if @gem_spec.nil?
        else
          @gem_spec = Gem.cache.find_name(@gem_name).sort_by { |g| g.version }.last
          raise "Could not find gem (#{@gem_name})." if @gem_spec.nil?
        end

        return Gem::Commands::UnpackCommand.new.get_path(@gem_name, @gem_spec.version.version)
      end

      def freeze_gem(gem_path)
        @gem_spec = Gem::Format.from_file_by_path(gem_path).spec unless @gem_spec
        @gem_dir_name = "#{@gem_spec.name}-#{@gem_spec.version}"

        establish_gem_dir

        gem_installer = Gem::Installer.new(gem_path)
        @templater.logger.log("unpacking gem", @gem_dir_path)
        gem_installer.unpack(@gem_dir_path)
        
        copy_gem_spec
      end
      
      def copy_gem_spec
        @templater.directory(File.join("__resources", "gems", "specifications"))
        FileUtils.copy(gemspec_path, local_gemspec_path) if File.exists?(gemspec_path)

        install_limelight_hook
      end
      
      def gemspec_path
        return File.join(Gem.dir, "specifications", "#{@gem_dir_name}.gemspec")
      end
      
      def local_gemspec_path
        return File.join(@production_path, "__resources", "gems", "specifications")
      end
      
      def establish_gem_dir
        @templater = Templates::Templater.new(@production_path)
        @gem_dir_path = File.join(@production_path, "__resources", "gems", "gems", @gem_dir_name)
        raise "The gem (#{@gem_dir_name}) is already frozen." if File.exists?(@gem_dir_path)
        @templater.directory(File.join("__resources", "gems", "gems", @gem_dir_name))
      end

      def install_limelight_hook
        tokens = { :GEM_NAME => @gem_dir_name, :PATHS => @gem_spec.require_paths.inspect }
        @templater.file(File.join("__resources", "gems", "gems", @gem_dir_name, "limelight_init.rb"), "freezing/limelight_init.rb.template", tokens)
      end

    end

  end
end