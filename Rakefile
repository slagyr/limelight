PROJECT_ROOT = File.expand_path(File.dirname(__FILE__))
TASK_DIR = File.expand_path(File.dirname(__FILE__) + "/etc/tasks")
Gem.clear_paths
ENV["GEM_HOME"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")
puts "Using gem home: #{Gem.dir}"
puts "Using gem path: #{Gem.path}"

require File.expand_path(File.dirname(__FILE__) + "/lib/limelight/version")

load File.join(TASK_DIR, "dev_gems.rake")
load File.join(TASK_DIR, "gem.rake")
load File.join(TASK_DIR, "dist.rake")
load File.join(TASK_DIR, "copyrights.rake")

task :jar do
  # system "ant jar"
end

task :spec do
  ARGV.clear
  ARGV << "spec"
  gem 'rspec'
  load 'spec'
end

task :junit do
  output = `ant unit_test`
  raise output if $?.exitstatus != 0
end

task :tests => [:junit, :spec]
