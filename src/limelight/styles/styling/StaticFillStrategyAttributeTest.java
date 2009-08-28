//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.ui.MockGraphics;
import limelight.util.Box;

import java.awt.*;

public class StaticFillStrategyAttributeTest extends FillStrategyAttributeTest
{
  private StaticFillStrategyAttribute attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new StaticFillStrategyAttribute();
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
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(0), new StaticYCoordinateAttribute(0));

    assertEquals(1, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 0, 0);
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(50, 50);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(12), new StaticYCoordinateAttribute(34));

    assertEquals(1, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 12, 34);
  }

}
