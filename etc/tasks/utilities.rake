namespace "utilities" do

  UTILITIES_ROOT = "#{LIMELIGHT_ROOT}/productions/utilities"

  def classpath
    elems = Dir.glob("#{LIMELIGHT_ROOT}/lib/**/*.jar")
    elems.unshift "#{LIMELIGHT_ROOT}/classes"
    elems.unshift "#{UTILITIES_ROOT}/classes"
    elems.join(":")
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
  task "compile" => ["compile:src", "compile:test"]

  desc "Deployes the Utilities production into compiled Limelight code"
  task "deploy" do
    deploy_root = File.join(LIMELIGHT_ROOT, "classes/limelight/builtin/productions")
    FileUtils.mkdir_p(deploy_root)
    FileUtils.cp_r(UTILITIES_ROOT, deploy_root)
    FileUtils.rm_rf(File.join(deploy_root, "utilities/src"))
    FileUtils.rm_rf(File.join(deploy_root, "utilities/test"))
  end

  desc "Run Utilities JUnit tests"
  task "test" do
    test_files = in_dir(File.join(UTILITIES_ROOT, "test")) { Dir.glob("**/*Test.java") }
    test_class_names = test_files.map { |name| name.gsub("/", ".").gsub(".java", "") }

    with_tmp_file(".testClasses", test_class_names) do
      run_command "java -cp #{classpath} limelight.TestRunner"
    end
  end

  desc "Compiles, deployes, and tests the Utilities production"
  task "build" => ["compile", "deploy", "test"]

end