package limelight;

import junit.framework.TestCase;

public class StackableStyleTest extends TestCase
{
  private StackableStyle style;

  public void setUp() throws Exception
  {
    style = new StackableStyle();
  }

  public void testReduction() throws Exception
  {
    style.setWidth("123");

    FlatStyle style2 = new FlatStyle();
    style2.setHeight("321");

    FlatStyle style3 = new FlatStyle();
    style3.setFontSize("456");

    style.push(style2);
    style.push(style3);

    FlatStyle reduced = style.getReducedStyle();
    assertEquals("123", reduced.getWidth());
    assertEquals("321", reduced.getHeight());
    assertEquals("456", reduced.getFontSize());
  }

  public void testChecksumIsSameAsNormalStyle() throws Exception
  {
    style.setWidth("123");

    FlatStyle style2 = new FlatStyle();
    style2.setHeight("321");

    FlatStyle style3 = new FlatStyle();
    style3.setFontSize("456");

    style.push(style2);
    style.push(style3);

    FlatStyle normal = new FlatStyle();
    normal.setWidth("123");
    normal.setHeight("321");
    normal.setFontSize("456");

    assertEquals(normal.checksum(), style.checksum());
  }
}
