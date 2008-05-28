require File.expand_path(File.dirname(__FILE__) + "/../copyrights/copyrights")

task :copyrights_ruby do
  Copyrights.add("Ruby", File.join(PROJECT_ROOT, 'lib'))
  Copyrights.add("Ruby", File.join(PROJECT_ROOT, 'productions'))
  Copyrights.add("Ruby", File.join(PROJECT_ROOT, 'etc', 'plugins'))
  Copyrights.add("Ruby", File.join(PROJECT_ROOT, 'spec'))
end

task :copyrights_java do
  Copyrights.add("Java", File.join(PROJECT_ROOT, 'src'))
end

task :copyrights => [:copyrights_ruby, :copyrights_java] do
end