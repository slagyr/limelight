package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextInputPanelTest
{
  TextInputPanel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel boxInfo;

  public class MockFocusEvent extends FocusEvent
  {
    public MockFocusEvent()
    {
      super(new Panel(), 1);
    }
  }

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    boxInfo = panel.getModelInfo();
    boxInfo.setText("Some Text");
  }

  @Test
  public void canGainFocus()
  {
    panel.focusGained(new MockFocusEvent());
    assertTrue(panel.focused);
    assertTrue(panel.cursorThread.isAlive());
    panel.focused = false;
  }

  @Test
  public void canLoseFocus()
  {
    panel.cursorCycleTime = 0;
    panel.focusGained(new MockFocusEvent());
    panel.focusLost(new MockFocusEvent());
    assertFalse(panel.focused);
    assertTrue(panel.cursorThread.isAlive());
  }

  @Test
  public void canTellIfTextMaxesOutTextArea()
  {
    boxInfo.setText("This Text is tooooo much text to fit inside the normal textbox");

    assertTrue(panel.isTextMaxed());
  }

  @Test
  public void canMaxOutPaintableRegionToEncapsulateTextIfPanelFull(){
    panel.maxOutPaintableRegion();

    assertEquals(panel.getWidth() - 2 * TextModel.SIDE_TEXT_MARGIN,panel.paintableRegion.width );
    assertEquals(TextModel.SIDE_TEXT_MARGIN,panel.paintableRegion.x );
  }

  public static class MockKeyEvent extends KeyEvent
  {

    public MockKeyEvent(int modifiers, int keyCode)
    {
      super(new Panel(), 1, 123456789l, modifiers, keyCode, ' ');
    }
  }

  @Test
  public void willRememberTheLastKeyPressed()
  {
    MockKeyEvent event = new MockKeyEvent(0,10);
    panel.keyPressed(event);

    assertEquals(10, panel.getLastKeyPressed());
  }


}
