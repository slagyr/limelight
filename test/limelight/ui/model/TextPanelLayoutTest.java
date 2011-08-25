//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;

public class TextPanelLayoutTest extends TestCase
{
    public void testOverride() throws Exception
    {
      assertEquals(true, TextPanelLayout.instance.overides(null));
    }
  }
