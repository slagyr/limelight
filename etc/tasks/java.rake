namespace "java" do

  def classpath
    elems = Dir.glob("#{LIMELIGHT_ROOT}/lib/**/*.jar")
    elems.unshift "#{LIMELIGHT_ROOT}/classes"
    elems.join(":")
  end

  def java_prod_deps
    ["http://download.java.net/maven/2/net/java/dev/jna/jna/3.0.9/jna-3.0.9.jar"]
  end

  def java_dev_deps
    ["https://github.com/downloads/KentBeck/junit/junit-4.8.2.jar"]
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
      FileUtils.mkdir("lib") if !File.exists?("lib")
      FileUtils.mkdir("lib/dev") if !File.exists?("lib/dev")
    end
  end

  desc "Install dependencies"
  task "deps" => %w{init} do
    deps(LIMELIGHT_ROOT, java_prod_deps, java_dev_deps)
  end

  namespace "compile" do

    desc "Compile limelight production source"
    task "src" => %w{init} do
      options = darwin? ? {} : {:excludes => ["src/limelight/os/darwin/**/*"]}
      javac(LIMELIGHT_ROOT, "src/**/*.java", classpath, options)
      FileUtils.cp_r "resources/.", "classes"
    end

    desc "Compile limelight test source"
    task "test" => %w{init} do
      options = darwin? ? {} : {:excludes => ["test/limelight/os/darwin/**/*"]}
      javac(LIMELIGHT_ROOT, "test/**/*.java", classpath, options)
    end

  end

  desc "Compile the java source code"
  task "compile" => ["compile:src", "compile:test"]

  desc "Run limelight JUnit tests"
  task "test" do
    options = darwin? ? {} : {:excludes => ["limelight/os/darwin/**/*"]}
    junit(LIMELIGHT_ROOT, classpath, options)
  end

  desc "Build a jar file with the limelight src"
  task "jar" => %w{compile utilities:deploy} do
    in_dir("classes") do
      with_tmp_file("../.manifest", ["Built-By: Micah Martin", "Main-Class: limelight.CmdLineMain", ""]) do
        run_command "jar cfm #{LIMELIGHT_ROOT}/limelight.jar ../.manifest *"
      end
    end
  end

  desc "Compiles, and tests the limelight java code"
  task "build" => %w{clean deps jar test}

end