package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class ButtonPanelTest extends TestCase
{
  private ButtonPanel panel;

  public void setUp() throws Exception
  {
    panel = new ButtonPanel();
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
  
}
