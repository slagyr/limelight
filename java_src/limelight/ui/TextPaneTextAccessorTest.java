package limelight.ui;

import junit.framework.TestCase;
import limelight.LimelightException;

public class TextPaneTextAccessorTest extends TestCase
{
  private Panel panel;
  private TextPaneTextAccessor accessor;

  public void setUp() throws Exception
  {
    panel = new Panel(new MockBlock());
    accessor = new TextPaneTextAccessor(panel);
    panel.setTextAccessor(accessor);
  }

  public void testSetInitialText() throws Exception
  {
    accessor.setText("blah");

    assertEquals(1, panel.getChildren().size());
    assertEquals(TextPanel.class, panel.getChildren().get(0).getClass());
    assertEquals("blah", ((TextPanel)panel.getChildren().get(0)).getText());
    assertEquals("blah", accessor.getText());
  }

  public void testSettingTextMultipleTimes() throws Exception
  {
    accessor.setText("blah");
    TextPanel panel = (TextPanel) this.panel.getChildren().get(0);

    accessor.setText("second text");
    assertEquals(1, this.panel.getChildren().size());
    assertSame(panel, this.panel.getChildren().get(0));

    assertEquals("second text", accessor.getText());
  }

  public void testSettingTextSterilizesThePanel() throws Exception
  {
    accessor.setText("blah");
    assertTrue(panel.isSterilized());
  }

  public void testSettingTextWhenThereIsAlreadyChildren() throws Exception
  {
    panel.add(new Panel(new MockBlock()));
    try
    {
      accessor.setText("blah");
      fail("should throw an exception");
    }
    catch(LimelightException e)
    {
      assertEquals("You may only set text on empty blocks.", e.getMessage());
    }
  }

  public void testSettingEmptyTextOrNullDoesNothingAtFirst() throws Exception
  {
    accessor.setText("");
    assertEquals(0, panel.getChildren().size());

    accessor.setText(null);
    assertEquals(0, panel.getChildren().size());
  }

  public void testSettingEmptyTextCanClearTheText() throws Exception
  {
    accessor.setText("blah");
    accessor.setText("");

    assertEquals("", ((TextPanel)panel.getChildren().get(0)).getText());
    assertEquals("", accessor.getText());

    accessor.setText("blah");
    accessor.setText(null);

    assertEquals(null, ((TextPanel)panel.getChildren().get(0)).getText());
    assertEquals(null, accessor.getText());
  }
}

