#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Gems

    class << self

      attr_accessor :current_production

      def load_path
        @load_path = $: if @load_path.nil?
        return @load_path
      end

      def install(gem_name, paths)
        stack = paths.dup
        while !stack.empty?
          load_path.unshift File.join(current_production.gems_directory, gem_name, stack.pop)
        end
      end

      #TODO - MDM - This doesn't work when loading gems like: gem 'gem_name'.  Need make use of Gem::SourceIndex. See SourceIndex.load_gems_in
      def install_gems_in_production(production)
        self.current_production = production

        gems_dir = current_production.gems_directory
        if File.exists?(gems_dir) && File.directory?(gems_dir)
          gems = Dir.entries(gems_dir).select { |dir| dir != "." && dir != ".." }
          gems.sort!
          gems.reverse!
          gems.each do |gem_name|
            init_file = File.join(gems_dir, gem_name, 'limelight_init.rb')
            if File.exists?(init_file)
              load init_file
            else
              puts "WARNING: Frozen gem (#{gem_name}) is missing limelight_init.rb file."
            end
          end
        end

        Gem.use_paths(self.current_production.gems_root, Gem.default_path)

        self.current_production = nil        
      end

    end


  end
end