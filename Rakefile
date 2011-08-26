#require 'java'
PROJECT_ROOT = File.expand_path(File.dirname(__FILE__))
TASK_DIR = File.expand_path(File.dirname(__FILE__) + "/etc/tasks")
MODULES = %w{java utilities ruby clojure}

Dir.glob(File.join(TASK_DIR, "*.rake")).each do |rakefile|
  load rakefile
end

def module_tasks(task)
  MODULES.map { |m| "#{m}:#{task}" }
end

%w{init clean deps test build}.each do |name|
  desc "#{name} all modules"
  task name => module_tasks(name)
end

task :default => :build
