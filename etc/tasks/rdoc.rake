require 'rake/rdoctask'

desc 'Generate RDoc'
rd = Rake::RDocTask.new do |rdoc|
  rdoc.rdoc_dir = 'etc/tmp/rdoc'
  rdoc.options << '--title' << 'Limelight' << '--line-numbers' << '--inline-source' << '--main' << 'README'
  rdoc.rdoc_files.include('README', 'CHANGES', 'lib/**/*.rb')
end
task :rdoc