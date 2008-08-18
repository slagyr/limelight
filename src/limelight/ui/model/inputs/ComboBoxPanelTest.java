package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class ComboBoxPanelTest extends TestCase
{
  private ComboBoxPanel panel;

  public void setUp() throws Exception
  {
    panel = new ComboBoxPanel();
  }
  
  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
}
