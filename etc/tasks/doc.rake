require 'rake/rdoctask'

WEB_ROOT = File.expand_path('~/Projects/slagyr.github.com/limelight/')

desc 'Generate RDoc'
rd = Rake::RDocTask.new do |rdoc|
  rdoc.rdoc_dir = "#{WEB_ROOT}/rdoc"
  rdoc.options << '--title' << 'Limelight' << '--line-numbers' << '--inline-source' << '--main' << 'README.rdoc'
  rdoc.rdoc_files.include('README.rdoc', 'CHANGES', 'lib/**/*.rb')
end
task :rdoc

desc "Generate the ruby for for styles"
task :gen_styles_rdoc do
  require 'lib/limelight.rb'
  require 'lib/limelight/string'
  src = "raise \"studio.rb is present for solely to document it's Java counterpart limelight.Studio.  This file should NOT be loaded in the Ruby runtime.\"\n\n"
  src << "module Limelight\n"
  src << "  module Styles\n"
  src << "    class Style\n"
  Java::limelight.styles.Style::STYLE_LIST.each do |descriptor|
    name = descriptor.name
    type = descriptor.compiler.type
    default_value = descriptor.defaultValue
    src << "      # Specifies the #{name} attribute of a prop.\n"
    src << "      #   type:           #{type}\n"
    src << "      #   default_value:  #{default_value}\n"
    src << "      attr_accessor :#{name.underscored.gsub(" ", "")}\n"
    src << "\n"
  end
  src << "    end\n"
  src << "  end\n"
  src << "end\n"
  File.open("#{$LIMELIGHT_LIB}/limelight/styles/style.rb", 'w') { |f| f.write src }
end
