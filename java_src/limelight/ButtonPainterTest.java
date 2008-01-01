package limelight;

import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class ButtonPainterTest extends TestCase
{
  private Panel panel;
  private ButtonPainter painter;
  private MockBlock block;

  public void setUp() throws Exception
  {
    block = new MockBlock();
    panel = new Panel(block);
    painter = new ButtonPainter(panel);
    panel.getPainters().add(painter);
  }

  public void testThatACheckboxIsAddedToThePanel() throws Exception
  {
    assertEquals(1, panel.getComponents().length);
    assertEquals(JButton.class, panel.getComponents()[0].getClass());
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
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(1, field.getKeyListeners().length);
    KeyListener listener = field.getKeyListeners()[0];

    KeyEvent e = new KeyEvent(field, 1, 2, 3, 4, '5');

    listener.keyPressed(e);
    assertEquals(e, block.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, block.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, block.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(2, field.getMouseListeners().length);
    MouseListener listener = field.getMouseListeners()[1];

    MouseEvent e = new MouseEvent(field, 1, 2, 3, 4, 5, 6, false);

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

  public void testFocusEvents() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(2, field.getFocusListeners().length);
    FocusListener listener = field.getFocusListeners()[1];

    FocusEvent e = new FocusEvent(field, 1);

    listener.focusGained(e);
    assertEquals(e, block.gainedFocus);

    listener.focusLost(e);
    assertEquals(e, block.lostFocus);
  }

  public void testChangedStateEvent() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(2, field.getChangeListeners().length);
    ChangeListener listener = field.getChangeListeners()[0];

    ChangeEvent e = new ChangeEvent(field);

    listener.stateChanged(e);
    assertEquals(e, block.changedState);
  }

  public void testButtonPressedEvent() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(1, field.getActionListeners().length);
    ActionListener listener = field.getActionListeners()[0];

    ActionEvent e = new ActionEvent(field, 1, "blah");

    listener.actionPerformed(e);
    assertEquals(e, block.pressedButton);
  }
  
  public void testText() throws Exception
  {
    panel.getTextAccessor().setText("blah");

    assertEquals("blah", painter.getButton().getText());

    painter.getButton().setText("hubbub");

    assertEquals("hubbub", panel.getTextAccessor().getText());
  }

}
