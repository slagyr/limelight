//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.styles.Style;
import limelight.ui.Panel;

import javax.swing.event.ChangeEvent;
import java.awt.event.*;

public interface Prop
{
  Panel getPanel();
  Style getStyle();
  String getName();
  String getText();
  Scene getScene();
  void setText(String value);

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
  void state_changed(ChangeEvent e);
  void button_pressed(ActionEvent e);
  void item_state_changed(ItemEvent e);
}
