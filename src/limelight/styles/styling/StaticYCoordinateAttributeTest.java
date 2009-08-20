//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.util.Box;

public class StaticYCoordinateAttributeTest extends TestCase
{
  private StaticYCoordinateAttribute ten;
  private StaticYCoordinateAttribute fifty;

  public void setUp() throws Exception
  {
    ten = new StaticYCoordinateAttribute(10);
    fifty = new StaticYCoordinateAttribute(50);
  }

  public void testGetY() throws Exception
  {
    assertEquals(10, ten.getY(0, new Box(0, 0, 100, 100)));
    assertEquals(15, ten.getY(0, new Box(0, 5, 100, 100)));
    assertEquals(10, ten.getY(-1, new Box(0, 0, 100, 100)));

    assertEquals(50, fifty.getY(0, new Box(0, 0, 100, 100)));
  }
}
