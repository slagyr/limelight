require 'rake/gempackagetask'

PKG_NAME = "limelight"
PKG_VERSION   = Limelight::VERSION::STRING
PKG_TAG = Limelight::VERSION::TAG
PKG_FILE_NAME = "#{PKG_NAME}-#{PKG_VERSION}"
PKG_FILES = FileList[
  'lib/init.rb',
  'lib/limelight/**/*',
  'lib/limelight.jar',
  'spec/**/*.rb',
  'productions/**/*',
  'bin/**/*'
]

spec = Gem::Specification.new do |s|
  s.name = PKG_NAME
  s.version = PKG_VERSION
  s.summary = Limelight::VERSION::DESCRIPTION
  s.description = "Limelight: A dynamic rich client framework and application platform."
  s.files = PKG_FILES.to_a
  s.require_path = 'lib'
  s.test_files = Dir.glob('spec/*_spec.rb')
  s.bindir = 'bin'
  s.executables = ['limelight']
  s.autorequire = 'init'
  s.platform = "java"
  s.author = "Micah Martin, 8th Light"
  s.email = "limelight@rubyforge.org"
  s.homepage = "http://limelight.8thlight.com"
  s.rubyforge_project = "limelight"
end

Rake::GemPackageTask.new(spec) do |pkg|
  pkg.need_zip = false
  pkg.need_tar = false
end

desc "Push the gem to server"
task :gem_deploy => [:gem] do
  system "gem push pkg/#{PKG_FILE_NAME}-java.gem"
end