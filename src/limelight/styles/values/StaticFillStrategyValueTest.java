//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.ui.MockGraphics;
import limelight.util.Box;

import java.awt.*;

public class StaticFillStrategyValueTest extends FillStrategyValueTest
{
  private StaticFillStrategyValue attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new StaticFillStrategyValue();
    graphics = new MockGraphics();
    clip = new Box(0, 0, 60, 60);
    graphics.clip = clip;
  }

  public void testName() throws Exception
  {
    assertEquals("static", attribute.toString());
  }

  public void testFill() throws Exception
  {
    Image image = new MockImage(50, 50);
    attribute.fill(graphics, image, new StaticXCoordinateValue(0), new StaticYCoordinateValue(0));

    assertEquals(1, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 0, 0);
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(50, 50);
    attribute.fill(graphics, image, new StaticXCoordinateValue(12), new StaticYCoordinateValue(34));

    assertEquals(1, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 12, 34);
  }

}
