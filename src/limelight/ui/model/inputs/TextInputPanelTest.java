//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.api.MockProp;
import limelight.ui.events.CharTypedEvent;
import limelight.ui.events.KeyEvent;
import limelight.ui.events.KeyPressedEvent;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextInputPanelTest extends Assert
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
    assertEquals(true, panel.cursorThread.isAlive());
    assertEquals(true, root.dirtyRegions.contains(panel.getBoundingBox()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBoundingBox()));
  }

  @Test
  public void canLoseFocus()
  {
    panel.cursorCycleTime = 0;
    root.getKeyListener().focusOn(panel);
    root.dirtyRegions.clear();
    root.getKeyListener().focusOn(root);

    assertEquals(false, panel.hasFocus());
    assertEquals(true, panel.cursorThread.isAlive());
    assertEquals(true, root.dirtyRegions.contains(panel.getBoundingBox()));
    assertEquals(true, root.dirtyRegions.contains(parent.getBoundingBox()));
  }

  @Test
  public void willRememberTheLastKeyPressed()
  {
    panel.getEventHandler().dispatch(new KeyPressedEvent(panel, 0, 10, 0));

    assertEquals(10, panel.getLastKeyPressed());
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
    model.setCaretIndex(0);

    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, 'Z'));

    assertEquals("ZSome Text", model.getText());
  }

  @Test
  public void typedCharsWithCommandModifierDoNothing() throws Exception
  {
    model.setCaretIndex(0);

    panel.getEventHandler().dispatch(new CharTypedEvent(panel, KeyEvent.COMMAND_MASK, 'A'));

    assertEquals("Some Text", model.getText());
  }
  
  @Test
  public void typedCharsWithControlModifierDoNothing() throws Exception
  {
    model.setCaretIndex(0);

    panel.getEventHandler().dispatch(new CharTypedEvent(panel, KeyEvent.CONTROL_MASK, 'A'));

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void typingACharMakesThePanelDirty() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    model.setCaretIndex(0);
    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, 'Z'));

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBoundingBox(), root.dirtyRegions.get(0));
  }
  
  @Test
  public void backspaceIsNotTyped() throws Exception
  {
    model.setCaretIndex(0);
    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, '\b'));

    assertEquals("Some Text", model.getText());
  }

  @Test
  public void newlineIsTyped() throws Exception
  {
    model.setCaretIndex(0);
    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, '\n'));

    assertEquals("\nSome Text", model.getText());
  }

}
