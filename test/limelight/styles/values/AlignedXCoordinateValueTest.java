//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.HorizontalAlignment;
import limelight.util.Box;

public class AlignedXCoordinateValueTest extends TestCase
{
  private AlignedXCoordinateValue center;

  public void setUp() throws Exception
  {
    center = new AlignedXCoordinateValue(HorizontalAlignment.CENTER);
  }
  
  public void testGetX() throws Exception
  {
    assertEquals(25, center.getX(50, new Box(0, 0, 100, 100)));
  }
}
