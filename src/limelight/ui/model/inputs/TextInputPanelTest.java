//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.api.MockProp;
import limelight.ui.events.*;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class TextInputPanelTest
{
  private TextInputPanel panel;
  private MockRootPanel root;
  private PropPanel parent;
  private TextModel model;

  @Before
  public void setUp()
  {
    root = new MockRootPanel();
    panel = new MockTextInputPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    root.add(parent);
    model = panel.getModel();
    model.setText("Some Text");
  }

  @Test
  public void shouldDefaultLayout() throws Exception
  {
    assertSame(TextInputPanelLayout.instance, panel.getDefaultLayout());
  }

  @Test
  public void canGainFocus()
  {
    assertEquals(0, root.dirtyRegions.size());

    root.getKeyListener().focusOn(panel);

    assertEquals(true, panel.hasFocus());
    assertEquals(true, panel.isCaretBlinking());
    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBounds()));
  }

  @Test
  public void canLoseFocus()
  {
    root.getKeyListener().focusOn(panel);
    root.dirtyRegions.clear();
    root.getKeyListener().focusOn(root);

    assertEquals(false, panel.hasFocus());
    assertEquals(false, panel.isCaretBlinking());
    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBounds()));
  }
  
  @Test
  public void shouldRequireLayoutAfterConsumableSizeChanges() throws Exception
  {
    MockRootPanel root = new MockRootPanel();
    root.add(panel);
    panel.getRoot();
    panel.resetLayout();

    panel.consumableAreaChanged();
    
    assertEquals(true, panel.needsLayout());
  }
  
  @Test
  public void typingACharWillInsertIt() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);

    new CharTypedEvent(panel, 0, 'Z').dispatch(panel);

    assertEquals("ZSome Text", model.getText());
  }

  @Test
  public void typedCharsWithCommandModifierDoNothing() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);

    new CharTypedEvent(panel, KeyEvent.COMMAND_MASK, 'A').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }
  
  @Test
  public void typedCharsWithControlModifierDoNothing() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);

    new CharTypedEvent(panel, KeyEvent.CONTROL_MASK, 'A').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void typingACharMakesThePanelDirty() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(panel, 0, 'Z').dispatch(panel);

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }
  
  @Test
  public void backspaceIsNotTyped() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(panel, 0, '\b').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void newlineIsTyped() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(panel, 0, '\n').dispatch(panel);

    assertEquals("\nSome Text", model.getText());
  }

  @Test
  public void consumedMousePressEventsDoNothing() throws Exception
  {
    new MousePressedEvent(panel, 0, new Point(0, 0), 3).consumed().dispatch(panel);
    
    assertEquals(false, model.hasSelection());
  }
  
  @Test
  public void consumedMouseDragEventsDoNothing() throws Exception
  {
    new MousePressedEvent(panel, 0, new Point(0, 0), 1).dispatch(panel);
    new MouseDraggedEvent(panel, 0, new Point(25, 5), 1).consumed().dispatch(panel);

    assertEquals(false, model.hasSelection());
  }

  @Test
  public void consumedFocusGainedEventsShouldNotStartTheCaret() throws Exception
  {
    assertEquals(false, panel.isCaretBlinking());
    
    new FocusGainedEvent(panel).consumed().dispatch(panel);

    assertEquals(false, panel.isCaretBlinking());
  }
  
  @Test
  public void consumedFocusLostEventsShouldNotStopTheCaret() throws Exception
  {
    new FocusGainedEvent(panel).dispatch(panel);
    new FocusLostEvent(panel).consumed().dispatch(panel);

    assertEquals(true, panel.isCaretBlinking());
  }

  @Test
  public void consumedKeyPressEventsDoNothing() throws Exception
  {
    panel = new TextBoxPanel();
    root.add(panel);
    panel.getModel().setText("Some Text");
    new KeyPressedEvent(panel, 0, KeyEvent.KEY_BACK_SPACE, 0).consumed().dispatch(panel);

    assertEquals("Some Text", panel.getText());
  }

  @Test
  public void consumedCharTypedEventsDoNothing() throws Exception
  {
    panel = new TextBoxPanel();
    root.add(panel);
    panel.getModel().setText("Some Text");
    new CharTypedEvent(panel, 0, 'A').consumed().dispatch(panel);

    assertEquals("Some Text", panel.getText());
  }

  @Test
  public void valuChangedEventInvokedWhenChangingText() throws Exception
  {
    panel.setText("foo");
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    panel.setText("foo");
    assertEquals(false, action.invoked);

    panel.setText("bar");
    assertEquals(true, action.invoked);
  }
  
  @Test
  public void changesToModelAreReportedOnFocusLost() throws Exception
  {
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    new FocusGainedEvent(panel).dispatch(panel);
    model.insertChar('a');
    assertEquals(true, model.hasChanged());
    new FocusLostEvent(panel).dispatch(panel);

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void caretAnimationIsStoppedWhenPanelIsDisowned() throws Exception
  {
    root.getKeyListener().focusOn(panel);
    assertEquals(true, panel.isCaretBlinking());

    panel.setParent(null);

    assertEquals(false, panel.isCaretBlinking());
  }

}
