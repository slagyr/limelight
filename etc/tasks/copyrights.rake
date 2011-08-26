namespace "copyright" do

  def load_copyrights()
    require 'mmcopyrights'
    IO.read(File.join(LIMELIGHT_ROOT, 'etc', 'copyright.txt'))
  end

  desc "add copyright headers to ruby source files"
  task "ruby" do
    copyright_text = load_copyrights()
    MM::Copyrights.process(File.join(LIMELIGHT_ROOT, 'ruby/lib'), "rb", "#-", copyright_text)
    MM::Copyrights.process(File.join(LIMELIGHT_ROOT, 'ruby/spec'), "rb", "#-", copyright_text)
  end

  desc "Add copyright headers to java source files"
  task "java" do
    copyright_text = load_copyrights()
    MM::Copyrights.process(File.join(LIMELIGHT_ROOT, 'src'), "java", "//-", copyright_text)
  end

  desc "Add copyright headers for all modules"
  task "all" => %w{ruby java}

end
