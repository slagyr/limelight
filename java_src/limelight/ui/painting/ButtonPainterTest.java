package limelight.ui.painting;

import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import limelight.ui.MockProp;
import limelight.ui.InputLayout;

public class ButtonPainterTest extends TestCase
{
  private limelight.ui.Panel panel;
  private ButtonPainter painter;
  private MockProp prop;

  public void setUp() throws Exception
  {
    prop = new MockProp();
    panel = new limelight.ui.Panel(prop);
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
    assertEquals(e, prop.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, prop.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, prop.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
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

  public void testFocusEvents() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(2, field.getFocusListeners().length);
    FocusListener listener = field.getFocusListeners()[1];

    FocusEvent e = new FocusEvent(field, 1);

    listener.focusGained(e);
    assertEquals(e, prop.gainedFocus);

    listener.focusLost(e);
    assertEquals(e, prop.lostFocus);
  }

  public void testChangedStateEvent() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(2, field.getChangeListeners().length);
    ChangeListener listener = field.getChangeListeners()[0];

    ChangeEvent e = new ChangeEvent(field);

    listener.stateChanged(e);
    assertEquals(e, prop.changedState);
  }

  public void testButtonPressedEvent() throws Exception
  {
    JButton field = (JButton)panel.getComponents()[0];
    assertEquals(1, field.getActionListeners().length);
    ActionListener listener = field.getActionListeners()[0];

    ActionEvent e = new ActionEvent(field, 1, "blah");

    listener.actionPerformed(e);
    assertEquals(e, prop.pressedButton);
  }
  
  public void testText() throws Exception
  {
    panel.getTextAccessor().setText("blah");

    assertEquals("blah", painter.getButton().getText());

    painter.getButton().setText("hubbub");

    assertEquals("hubbub", panel.getTextAccessor().getText());
  }

}
