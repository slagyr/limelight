PROJECT_ROOT = File.expand_path(File.dirname(__FILE__))
TASK_DIR = File.expand_path(File.dirname(__FILE__) + "/etc/tasks")
Gem.clear_paths
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")

require File.expand_path(File.dirname(__FILE__) + "/lib/limelight/version")

def run_command(command)
  output = `#{command}`
  exit_code = $?.exitstatus
  if exit_code != 0
    puts "Command failed with code #{exit_code}: #{command}"
    raise output
  else
    puts "Command executed successfully: #{command}"
  end
end

Dir.glob(File.join(TASK_DIR, "*.rake")).each do |rakefile|
  load rakefile
end

task :jar do
  system "ant jar"
end

task :init => [:jar, :jruby_gems, :dev_gems] do
end

task :spec do
  begin
    require 'java'
    gem 'rspec'
    require 'spec/rake/spectask'
    Spec::Rake::SpecTask.new(:lib_specs){|t| t.spec_files = FileList['spec/**/*.rb']}
    Rake::Task[:lib_specs].invoke
  rescue LoadError
    run_command "java -jar lib/jruby-complete-1.4.0.jar -S spec spec"
  end
end

task :junit do
  run_command "ant unit_test"
end

task :jar do
  run_command "ant jar"
end

task :tests => [:junit, :spec]

task :continuous => [:tests_cont]

task :junit_cont do
  output = `ant unit_test.cont`
  raise output if $?.exitstatus != 0
end

task :tests_cont => [:junit_cont, :spec]

task :default => :continuous
