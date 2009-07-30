package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class InputPanelLayoutTest extends TestCase
{
  public void testOverride() throws Exception
  {
    assertEquals(true, InputPanelLayout.instance.overides(null));
  }
}
