//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.MockPanel;
import limelight.ui.events.panel.FocusGainedEvent;
import limelight.ui.events.panel.FocusLostEvent;
import limelight.ui.events.panel.CharTypedEvent;
import limelight.ui.events.panel.KeyPressedEvent;
import limelight.ui.events.panel.KeyReleasedEvent;
import limelight.ui.model.inputs.MockEventAction;
import limelight.ui.model.inputs.TestableInputPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class StageKeyListenerTest
{
  private StageKeyListener listener;
  private FakeScene root;
  private MockParentPanel panel;
  private Component component;
  private TestableInputPanel input;
  private TestableInputPanel input2;
  private TestableInputPanel input3;
  private MockEventAction action;
  private MockEventAction action2;

  @Before
  public void setUp() throws Exception
  {
    root = new FakeScene();
    listener = new StageKeyListener(root);
    panel = new MockParentPanel();
    root.add(panel);
    component = new Panel();
    action = new MockEventAction();
    action2 = new MockEventAction();
  }

  @Test
  public void focusOnFrameByDefault() throws Exception
  {
    assertEquals(root, listener.getFocusedPanel());
  }

  @Test
  public void changeFocus() throws Exception
  {
    panel.getEventHandler().add(FocusGainedEvent.class, action);

    listener.focusOn(panel);

    assertEquals(panel, listener.getFocusedPanel());
    assertEquals(true, action.invoked);
  }

  @Test
  public void focusIsLostWhenChangingFocus() throws Exception
  {
    panel.getEventHandler().add(FocusLostEvent.class, action);
    MockPanel panel2 = new MockPanel();
    panel2.getEventHandler().add(FocusGainedEvent.class, action2);

    listener.focusOn(panel);
    listener.focusOn(panel2);

    assertEquals(panel2, listener.getFocusedPanel());
    assertEquals(true, action.invoked);
    assertEquals(true, action2.invoked);
  }

  @Test
  public void focusingOnNullDoesntChangeFocus() throws Exception
  {
    listener.focusOn(panel);
    panel.getEventHandler().add(FocusLostEvent.class, action);
    listener.focusOn(null);

    assertEquals(panel, listener.getFocusedPanel());
    assertEquals(false, action.invoked);
  }

  @Test
  public void focuedPanelReceivesTypedKeyEvents() throws Exception
  {
    panel.getEventHandler().add(CharTypedEvent.class, action);
    listener.focusOn(panel);

    final KeyEvent typedEvent = new KeyEvent(component, 1, 2, 3, 4, 'a');
    listener.keyTyped(typedEvent);
    assertEquals(panel, action.recipient);
    assertEquals('a', ((CharTypedEvent)action.event).getChar());
  }

  @Test
  public void focuedPanelReceivesPressKeyEvents() throws Exception
  {
    panel.getEventHandler().add(KeyPressedEvent.class, action);
    listener.focusOn(panel);

    final KeyEvent pressedEvent = new KeyEvent(component, 1, 2, 3, KeyEvent.VK_A, 'b');
    listener.keyPressed(pressedEvent);
    KeyPressedEvent keyEvent = (KeyPressedEvent) action.event;
    assertSame(panel, keyEvent.getSource());
    assertEquals(limelight.ui.events.panel.KeyEvent.KEY_A, keyEvent.getKeyCode());
  }

  @Test
  public void focuedPanelReceivesReleaseKeyEvents() throws Exception
  {
    panel.getEventHandler().add(KeyReleasedEvent.class, action);
    listener.focusOn(panel);

    final KeyEvent releasedEvent = new KeyEvent(component, 1, 2, 3, KeyEvent.VK_B, 'c');
    listener.keyReleased(releasedEvent);
    KeyReleasedEvent keyEvent1 = (KeyReleasedEvent) action.event;
    assertSame(panel, keyEvent1.getSource());
    assertEquals(limelight.ui.events.panel.KeyEvent.KEY_B, keyEvent1.getKeyCode());
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
    assertSame(input2, StageKeyListener.nextInputPanel(input));
    assertSame(input3, StageKeyListener.nextInputPanel(input2));
    assertSame(input, StageKeyListener.nextInputPanel(input3));
  }

  @Test
  public void findPreviousInput() throws Exception
  {
    buildInputTree();
    assertSame(input, StageKeyListener.previousInputPanel(input2));
    assertSame(input2, StageKeyListener.previousInputPanel(input3));
    assertSame(input3, StageKeyListener.previousInputPanel(input));
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
