package limelight.ui.model2.inputs;

import junit.framework.TestCase;
import javax.swing.*;

public class TextBoxPanelTest extends TestCase
{
  private TextBoxPanel panel;

  public void setUp() throws Exception
  {
    panel = new TextBoxPanel();
  }

  public void testHasJtextBox() throws Exception
  {
    assertEquals(panel.getTextBox().getClass(), TextBox.class);    
  }

}
