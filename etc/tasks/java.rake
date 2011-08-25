namespace "java" do

  def classpath
    elems = Dir.glob("#{LIMELIGHT_ROOT}/lib/**/*.jar")
    elems.unshift "#{LIMELIGHT_ROOT}/classes"
    elems.join(":")
  end

  desc "Prints the calculated classpath"
  task "classpath" do
    puts classpath
  end

  desc "Cleans classes and generated build files"
  task "clean" do
    in_dir(LIMELIGHT_ROOT) do
      FileUtils.rm_rf "classes"
    end
  end

  desc "Creates required files"
  task "init" do
    in_dir(LIMELIGHT_ROOT) do
      FileUtils.mkdir("classes") if !File.exists?("classes")
    end
  end

  namespace "compile" do

    desc "Compile limelight production source"
    task "src" => ["init"] do
      javac(LIMELIGHT_ROOT, "src/**/*.java", classpath)
    end

    desc "Compile limelight test source"
    task "test" => ["init"] do
      javac(LIMELIGHT_ROOT, "test/**/*.java", classpath)
    end

  end

  desc "Compile the java source code"
  task "compile" => ["compile:src", "compile:test"]

  desc "Run limelight JUnit tests"
  task "test" do
    junit(LIMELIGHT_ROOT, classpath)
  end

  desc "Compiles, and tests the limelight java code"
  task "build" => ["clean", "compile", "test"]

end