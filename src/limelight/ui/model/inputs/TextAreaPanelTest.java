package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class TextAreaPanelTest extends TestCase
{
  private TextAreaPanel panel;

  public void setUp() throws Exception
  {
    panel = new TextAreaPanel();
  }
  
  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

}
