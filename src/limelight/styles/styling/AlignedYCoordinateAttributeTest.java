//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.VerticalAlignment;
import limelight.util.Box;

public class AlignedYCoordinateAttributeTest extends TestCase
{
  private AlignedYCoordinateAttribute center;

  public void setUp() throws Exception
  {
    center = new AlignedYCoordinateAttribute(VerticalAlignment.CENTER);
  }

  public void testGetY() throws Exception
  {
    assertEquals(25, center.getY(50, new Box(0, 0, 100, 100)));
  }
}
