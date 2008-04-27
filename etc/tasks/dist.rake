DIST_DIR = 'etc/dist'
require 'fileutils'

task :prep_dist => :jar do
  puts "Preparing Distribution"
  if File.exists?(DIST_DIR)
    puts "\tdeleting #{DIST_DIR}"
    FileUtils.rm_r(DIST_DIR, :force => true)
  end
  puts "\tcreating #{DIST_DIR}"
  FileUtils.mkdir(DIST_DIR)
  dist_add('lib', 'jruby', 'bin', 'productions', 'license.txt')
  dist_delete("**/.svn", "**/junit*.jar")
end

def dist_delete(*globs)
  FileUtils.cd(DIST_DIR) do |dir|
    globs.each do |glob|
      puts "\tdeleting #{glob}"
      Dir.glob(glob).each do |file|
        FileUtils.rm_r(file)
      end
    end
  end
end

def dist_add(*paths)
  paths.each do |path|
    puts "\tadding #{path}"
    FileUtils.cp_r(path, DIST_DIR)
  end
end