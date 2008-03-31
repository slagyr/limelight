package limelight.ui;

import junit.framework.TestCase;

public class StyleTest extends TestCase
{
  public void testSizeDefaults() throws Exception
  {
    assertEquals("auto", Style.WIDTH.defaultValue);
    assertEquals("auto", Style.HEIGHT.defaultValue);
  }
}
