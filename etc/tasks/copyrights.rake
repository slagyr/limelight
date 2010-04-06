def load_copyrights()
  require 'mmcopyrights'
  copyright_text = IO.read(File.join(PROJECT_ROOT, 'etc', 'copyright.txt'))
  return copyright_text
end

task :copyrights_ruby do
  copyright_text = load_copyrights()
  MM::Copyrights.process(File.join(PROJECT_ROOT, 'lib'), "rb", "#-", copyright_text)
  MM::Copyrights.process(File.join(PROJECT_ROOT, 'etc', 'plugins'), "rb", "#-", copyright_text)
  MM::Copyrights.process(File.join(PROJECT_ROOT, 'spec'), "rb", "#-", copyright_text)
end

task :copyrights_java do
  copyright_text = load_copyrights()
  MM::Copyrights.process(File.join(PROJECT_ROOT, 'src'), "java", "//-", copyright_text)
end

task :copyrights => [:copyrights_ruby, :copyrights_java] do
end