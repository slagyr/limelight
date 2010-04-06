//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.util.Box;

public class StaticXCoordinateAttributeTest extends TestCase
{
  private StaticXCoordinateAttribute ten;
  private StaticXCoordinateAttribute fifty;

  public void setUp() throws Exception
  {
    ten = new StaticXCoordinateAttribute(10);
    fifty = new StaticXCoordinateAttribute(50);
  }

  public void testGetX() throws Exception
  {
    assertEquals(10, ten.getX(0, new Box(0, 0, 100, 100)));
    assertEquals(15, ten.getX(0, new Box(5, 0, 100, 100)));
    assertEquals(10, ten.getX(-1, new Box(0, 0, 100, 100)));

    assertEquals(50, fifty.getX(0, new Box(0, 0, 100, 100)));
  }
}
