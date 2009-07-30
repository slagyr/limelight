package limelight.ui.model;

import junit.framework.TestCase;

public class TextPanelLayoutTest extends TestCase
{
    public void testOverride() throws Exception
    {
      assertEquals(true, TextPanelLayout.instance.overides(null));
    }
  }
