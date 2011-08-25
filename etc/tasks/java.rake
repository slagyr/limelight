namespace "java" do

  def classpath
    elems = Dir.glob("lib/**/*.jar")
    elems.unshift "classes"
    elems.join(":")
  end

  desc "Prints the calculated classpath"
  task "classpath" do
    puts classpath
  end

  desc "Compile the java source code"
  task "compile" do
    run_command 'ant compile'
  end

  desc "Run all JUnit tests"
  task "test" do
    pwd = Dir.getwd
    Dir.chdir File.join(pwd, "classes")
    test_class_paths = Dir.glob("**/*Test.class")
    Dir.chdir pwd

    test_class_names = test_class_paths.map {|name| name.gsub("/", ".").gsub(".class", "")}

    test_class_names = test_class_names.delete_if do |name|
      name =~ /limelight.builtin.productions.utilitieses/
    end

    File.open(".testClasses", "w") do |f|
      test_class_names.each { |name| f.puts name }
    end

    run_command "java -cp #{classpath} limelight.TestRunner"

    File.delete(".textClasses")
  end

end