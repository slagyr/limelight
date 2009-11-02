PROJECT_ROOT = File.expand_path(File.dirname(__FILE__))
TASK_DIR = File.expand_path(File.dirname(__FILE__) + "/etc/tasks")
Gem.clear_paths
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")

require File.expand_path(File.dirname(__FILE__) + "/lib/limelight/version")

Dir.glob(File.join(TASK_DIR, "*.rake")).each do |rakefile|
  load rakefile
end

task :jar do
  system "ant jar"
end

task :init => [:jar, :jruby_gems, :dev_gems] do
end

task :spec do
  gem 'rspec'
  require 'spec/rake/spectask'
  Spec::Rake::SpecTask.new(:lib_specs){|t| t.spec_files = FileList['spec/**/*.rb']}
  Rake::Task[:lib_specs].invoke
end

task :junit do
  output = `ant unit_test`
  raise output if $?.exitstatus != 0
end

task :tests => [:junit, :spec]

task :continuous do
  require 'tmpdir'
  tmpdir = File.join(Dir.tmpdir, "limelight_darwin_#{rand}")
  system "mkdir #{tmpdir}"
  puts "contents of darwin:"
  system "ls -la src/limelight/os/darwin"
  system "mv src/limelight/os/darwin/* #{tmpdir}"
  puts "removed contents of darwin dir..."
  system "ls -la src/limelight/os/darwin"
  system "pwd"
  begin
    Rake::Task[:tests].invoke
  ensure
    system "mv #{tmpdir}/* src/limelight/os/darwin"
  end 
end

task :default => :continuous
