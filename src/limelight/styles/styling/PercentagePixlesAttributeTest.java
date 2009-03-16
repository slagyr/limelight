//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;

import limelight.util.Box;

public class PercentagePixlesAttributeTest extends TestCase
{
  public void testReturnsPercentageOfPixels() throws Exception
  {
    assertEquals(50, new PercentagePixelsAttribute(50.0).pixelsFor(100));
    assertEquals(67, new PercentagePixelsAttribute(66.6).pixelsFor(100));
  }

  public void testReturnsPercentageOfMinDimension() throws Exception
  {
    assertEquals(50, new PercentagePixelsAttribute(50.0).pixelsFor(new Box(0, 0, 100, 200)));
    assertEquals(67, new PercentagePixelsAttribute(66.6).pixelsFor(new Box(0, 0, 100, 200)));
  }
}
