//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import limelight.ui.MockProp;
import limelight.ui.InputLayout;

public class TextAreaPainterTest extends TestCase
{
  private limelight.ui.Panel panel;
  private TextAreaPainter painter;
  private MockProp prop;

  public void setUp() throws Exception
  {
    prop = new MockProp();
    panel = new limelight.ui.Panel(prop);
    painter = new TextAreaPainter(panel);
    panel.getPainters().add(painter);
  }

  public void testThatATextAreaIsAddedToThePanel() throws Exception
  {
    assertEquals(1, panel.getComponents().length);
    assertEquals(JTextArea.class, panel.getComponents()[0].getClass());
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
    JTextArea area = (JTextArea)panel.getComponents()[0];
    assertEquals(1, area.getKeyListeners().length);
    KeyListener listener = area.getKeyListeners()[0];

    KeyEvent e = new KeyEvent(area, 1, 2, 3, 4, '5');

    listener.keyPressed(e);
    assertEquals(e, prop.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, prop.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, prop.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    JTextArea area = (JTextArea)panel.getComponents()[0];
    assertEquals(4, area.getMouseListeners().length);
    MouseListener listener = area.getMouseListeners()[3];

    MouseEvent e = new MouseEvent(area, 1, 2, 3, 4, 5, 6, false);

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
    panel.getTextAccessor().setText("blah");

    assertEquals("blah", painter.getTextArea().getText());

    painter.getTextArea().setText("hubbub");

    assertEquals("hubbub", panel.getTextAccessor().getText());
  }

  public void testFocusEvents() throws Exception
  {
    JTextArea area = (JTextArea)panel.getComponents()[0];
    assertEquals(4, area.getFocusListeners().length);
    FocusListener listener = area.getFocusListeners()[3];

    FocusEvent e = new FocusEvent(area, 1);

    listener.focusGained(e);
    assertEquals(e, prop.gainedFocus);

    listener.focusLost(e);
    assertEquals(e, prop.lostFocus);
  }
}
