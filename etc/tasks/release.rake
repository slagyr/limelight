

task :release => [:verify_committed, :verify_user, :verify_password, :publish_packages, :tag, :publish_news]

desc "Verifies that there is no uncommitted code"
task :verify_committed do
  IO.popen('svn stat') do |io|
    io.each_line do |line|
      raise "\n!!! Do a svn commit first !!!\n\n" if line =~ /^\s*M\s*/
    end
  end
end

desc "Upload Website to RubyForge"
task :publish_website => [:verify_user, :verify_password] do
  require 'rake/contrib/rubyforgepublisher'
  publisher = Rake::SshDirPublisher.new(
    "#{ENV['RUBYFORGE_USER']}@rubyforge.org",
    "/var/www/gforge-projects/#{PKG_NAME}",
    "doc/rubyforge_site"
  )

  publisher.upload
end

desc "Creates a tag in svn"
task :tag do
  puts "Creating tag in git"
  system "git tag -a -m '#{PKG_TAG}' #{PKG_TAG}"
  puts "Done!"
end

task :verify_user do
  raise "RUBYFORGE_USER environment variable not set!" unless ENV['RUBYFORGE_USER']
end

task :verify_password do
  raise "RUBYFORGE_PASSWORD environment variable not set!" unless ENV['RUBYFORGE_PASSWORD']
end

desc "Publish gem+tgz+zip on RubyForge. You must make sure lib/version.rb is aligned with the CHANGELOG file"
task :publish_packages => [:verify_user, :verify_password, :package] do
  require 'meta_project'
  require 'rake/contrib/xforge'
  release_files = FileList[
    "pkg/#{PKG_FILE_NAME}-java.gem",
    "etc/installers/Limelight_windows#{PKG_TAG.gsub('REL', '')}.exe",
    "etc/installers/Limelight_macos#{PKG_TAG.gsub('REL', '')}.dmg"
  ]

  Rake::XForge::Release.new(MetaProject::Project::XForge::RubyForge.new(PKG_NAME)) do |xf|
    # Never hardcode user name and password in the Rakefile!
    xf.user_name = ENV['RUBYFORGE_USER']
    xf.password = ENV['RUBYFORGE_PASSWORD']
    xf.files = release_files.to_a
    xf.release_name = "limelight #{PKG_VERSION}"
  end
end