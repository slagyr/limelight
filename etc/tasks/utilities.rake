namespace "utilities" do

  UTILITIES_ROOT = "#{LIMELIGHT_ROOT}/productions/utilities"

  def classpath
    elems = Dir.glob("#{LIMELIGHT_ROOT}/lib/**/*.jar")
    elems.unshift "#{LIMELIGHT_ROOT}/classes"
    elems.unshift "#{UTILITIES_ROOT}/classes"
    elems.join(path_separator)
  end

  desc "Prints the calculated classpath"
  task "classpath" do
    puts classpath
  end

  desc "Cleans classes and generated build files"
  task "clean" do
    in_dir(UTILITIES_ROOT) do
      FileUtils.rm_rf "classes"
    end
  end

  desc "Creates required files"
  task "init" do
    in_dir(UTILITIES_ROOT) do
      FileUtils.mkdir("classes") if !File.exists?("classes")
    end
  end

  desc "Install dependencies"
  task "deps"

  namespace "compile" do

    desc "Compile Utilities production source"
    task "src" => ["init"] do
      javac(UTILITIES_ROOT, "src/**/*.java", classpath)
    end

    desc "Compile Utilities test source"
    task "test" => ["init"] do
      javac(UTILITIES_ROOT, "test/**/*.java", classpath)
    end

  end

  desc "Compile the java source code"
  task "compile" => %w{compile:src compile:test}

  desc "Deployes the Utilities production into compiled Limelight code"
  task "deploy" => %w{clean compile:src} do
    deploy_root = File.join(LIMELIGHT_ROOT, "classes/limelight/builtin/productions")
    FileUtils.mkdir_p(deploy_root)
    FileUtils.cp_r(UTILITIES_ROOT, deploy_root)
    FileUtils.rm_rf(File.join(deploy_root, "utilities/src"))
    FileUtils.rm_rf(File.join(deploy_root, "utilities/test"))
  end

  desc "Run Utilities JUnit tests"
  task "test" do
    junit(UTILITIES_ROOT, classpath)
  end

  desc "Compiles, deployes, and tests the Utilities production"
  task "build" => %w{deploy compile:test test}

end