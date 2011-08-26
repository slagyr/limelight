#namespace "ruby" do
#
#  RUBY_ROOT = File.join(LIMELIGHT_ROOT, "ruby")
#
#  def classpath
#    elems = Dir.glob("#{RUBY_ROOT}/lib/**/*.jar")
#    elems.unshift "#{LIMELIGHT_ROOT}/classes"
#    elems.unshift "#{RUBY_ROOT}/classes"
#    elems.join(":")
#  end
#
#  desc "Prints the calculated classpath"
#  task "classpath" do
#    puts classpath
#  end
#
#  desc "Cleans classes and generated build files"
#  task "clean" do
#    in_dir(RUBY_ROOT) do
#      FileUtils.rm_rf "classes"
#    end
#  end
#
#  desc "Creates required files"
#  task "init" do
#    in_dir(RUBY_ROOT) do
#      FileUtils.mkdir("classes") if !File.exists?("classes")
#    end
#  end
#
#  desc "Compile the java source code"
#  task "compile" do
#    javac(RUBY_ROOT, "src/**/*.java", classpath)
#  end
#
#  desc "Run ruby specs"
#  task "spec" do
#    ???
#  end
#
#  desc "Compiles, and tests the limelight java code"
#  task "build" => ["clean", "compile", "test"]
#
#  desc "Build a jar file with the limelight src"
#  task "jar" do #=> %w{clean compile:src utilities:deploy} do
#    in_dir("classes") do
#      with_tmp_file("../.manifest", ["Built-By: Micah Martin", "Main-Class: limelight.CmdLineMain", ""]) do
#        run_command "jar -cfm #{LIMELIGHT_ROOT}/limelight.jar ../.manifest *"
#      end
#    end
#  end
#
#
#end