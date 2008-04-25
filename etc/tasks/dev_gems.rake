def installed_gems
  gem_dir_pattern = /\W+(\d+\.?)*/
  installed_gems = []
  Dir.entries("etc/gems/gems").each { |entry| installed_gems << entry if entry.match(gem_dir_pattern)} if File.exists?("etc/gems/gems")
  return installed_gems
end

task :gems do
  puts "Establishing Gems"
  gem_pattern = /.*\.gem/
  all_gems = []
  Dir.entries("etc/gems/required_gems").each { |entry| all_gems << entry[0...-4] if entry.match(gem_pattern) }  
  gems_to_install = all_gems - installed_gems
  gems_to_install.each do |gem|
    puts "Installing gem: #{gem}"
    system "jruby/bin/gem install etc/gems/required_gems/#{gem}.gem -i etc/gems -f --no-rdoc --no-ri"
  end
end

task :clear_gems do
  require 'rubygems/uninstaller'
  Gem.source_index.each do |spec|
    puts "spec[1].installation_path: #{spec[1].installation_path}"
    uninstaller = Gem::Uninstaller.new(spec[1], {:ignore => true, :executables => true})
    uninstaller.remove_all([spec[1]])
    uninstaller.remove_executables(spec[1])
  end
end