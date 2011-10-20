require 'fileutils'

LIMELIGHT_ROOT = File.expand_path(File.dirname(__FILE__) + "/../../")

def run_command(command)
  system command
  exit_code = $?.exitstatus
  if exit_code != 0
    raise "Command failed with code #{exit_code}: #{command}"
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

def darwin?
  RUBY_PLATFORM =~ /darwin/
end

def _apply_includes_excludes(options, src_files)
  if options[:excludes]
    options[:excludes].each do |exclude|
      src_files -= Dir.glob(exclude)
    end
  end
  if options[:includes]
    options[:includes].each do |include|
      src_files += Dir.glob(include)
    end
  end
  src_files
end

def javac(dir, glob, classpath, options={})
  in_dir(dir) do
    src_files = Dir.glob(glob)
    src_files = _apply_includes_excludes(options, src_files)
    puts "compiling #{src_files.size} files in #{dir}..."
    with_tmp_file(".javaFiles", src_files) do
      run_command "javac -cp #{classpath} -d classes @.javaFiles"
    end
  end
end

def junit(dir, classpath, options)
  test_files = in_dir(File.join(dir, "test")) do
    _apply_includes_excludes(options, Dir.glob("**/*Test.java"))
  end

  test_class_names = test_files.map { |name| name.gsub("/", ".").gsub(/.java\Z/, "") }

  puts "running #{test_files.size} test files in #{dir}..."
  with_tmp_file(".testClasses", test_class_names) do
    run_command "java -cp #{classpath} limelight.TestRunner"
  end
end

def fetch_dep(dep)
  case dep
    when /file\:\//
      FileUtils.cp dep[6..-1], "."
    when /http\:\/\//
      run_command "wget #{dep}"
    when /https\:\/\//
      run_command "wget #{dep}"
    else
      raise "Don't know how to install dependency: #{dep}"
  end
end

def install_deps(deps)
  deps.each do |dep|
    filename = File.basename(dep)
    if File.exists?(filename)
      puts "installed - #{filename}"
    else
      puts "fetching  - #{dep}"
      fetch_dep(dep)
    end
  end
end

def deps(dir, prod_deps, dev_deps)
  in_dir(File.join(dir, "lib")) { install_deps(prod_deps) }
  in_dir(File.join(dir, "lib", "dev")) { install_deps(dev_deps) }
end

