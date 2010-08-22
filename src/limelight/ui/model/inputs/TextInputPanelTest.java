//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.api.MockProp;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;

public class TextInputPanelTest extends Assert
{
  private TextInputPanel panel;
  private MockRootPanel root;

  @Before
  public void setUp()
  {
    root = new MockRootPanel();
    panel = new MockTextInputPanel();
    Panel parent = new PropPanel(new MockProp());
    parent.add(panel);
    root.add(parent);
    TextModel model = panel.getModel();
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
    root.getKeyListener().focusOn(panel);
    assertEquals(true, panel.hasFocus());
    assertEquals(true, panel.cursorThread.isAlive());
  }

  @Test
  public void canLoseFocus()
  {
    panel.cursorCycleTime = 0;
    root.getKeyListener().focusOn(panel);
    root.getKeyListener().focusOn(root);
    assertEquals(false, panel.hasFocus());
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
