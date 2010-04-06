//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import junit.framework.TestCase;

public class BoxTest extends TestCase
{
  public void testDimensionsEqual() throws Exception
  {
    Box b1 = new Box(0, 0, 100, 200);
    Box b2 = new Box(10, 20, 100, 200);
    Box b3 = new Box(0, 0, 300, 400);

    assertEquals(true, b1.sameSize(b1));
    assertEquals(true, b1.sameSize(b2));
    assertEquals(false, b1.sameSize(b3));
  }
}
