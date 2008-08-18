package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class CheckBoxPanelTest extends TestCase
{
  private CheckBoxPanel panel;

  public void setUp() throws Exception
  {
    panel = new CheckBoxPanel();
  }
  
  public void testisNotBufferable() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
}
