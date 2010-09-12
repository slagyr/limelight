package limelight.ui.model;

import limelight.ui.MockPanel;
import limelight.ui.events.FocusGainedEvent;
import limelight.ui.events.FocusLostEvent;
import limelight.ui.model.inputs.TestableInputPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class RootKeyListenerTest
{
  private RootKeyListener listener;
  private MockRootPanel root;
  private MockParentPanel panel;
  private Component component;
  private TestableInputPanel input;
  private TestableInputPanel input2;
  private TestableInputPanel input3;
  private MockEventHandler panelEvents;

  @Before
  public void setUp() throws Exception
  {
    root = new MockRootPanel();
    listener = new RootKeyListener(root);
    panel = new MockParentPanel();
    panelEvents = panel.mockEventHandler;
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
    assertEquals(FocusGainedEvent.class, panelEvents.last().getClass());
  }
  
  @Test
  public void focusIsLostWhenChangingFocus() throws Exception
  {
    MockPanel panel2 = new MockPanel();

    listener.focusOn(panel);
    listener.focusOn(panel2);

    assertEquals(panel2, listener.getFocusedPanel());
    assertEquals(FocusLostEvent.class, panelEvents.last().getClass());
    assertEquals(FocusGainedEvent.class, panel2.mockEventHandler.last().getClass());
  }
  
  @Test
  public void focusingOnNullDoesntChangeFocus() throws Exception
  {
    listener.focusOn(panel);
    listener.focusOn(null);

    assertEquals(panel, listener.getFocusedPanel());
    assertEquals(FocusGainedEvent.class, panelEvents.last().getClass());
  }
  
  @Test
  public void focuedPanelReceivesTypedKeyEvents() throws Exception
  {
    listener.focusOn(panel);

    final KeyEvent typedEvent = new KeyEvent(component, 1, 2, 3, 4, 'a');
    listener.keyTyped(typedEvent);
    limelight.ui.events.CharTypedEvent charEvent = (limelight.ui.events.CharTypedEvent) panelEvents.last();
    assertEquals(panel, charEvent.getSource());
    assertEquals('a', charEvent.getChar());
  }

  @Test
  public void focuedPanelReceivesPressKeyEvents() throws Exception
  {
    listener.focusOn(panel);

    final KeyEvent pressedEvent = new KeyEvent(component, 1, 2, 3, KeyEvent.VK_A, 'b');
    listener.keyPressed(pressedEvent);
    limelight.ui.events.KeyPressedEvent keyEvent = (limelight.ui.events.KeyPressedEvent) panelEvents.last();
    assertSame(panel, keyEvent.getSource());
    assertEquals(limelight.ui.events.KeyEvent.KEY_A, keyEvent.getKeyCode());
  }
  
  @Test
  public void focuedPanelReceivesReleaseKeyEvents() throws Exception
  {
    listener.focusOn(panel);

    final KeyEvent releasedEvent = new KeyEvent(component, 1, 2, 3, KeyEvent.VK_B, 'c');
    listener.keyReleased(releasedEvent);
    limelight.ui.events.KeyReleasedEvent keyEvent1 = (limelight.ui.events.KeyReleasedEvent) panelEvents.last();
    assertSame(panel, keyEvent1.getSource());
    assertEquals(limelight.ui.events.KeyEvent.KEY_B, keyEvent1.getKeyCode());
  }

  private void buildInputTree()
  {
    MockParentPanel parent = new MockParentPanel();
    panel.add(parent);
    input = new TestableInputPanel();
    parent.add(input);
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();
    panel.add(input2);
    panel.add(input3);
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
