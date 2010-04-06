//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.util.Box;

public class PercentageYCoordinateAttributeTest extends TestCase
{
  private PercentageYCoordinateAttribute dime;
  private PercentageYCoordinateAttribute half;

  public void setUp() throws Exception
  {
    dime = new PercentageYCoordinateAttribute(10.0);
    half = new PercentageYCoordinateAttribute(50.0);
  }
  
  public void testGetY() throws Exception
  {
    assertEquals(10, dime.getY(0, new Box(0, 0, 100, 100)));
    assertEquals(15, dime.getY(0, new Box(0, 5, 100, 100)));
    assertEquals(50, dime.getY(0, new Box(0, 0, 100, 500)));

    assertEquals(50, half.getY(0, new Box(0, 0, 100, 100)));
    assertEquals(55, half.getY(0, new Box(0, 5, 100, 100)));
    assertEquals(250, half.getY(0, new Box(0, 0, 100, 500)));
  }
}
