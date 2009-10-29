
desc "Creates a tag in git"
task :tag do
  puts "Creating tag in git"
  system "git tag -a -m '#{PKG_TAG}' #{PKG_TAG}"
  puts "Done!"
end

task :gem_jar => [:gem] do
  system "jruby -S gem install -i pkg/gems pkg/#{PKG_FILE_NAME}-java.gem"
  system "rm pkg/gems/gems/#{PKG_FILE_NAME}-java/lib/limelight.jar"
  system "rm -rf pkg/gems/gems/#{PKG_FILE_NAME}-java/productions"
  system "rm -rf pkg/gems/gems/#{PKG_FILE_NAME}-java/spec"
  system "rm -rf pkg/gems/gems/#{PKG_FILE_NAME}-java/bin/icons/limelight.icns"
  system "rm -rf pkg/gems/cache/#{PKG_FILE_NAME}-java.gem"
  system "jar cf pkg/limelight_gem.jar -C pkg/gems ."
end