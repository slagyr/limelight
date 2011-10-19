namespace "clojure" do

  def clojure_prod_deps
    ["file:/#{LIMELIGHT_ROOT}/limelight.jar"]
  end

  %w{classpath clean compile spec jar}.each do |name|
    desc "Delegates to clojure project"
    task name do
      in_dir("clojure") do
        run_command "lein #{name}"
      end
    end
  end

  desc "Install dependecies"
  task "deps" do
    in_dir("clojure") { run_command "lein deps" }
    deps(File.join(LIMELIGHT_ROOT, "clojure"), clojure_prod_deps, [])
  end

  desc "Init (noop)"
  task "init"

  desc "Alias for spec"
  task "test" => %w{spec}

  desc "Clean, compile, and test"
  task "build" => %w{deps compile spec}

end
