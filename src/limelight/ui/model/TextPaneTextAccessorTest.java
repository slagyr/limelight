//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.LimelightException;

public class TextPaneTextAccessorTest extends TestCase
{
  private Panel panel;
  private TextPaneTextAccessor accessor;

  public void setUp() throws Exception
  {
    panel = new Panel(new MockProp());
    accessor = new TextPaneTextAccessor(panel);
    panel.setTextAccessor(accessor);
  }

  public void testSetInitialText() throws Exception
  {
    accessor.setText("blah");

    assertEquals(1, panel.getComponents().length);
    assertEquals(TextPane.class, panel.getComponents()[0].getClass());
    assertEquals("blah", ((TextPane)panel.getComponents()[0]).getText());
    assertEquals("blah", accessor.getText());
  }

  public void testSettingTextMultipleTimes() throws Exception
  {
    accessor.setText("blah");
    TextPane pane = (TextPane)panel.getComponents()[0];

    accessor.setText("second text");
    assertEquals(1, panel.getComponents().length);
    assertSame(pane, panel.getComponents()[0]);

    assertEquals("second text", accessor.getText());
  }

  public void testSettingTextSterilizesThePanel() throws Exception
  {
    accessor.setText("blah");
    assertTrue(panel.isSterilized());
  }

  public void testSettingTextWhenThereIsAlreadyChildren() throws Exception
  {
    panel.add(new Panel(new MockProp()));
    try
    {
      accessor.setText("blah");
      fail("should throw an exception");
    }
    catch(LimelightException e)
    {
      assertEquals("You may only set text on empty props.", e.getMessage());
    }
  }

  public void testSettingEmptyTextOrNullDoesNothingAtFirst() throws Exception
  {
    accessor.setText("");
    assertEquals(0, panel.getComponents().length);

    accessor.setText(null);
    assertEquals(0, panel.getComponents().length);
  }
  
  public void testSettingEmptyTextCanClearTheText() throws Exception
  {
    accessor.setText("blah");
    accessor.setText("");

    assertEquals("", ((TextPane)panel.getComponents()[0]).getText());
    assertEquals("", accessor.getText());

    accessor.setText("blah");
    accessor.setText(null);

    assertEquals(null, ((TextPane)panel.getComponents()[0]).getText());
    assertEquals(null, accessor.getText());
  }
}
