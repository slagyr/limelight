//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.util.Box;

public class PercentageYCoordinateValueTest extends TestCase
{
  private PercentageYCoordinateValue dime;
  private PercentageYCoordinateValue half;

  public void setUp() throws Exception
  {
    dime = new PercentageYCoordinateValue(10.0);
    half = new PercentageYCoordinateValue(50.0);
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
