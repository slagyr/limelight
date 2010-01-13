package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.api.MockProp;
import limelight.ui.model.MockPropFrame;
import limelight.ui.model.PropPanel;
import limelight.ui.model.RootPanel;
import limelight.ui.model.TextAccessor;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.FocusEvent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextInputPanelTest
{
  TextInputPanel panel;
  PropPanel parent;

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

}
