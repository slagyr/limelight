package limelight.ui;

import junit.framework.TestCase;

public class StyleTest extends TestCase
{
  public void testSizeDefaults() throws Exception
  {
    assertEquals("auto", Style.WIDTH.defaultValue);
    assertEquals("auto", Style.HEIGHT.defaultValue);
  }

  public void testFontDefaults() throws Exception
  {
    assertEquals("Arial", Style.FONT_FACE.defaultValue);
    assertEquals("12", Style.FONT_SIZE.defaultValue);
    assertEquals("black", Style.TEXT_COLOR.defaultValue);
  }

  public void testScrolling() throws Exception
  {
    assertEquals("auto", Style.VERTICAL_SCROLLBAR.defaultValue);
    assertEquals("auto", Style.HORIZONTAL_SCROLLBAR.defaultValue);
  }
}
