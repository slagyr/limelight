package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class RadioButtonPanelTest extends TestCase
{
  private RadioButtonPanel panel;

  public void setUp() throws Exception
  {
    panel = new RadioButtonPanel();
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
}
