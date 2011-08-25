require 'fileutils'

LIMELIGHT_ROOT = File.expand_path(File.dirname(__FILE__) + "/../../")

def run_command(command)
  system command
  exit_code = $?.exitstatus
  if exit_code != 0
    puts "Command failed with code #{exit_code}: #{command}"
  else
    puts "Command executed successfully: #{command}"
  end
end

def in_dir(path)
  pwd = Dir.getwd
  Dir.chdir path
  yield
ensure
  Dir.chdir pwd
end

def with_tmp_file(filename, lines)
  File.open(filename, "w") do |f|
    lines.each { |l| f.puts l }
  end
  yield
ensure
  File.delete(filename)
end

def javac(dir, glob, classpath)
  in_dir(dir) do
    src_files = Dir.glob(glob)
    puts "compiling #{src_files.size} files in #{dir}..."
    with_tmp_file(".javaFiles", src_files) do
      run_command "javac -cp #{classpath} -d classes @.javaFiles"
    end
  end
end

def junit(dir, classpath)
  test_files = in_dir(File.join(dir, "test")) { Dir.glob("**/*Test.java") }
  test_class_names = test_files.map { |name| name.gsub("/", ".").gsub(/.java\Z/, "") }

  puts "running #{test_files.size} test files in #{dir}..."
  with_tmp_file(".testClasses", test_class_names) do
    run_command "java -cp #{classpath} limelight.TestRunner"
  end
end

