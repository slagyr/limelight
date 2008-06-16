//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import limelight.ui.api.MockProp;
import limelight.ui.model.InputLayout;
import limelight.ui.model.Panel;

public class ComboBoxPainterTest extends TestCase
{
  private Panel panel;
  private ComboBoxPainter painter;
  private MockProp prop;

  public void setUp() throws Exception
  {
    prop = new MockProp();
    panel = new limelight.ui.model.Panel(prop);
    painter = new ComboBoxPainter(panel);
    panel.getPainters().add(painter);
  }

  public void testThatATextboxIsAddedToThePanel() throws Exception
  {
    assertEquals(1, panel.getComponents().length);
    assertEquals(JComboBox.class, panel.getComponents()[0].getClass());
  }

  public void testItsPanelIsSterilized() throws Exception
  {
    assertTrue(panel.isSterilized());
  }

  public void testThatTheLayoutIsSet() throws Exception
  {
    LayoutManager layout = panel.getLayout();
    assertTrue(layout.getClass() == InputLayout.class);
  }

  public void testEvents() throws Exception
  {
    JComboBox field = (JComboBox)panel.getComponents()[0];
    assertEquals(2, field.getKeyListeners().length);
    KeyListener listener = field.getKeyListeners()[1];

    KeyEvent e = new KeyEvent(field, 1, 2, 3, 4, '5');

    listener.keyPressed(e);
    assertEquals(e, prop.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, prop.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, prop.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    JComboBox field = (JComboBox)panel.getComponents()[0];
    assertEquals(2, field.getMouseListeners().length);
    MouseListener listener = field.getMouseListeners()[1];

    MouseEvent e = new MouseEvent(field, 1, 2, 3, 4, 5, 6, false);

    listener.mouseClicked(e);
    assertEquals(e, prop.clickedMouse);

    listener.mouseEntered(e);
    assertEquals(e, prop.enteredMouse);
    assertTrue(prop.hooverOn);

    listener.mouseExited(e);
    assertEquals(e, prop.exitedMouse);
    assertFalse(prop.hooverOn);

    listener.mousePressed(e);
    assertEquals(e, prop.pressedMouse);

    listener.mouseReleased(e);
    assertEquals(e, prop.releasedMouse);
  }

  public void testText() throws Exception
  {
    painter.getComboBox().addItem("Red");
    painter.getComboBox().addItem("Blue");

    panel.getTextAccessor().setText("Red");

    assertEquals("Red", painter.getComboBox().getSelectedItem());

    painter.getComboBox().setSelectedItem("Blue");

    assertEquals("Blue", panel.getTextAccessor().getText());
  }

  public void testFocusEvents() throws Exception
  {
    JComboBox field = (JComboBox)panel.getComponents()[0];
    assertEquals(2, field.getFocusListeners().length);
    FocusListener listener = field.getFocusListeners()[1];

    FocusEvent e = new FocusEvent(field, 1);

    listener.focusGained(e);
    assertEquals(e, prop.gainedFocus);

    listener.focusLost(e);
    assertEquals(e, prop.lostFocus);
  }

  public void testItemEvent() throws Exception
  {
    JComboBox field = (JComboBox)panel.getComponents()[0];
    assertEquals(3, field.getItemListeners().length);
    ItemListener listener = field.getItemListeners()[0];

    ItemEvent e = new ItemEvent(field, 1, "blah", 2);

    listener.itemStateChanged(e);
    assertEquals(e, prop.changedItemState); 
  }
}
