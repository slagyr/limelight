namespace "clojure" do

  %w{deps classpath clean compile spec jar}.each do |name|
    desc "Delegates to clojure project"
    task name do
      in_dir("clojure") do
        run_command "lein #{name}"
      end
    end
  end

  desc "Init (noop)"
  task "init"

  desc "Alias for spec"
  task "test" => %w{spec}

  desc "Clean, compile, and test"
  task "build" => %w{compile spec}

end
