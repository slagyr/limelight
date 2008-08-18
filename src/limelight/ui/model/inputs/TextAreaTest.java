package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class TextAreaTest extends TestCase
{
  private TextArea textArea;
  private TextAreaPanel panel;

  public void setUp() throws Exception
  {
    panel = new TextAreaPanel();
    textArea = new TextArea(panel);
  }

  public void testLineWrapping() throws Exception
  {
    assertEquals(true, textArea.getLineWrap());
  }
}
