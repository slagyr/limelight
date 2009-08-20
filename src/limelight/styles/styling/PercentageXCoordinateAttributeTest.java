//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.util.Box;

public class PercentageXCoordinateAttributeTest extends TestCase
{
  private PercentageXCoordinateAttribute dime;
  private PercentageXCoordinateAttribute half;

  public void setUp() throws Exception
  {
    dime = new PercentageXCoordinateAttribute(10.0);
    half = new PercentageXCoordinateAttribute(50.0);
  }
  
  public void testGetX() throws Exception
  {
    assertEquals(10, dime.getX(0, new Box(0, 0, 100, 100)));
    assertEquals(15, dime.getX(0, new Box(5, 0, 100, 100)));
    assertEquals(50, dime.getX(0, new Box(0, 0, 500, 100)));

    assertEquals(50, half.getX(0, new Box(0, 0, 100, 100)));
    assertEquals(55, half.getX(0, new Box(5, 0, 100, 100)));
    assertEquals(250, half.getX(0, new Box(0, 0, 500, 100)));
  }
}
