package limelight.ui.model;

import junit.framework.TestCase;

public class ImagePanelLayoutTest extends TestCase
{
  public void testOverride() throws Exception
  {
    assertEquals(true, ImagePanelLayout.instance.overides(null));
  }
}
