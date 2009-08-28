#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

# This file (props.rb) defines all the props that appear in a scene when loaded.  It makes use of the
# PropBuilder DSL.
#
# For more information see: http://limelightwiki.8thlight.com/index.php/A_Cook%27s_Tour_of_Limelight#PropBuilder_DSL

root :text => "This production is forever incompatible with the current version of Limelight (for testing purposes)."
close_button :id => "close_button", :text => "Close this Scene", :on_mouse_clicked => "scene.stage.close"