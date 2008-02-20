package limelight.ui.painting;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import limelight.ui.MockBlock;
import limelight.ui.InputLayout;

public class TextAreaPainterTest extends TestCase
{
  private limelight.ui.Panel panel;
  private TextAreaPainter painter;
  private MockBlock block;

  public void setUp() throws Exception
  {
    block = new MockBlock();
    panel = new limelight.ui.Panel(block);
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
    assertEquals(e, block.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, block.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, block.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    JTextArea area = (JTextArea)panel.getComponents()[0];
    assertEquals(4, area.getMouseListeners().length);
    MouseListener listener = area.getMouseListeners()[3];

    MouseEvent e = new MouseEvent(area, 1, 2, 3, 4, 5, 6, false);

    listener.mouseClicked(e);
    assertEquals(e, block.clickedMouse);

    listener.mouseEntered(e);
    assertEquals(e, block.enteredMouse);
    assertTrue(block.hooverOn);

    listener.mouseExited(e);
    assertEquals(e, block.exitedMouse);
    assertFalse(block.hooverOn);

    listener.mousePressed(e);
    assertEquals(e, block.pressedMouse);

    listener.mouseReleased(e);
    assertEquals(e, block.releasedMouse);
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
    assertEquals(e, block.gainedFocus);

    listener.focusLost(e);
    assertEquals(e, block.lostFocus);
  }
}
