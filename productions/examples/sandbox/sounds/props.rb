#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  %w{ bird cat cow dog donkey duck }.each do |animal|
    clip :text => animal 
  end
end