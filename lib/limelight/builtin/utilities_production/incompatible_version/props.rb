#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

title :text => "Incompatible Version"
label :text => "Production:"
bold_label :id => "production_name_label", :text => @production_name
label :text => "Minimum Required Version:"
bold_label :id => "required_version_label", :text => @required_version
label :text => "Current Limelight Version:"
bold_label :id => "current_version_label", :text => Limelight::VERSION::STRING
advise_text = <<END
To open this production, it is recommended that you install a newer version of Limelight. Proceeding to open this production with the current version may lead to unpredictable results.

Would you like to open this production anyway?
END
advise :text => advise_text
buttons do
  button :id => "cancel_button", :text => "Cancel", :players => "button", :on_button_pushed => "production.process_incompatible_version_response(false)"
  button :id => "proceed_button", :width => "150", :text => "Open Production", :players => "button", :on_button_pushed => "production.process_incompatible_version_response(true)"
end
