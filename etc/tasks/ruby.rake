namespace "ruby" do

  %w{init deps classpath clean compile spec test jar build}.each do |name|
    desc "Delegates to ruby project"
    task name do
      in_dir("ruby") do
        run_command "rake #{name}"
      end
    end
  end

end
