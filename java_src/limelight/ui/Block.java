package limelight.ui;

import limelight.ui.Style;
import limelight.ui.Panel;
import limelight.ui.Page;

import javax.swing.event.ChangeEvent;
import java.awt.event.*;

public interface Block
{
  Panel getPanel();
  Style getStyle();
  String getClassName();
  String getText();
  Page getPage();
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
