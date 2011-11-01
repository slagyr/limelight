//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.*;
import limelight.ui.model.FakeScene;
import limelight.ui.model.MockStage;
import limelight.ui.model.PropPanel;
import limelight.ui.text.TextLocation;
import limelight.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.junit.Assume.assumeTrue;

public class TextInputPanelTest
{
  private TextInputPanel panel;
  private FakeScene root;
  private PropPanel parent;
  private TextModel model;
  private MockStage stage;

  @Before
  public void setUp()
  {
    assumeTrue(TestUtil.notHeadless());
    root = new FakeScene();
    panel = new MockTextInputPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);
    root.add(parent);
    stage = new MockStage();
    root.setStage(stage);
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

    stage.getKeyListener().focusOn(panel);

    assertEquals(true, panel.hasFocus());
    assertEquals(true, panel.isCaretBlinking());
    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBounds()));
  }

  @Test
  public void canLoseFocus()
  {
    stage.getKeyListener().focusOn(panel);
    root.dirtyRegions.clear();
    stage.getKeyListener().focusOn(root);

    assertEquals(false, panel.hasFocus());
    assertEquals(false, panel.isCaretBlinking());
    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBounds()));
  }

  @Test
  public void shouldRequireLayoutAfterConsumableSizeChanges() throws Exception
  {
    FakeScene root = new FakeScene();
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

    new CharTypedEvent(0, 'Z').dispatch(panel);

    assertEquals("ZSome Text", model.getText());
  }

  @Test
  public void typedCharsWithCommandModifierDoNothing() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);

    new CharTypedEvent(KeyEvent.COMMAND_MASK, 'A').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void typedCharsWithControlModifierDoNothing() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);

    new CharTypedEvent(KeyEvent.CONTROL_MASK, 'A').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void typingACharMakesThePanelDirty() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(0, 'Z').dispatch(panel);

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void backspaceIsNotTyped() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(0, '\b').dispatch(panel);

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void newlineIsTyped() throws Exception
  {
    model.setCaretLocation(TextLocation.origin);
    new CharTypedEvent(0, '\n').dispatch(panel);

    assertEquals("\nSome Text", model.getText());
  }

  @Test
  public void consumedMousePressEventsDoNothing() throws Exception
  {
    new MousePressedEvent(0, new Point(0, 0), 3).consumed().dispatch(panel);

    assertEquals(false, model.hasSelection());
  }

  @Test
  public void consumedMouseDragEventsDoNothing() throws Exception
  {
    new MousePressedEvent(0, new Point(0, 0), 1).dispatch(panel);
    new MouseDraggedEvent(0, new Point(25, 5), 1).consumed().dispatch(panel);

    assertEquals(false, model.hasSelection());
  }

  @Test
  public void consumedFocusGainedEventsShouldNotStartTheCaret() throws Exception
  {
    assertEquals(false, panel.isCaretBlinking());

    new FocusGainedEvent().consumed().dispatch(panel);

    assertEquals(false, panel.isCaretBlinking());
  }

  @Test
  public void consumedFocusLostEventsShouldNotStopTheCaret() throws Exception
  {
    new FocusGainedEvent().dispatch(panel);
    new FocusLostEvent().consumed().dispatch(panel);

    assertEquals(true, panel.isCaretBlinking());
  }

  @Test
  public void consumedKeyPressEventsDoNothing() throws Exception
  {
    panel = new TextBoxPanel();
    root.add(panel);
    panel.getModel().setText("Some Text");
    new KeyPressedEvent(0, KeyEvent.KEY_BACK_SPACE, 0).consumed().dispatch(panel);

    assertEquals("Some Text", panel.getText());
  }

  @Test
  public void consumedCharTypedEventsDoNothing() throws Exception
  {
    panel = new TextBoxPanel();
    root.add(panel);
    panel.getModel().setText("Some Text");
    new CharTypedEvent(0, 'A').consumed().dispatch(panel);

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

    new FocusGainedEvent().dispatch(panel);
    model.insertChar('a');
    assertEquals(true, model.hasChanged());
    new FocusLostEvent().dispatch(panel);

    assertEquals(true, action.invoked);
  }

  @Test
  public void caretAnimationIsStoppedWhenPanelIsDisowned() throws Exception
  {
    stage.getKeyListener().focusOn(panel);
    assertEquals(true, panel.isCaretBlinking());

    panel.setParent(null);

    assertEquals(false, panel.isCaretBlinking());
  }

}
