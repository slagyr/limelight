package limelight.ui;

import junit.framework.TestCase;

public class TextBoxPanelTest extends TestCase
{
  private TextBoxPanel textBox;
  private MockBlockPanel parent;

  public void setUp() throws Exception
  {
    MockRootBlockPanel root = new MockRootBlockPanel();
    parent = new MockBlockPanel();
    textBox = new TextBoxPanel();

    root.add(parent);
    parent.add(textBox);

    parent.setSize(100, 100);
  }

  public void testConstruction() throws Exception
  {
    assertNotNull(textBox.getTextBox());
  }

  public void testSnapToSize() throws Exception
  {
    parent.getBlock().getStyle().setWidth("100");
    parent.getBlock().getStyle().setHeight("100");
    parent.getBlock().getStyle().setMargin("5");
    parent.getBlock().getStyle().setPadding("10");

    parent.doLayout();

    assertEquals(70, textBox.getWidth());
    assertEquals(70, textBox.getHeight());
    assertEquals(70, textBox.getTextBox().getWidth());
    assertEquals(70, textBox.getTextBox().getHeight());
  }
}
