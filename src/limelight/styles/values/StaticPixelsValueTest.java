//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;

import limelight.util.Box;

public class StaticPixelsValueTest extends TestCase
{
  public void testReturnsPixles() throws Exception
  {
    assertEquals(50, new StaticPixelsValue(50).pixelsFor(100));
    assertEquals(66, new StaticPixelsValue(66).pixelsFor(100));
    assertEquals(50, new StaticPixelsValue(50).pixelsFor(new Box(0, 0, 100, 200)));
    assertEquals(66, new StaticPixelsValue(66).pixelsFor(new Box(0, 0, 100, 200)));
  }
}
