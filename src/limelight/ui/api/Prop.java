//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.styles.Style;
import limelight.styles.ScreenableStyle;
import limelight.ui.Panel;
import limelight.ResourceLoader;

import java.awt.event.*;

public interface Prop
{
  Panel getPanel();
  ScreenableStyle getStyle();
  Style getHoverStyle();
  String getName();
  Scene getScene();
  ResourceLoader getLoader();

  boolean accepts_mouse_clicked();
  boolean accepts_mouse_pressed();
  boolean accepts_mouse_released();

  void hover_on();
  void hover_off();
  void mouse_clicked(MouseEvent e);
  void mouse_entered(MouseEvent e);
  void mouse_exited(MouseEvent e);
  void mouse_pressed(MouseEvent e);
  void mouse_released(MouseEvent e);
  void mouse_dragged(MouseEvent e);
  void mouse_moved(MouseEvent e);
  void key_typed(KeyEvent e);
  void key_pressed(KeyEvent e);
  void key_released(KeyEvent e);
  void focus_gained(FocusEvent e);
  void focus_lost(FocusEvent e);
  void button_pressed(Object e);
  void value_changed(Object e);
}
