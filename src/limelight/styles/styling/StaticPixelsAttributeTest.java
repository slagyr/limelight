//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;

import limelight.util.Box;

public class StaticPixelsAttributeTest extends TestCase
{
  public void testReturnsPixles() throws Exception
  {
    assertEquals(50, new StaticPixelsAttribute(50).pixelsFor(100));
    assertEquals(66, new StaticPixelsAttribute(66).pixelsFor(100));
    assertEquals(50, new StaticPixelsAttribute(50).pixelsFor(new Box(0, 0, 100, 200)));
    assertEquals(66, new StaticPixelsAttribute(66).pixelsFor(new Box(0, 0, 100, 200)));
  }
}
