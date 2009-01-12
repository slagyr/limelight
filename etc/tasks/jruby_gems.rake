def installed_jruby_gems
  gem_dir_pattern = /\W+(\d+\.?)*/
  installed_gems = []
  Dir.entries("jruby/lib/ruby/gems/1.8/gems").each { |entry| installed_gems << entry if entry.match(gem_dir_pattern)} if File.exists?("jruby/lib/ruby/gems/1.8/gems")
  return installed_gems
end

def init_jruby_gems
  ENV["GEM_HOME"] = File.expand_path(File.dirname(__FILE__) + "/../../jruby/lib/ruby/gems/1.8/")
  puts "Using gem home: #{Gem.dir}"
end

task :jruby_gems do
  puts "Establishing Gems"
  init_jruby_gems
  gem_pattern = /.*\.gem/
  all_gems = []
  Dir.entries("etc/gems/required_jruby_gems").each { |entry| all_gems << entry[0...-4] if entry.match(gem_pattern) }
  gems_to_install = all_gems - installed_jruby_gems
  gems_to_install.each do |gem|
    puts "Installing gem: #{gem}"
    system "jruby/bin/gem install etc/gems/required_jruby_gems/#{gem}.gem -f --no-rdoc --no-ri"
  end
end

task :clear_jruby_gems do
  init_jruby_gems
  require 'rubygems/uninstaller'
  Gem.source_index.each do |spec|
    puts "spec[1].installation_path: #{spec[1].installation_path}"
    uninstaller = Gem::Uninstaller.new(spec[1], {:ignore => true, :executables => true})
    uninstaller.remove_all([spec[1]])
    uninstaller.remove_executables(spec[1])
  end
end