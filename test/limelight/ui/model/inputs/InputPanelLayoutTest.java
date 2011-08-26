//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class InputPanelLayoutTest extends TestCase
{
  public void testOverride() throws Exception
  {
    assertEquals(true, InputPanelLayout.instance.overides(null));
  }
}
