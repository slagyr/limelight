#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

header
menu do
  link :on_mouse_clicked => "scene.load('home')", :text => "Home"
  link :on_mouse_clicked => "scene.load('services')", :text => "Services"
  link :on_mouse_clicked => "scene.load('about')", :text => "About"
  link :text => "Blog"
  link :text => "Contact"
end