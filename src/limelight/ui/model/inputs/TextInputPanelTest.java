//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.Panel;
import limelight.ui.api.MockProp;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class TextInputPanelTest extends Assert
{
  TextInputPanel panel;
  Panel parent;
  MockGraphics graphics;
  TextModel boxInfo;

  public class MockFocusEvent extends FocusEvent
  {
    public MockFocusEvent()
    {
      super(new java.awt.Panel(), 1);
    }
  }

  @Before
  public void setUp()
  {
    panel = new MockTextInputPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    boxInfo = panel.getModel();
    boxInfo.setText("Some Text");
  }

  @Test
  public void shouldDefaultLayout() throws Exception
  {
    assertSame(TextInputPanelLayout.instance, panel.getDefaultLayout());
  }

  @Test
  public void canGainFocus()
  {
    panel.focusGained(new MockFocusEvent());
    assertEquals(true, panel.focused);
    assertEquals(true, panel.cursorThread.isAlive());
  }

  @Test
  public void canLoseFocus()
  {
    panel.cursorCycleTime = 0;
    panel.focusGained(new MockFocusEvent());
    panel.focusLost(new MockFocusEvent());
    assertEquals(false, panel.focused);
    assertEquals(true, panel.cursorThread.isAlive());
  }

  public static class MockKeyEvent extends KeyEvent
  {
    public MockKeyEvent(int modifiers, int keyCode)
    {
      super(new java.awt.Panel(), 1, 123456789l, modifiers, keyCode, ' ');
    }
  }

  @Test
  public void willRememberTheLastKeyPressed()
  {
    MockKeyEvent event = new MockKeyEvent(0,10);
    panel.keyPressed(event);

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

}
