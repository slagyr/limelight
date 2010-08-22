package limelight.ui.model;

import limelight.ui.MockPanel;
import limelight.ui.model.inputs.InputPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class KeyListenerTest
{
  private RootKeyListener listener;
  private MockRootPanel root;
  private MockPanel panel;
  private Component component;
  private TestableInputPanel input;
  private TestableInputPanel input2;
  private TestableInputPanel input3;

  @Before
  public void setUp() throws Exception
  {
    root = new MockRootPanel();
    listener = new RootKeyListener(root);
    panel = new MockPanel();
    root.add(panel);
    component = new Panel();
  }

  @Test
  public void focusOnFrameByDefault() throws Exception
  {
    assertEquals(root, listener.getFocusedPanel());
  }
  
  @Test
  public void changeFocus() throws Exception
  {
    listener.focusOn(panel);

    assertEquals(panel, listener.getFocusedPanel());
    assertEquals(true, panel.hasFocus);
  }
  
  @Test
  public void focusIsLostWhenChangingFocus() throws Exception
  {
    MockPanel panel2 = new MockPanel();

    listener.focusOn(panel);
    listener.focusOn(panel2);

    assertEquals(panel2, listener.getFocusedPanel());
    assertEquals(false, panel.hasFocus);
    assertEquals(true, panel2.hasFocus);
  }
  
  @Test
  public void focusingOnNullDoesntChangeFocus() throws Exception
  {
    listener.focusOn(panel);
    listener.focusOn(null);

    assertEquals(panel, listener.getFocusedPanel());
    assertEquals(true, panel.hasFocus);
  }
  
  @Test
  public void focuedPanelReceivesAllTheKeyEvents() throws Exception
  {
    listener.focusOn(panel);

    final KeyEvent typedEvent = new KeyEvent(component, 1, 2, 3, 4, 'a');
    listener.keyTyped(typedEvent);
    assertEquals(typedEvent, panel.typedKeyEvent);

    final KeyEvent pressedEvent = new KeyEvent(component, 1, 2, 3, 4, 'b');
    listener.keyPressed(pressedEvent);
    assertEquals(pressedEvent, panel.pressedKeyEvent);

    final KeyEvent releasedEvent = new KeyEvent(component, 1, 2, 3, 4, 'c');
    listener.keyReleased(releasedEvent);
    assertEquals(releasedEvent, panel.releasedKeyEvent);
  }

  private void buildInputTree()
  {
    MockPanel parent = new MockPanel();
    panel.add(parent);
    input = new TestableInputPanel();
    parent.add(input);
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();
    panel.add(input2);
    panel.add(input3);
  }

  private static class TestableInputPanel extends MockPanel implements InputPanel
  {
  }

  @Test
  public void findNextInput() throws Exception
  {
    buildInputTree();
    assertSame(input2, RootKeyListener.nextInputPanel(input));
    assertSame(input3, RootKeyListener.nextInputPanel(input2));
    assertSame(input, RootKeyListener.nextInputPanel(input3));
  }

  @Test
  public void findPreviousInput() throws Exception
  {
    buildInputTree();
    assertSame(input, RootKeyListener.previousInputPanel(input2));
    assertSame(input2, RootKeyListener.previousInputPanel(input3));
    assertSame(input3, RootKeyListener.previousInputPanel(input));
  }

  @Test
  public void walkingForwardThroughInputs() throws Exception
  {
    buildInputTree();
    listener.focusOnNextInput();
    assertEquals(input, listener.getFocusedPanel());

    listener.focusOnNextInput();
    assertEquals(input2, listener.getFocusedPanel());

    listener.focusOnNextInput();
    assertEquals(input3, listener.getFocusedPanel());

    listener.focusOnNextInput();
    assertEquals(input, listener.getFocusedPanel());
  }

  @Test
  public void walkingBackwardsThroughInputs() throws Exception
  {
    buildInputTree();
    listener.focusOnPreviousInput();
    assertEquals(input3, listener.getFocusedPanel());

    listener.focusOnPreviousInput();
    assertEquals(input2, listener.getFocusedPanel());

    listener.focusOnPreviousInput();
    assertEquals(input, listener.getFocusedPanel());

    listener.focusOnPreviousInput();
    assertEquals(input3, listener.getFocusedPanel());
  }
}
