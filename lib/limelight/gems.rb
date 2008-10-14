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

        self.current_production = nil
      end

    end


  end
end