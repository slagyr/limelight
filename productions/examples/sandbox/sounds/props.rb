__ :name => "sandbox"
__install "header.rb"
arena do
  %w{ bird cat cow dog donkey duck }.each do |animal|
    clip :text => animal 
  end
end