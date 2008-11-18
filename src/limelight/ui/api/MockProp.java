//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ResourceLoader;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;
import limelight.ui.Panel;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MockProp implements Prop
{
  public final ScreenableStyle style;
  public Style hoverStyle;
  public String text;
  public String name;
  public Object pressedKey;
  public Object releasedKey;
  public Object typedKey;
  public Object clickedMouse;
  public Object enteredMouse;
  public boolean hooverOn;
  public Object exitedMouse;
  public Object releasedMouse;
  public Object pressedMouse;
  public Object draggedMouse;
  public Object movedMouse;
  public Object gainedFocus;
  public Object lostFocus;
  public Object pressedButton;
  public Object changedValue;
  public Scene scene;
  public ResourceLoader loader;
  public boolean accept_mouse_clicked = true;
  public boolean accept_mouse_pressed = true;
  public boolean accept_mouse_released = true;

  public MockProp()
  {
    style = new ScreenableStyle();
  }

  public MockProp(String name)
  {
    this();
    this.name = name;
  }

  public Panel getPanel()
  {
    return null;
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }

  public Style getHoverStyle()
  {
    return hoverStyle;
  }

  public String getName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public Scene getScene()
  {
    return scene;
  }

  public ResourceLoader getLoader()
  {
    return loader;
  }

  public boolean accepts_mouse_clicked()
  {
    return accept_mouse_clicked;
  }

  public boolean accepts_mouse_pressed()
  {
    return accept_mouse_pressed;
  }

  public boolean accepts_mouse_released()
  {
    return accept_mouse_released;
  }

  public void setText(String value)
  {
    text = value;
  }

  public void mouse_clicked(MouseEvent e)
  {
    clickedMouse = e;
  }

  public void hover_on()
  {
    hooverOn = true;
  }

  public void mouse_entered(MouseEvent e)
  {
    enteredMouse = e;
  }

  public void mouse_exited(MouseEvent e)
  {
    exitedMouse = e;
  }

  public void mouse_pressed(MouseEvent e)
  {
    pressedMouse = e;
  }

  public void mouse_released(MouseEvent e)
  {
    releasedMouse = e;
  }

  public void mouse_dragged(MouseEvent e)
  {
    draggedMouse = e;
  }

  public void mouse_moved(MouseEvent e)
  {
    movedMouse = e;
  }

  public void hover_off()
  {
    hooverOn = false;
  }

  public void key_typed(KeyEvent e)
  {
    typedKey = e;
  }

  public void key_pressed(KeyEvent e)
  {
    pressedKey = e;
  }

  public void key_released(KeyEvent e)
  {
    releasedKey = e;
  }

  public void focus_gained(FocusEvent e)
  {
    gainedFocus = e;
  }

  public void focus_lost(FocusEvent e)
  {
    lostFocus = e;
  }

  public void button_pressed(Object e)
  {
    pressedButton = e;
  }

  public void value_changed(Object e)
  {
    changedValue = e;
  }
}
