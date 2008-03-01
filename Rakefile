require File.expand_path(File.dirname(__FILE__) + "/ruby_src/lib/init")

task :gems do
  puts "Establishing Gems"
  gem_pattern = /\W+(\d+\.?)*\.gem/
  gem_dir_pattern = /\W+(\d+\.?)*/
  all_gems = []
  Dir.entries("etc/gems/cache").each { |entry| all_gems << entry[0...-4] if entry.match(gem_pattern) }
  installed_gems = []
  Dir.entries("etc/gems/gems").each { |entry| installed_gems << entry if entry.match(gem_dir_pattern)} if File.exists?("etc/gems/gems")   
  gems_to_install = all_gems - installed_gems
  gems_to_install.each do |gem|
    puts "Installing gem: #{gem}"
    system "gem install -i etc/gems --no-rdoc --no-ri -f etc/gems/cache/#{gem}.gem"
  end
end

task :spec do
  ARGV.clear
  ARGV << "ruby_src/spec"
  gem 'rspec'
  load 'spec'
end

task :junit do
  output = `ant unit_test`
  raise output if $?.exitstatus != 0
end

task :tests => [:junit, :spec]
