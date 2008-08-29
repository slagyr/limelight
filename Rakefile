PROJECT_ROOT = File.expand_path(File.dirname(__FILE__))
TASK_DIR = File.expand_path(File.dirname(__FILE__) + "/etc/tasks")
Gem.clear_paths
ENV["GEM_HOME"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/etc/gems")
puts "Using gem home: #{Gem.dir}"
puts "Using gem path: #{Gem.path}"

require File.expand_path(File.dirname(__FILE__) + "/lib/limelight/version")

Dir.glob(File.join(TASK_DIR, "*.rake")).each do |rakefile|
  load rakefile
end

task :jar do
  system "ant jar"
end

task :init_jruby do
  JRUBY_TAG = "jruby-1_1_3"
  jruby_bin_path = File.join(PROJECT_ROOT, 'jruby', 'bin')
  jruby_ruby_path = File.join(PROJECT_ROOT, 'jruby', 'lib', 'ruby')
  FileUtils.rm_r(jruby_bin_path, :force => true)
  FileUtils.rm_r(jruby_ruby_path, :force => true)
  system "svn export http://svn.codehaus.org/jruby/tags/#{JRUBY_TAG}/bin/ #{jruby_bin_path}"
  system "svn export http://svn.codehaus.org/jruby/tags/#{JRUBY_TAG}/lib/ruby #{jruby_ruby_path}"

end

task :init => [:jar, :init_jruby, :gems] do
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
