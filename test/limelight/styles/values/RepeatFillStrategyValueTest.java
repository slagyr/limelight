//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.ui.MockGraphics;
import limelight.util.Box;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;

import java.awt.*;

public class RepeatFillStrategyValueTest extends FillStrategyValueTest
{
  private RepeatFillStrategyValue attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new RepeatFillStrategyValue();
    graphics = new MockGraphics();
    clip = new Box(0, 0, 60, 60);
    graphics.clip = clip;
  }

  public void testName() throws Exception
  {
    assertEquals("repeat", attribute.toString());
  }

  public void testFill() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateValue(0), new StaticYCoordinateValue(0));

    assertEquals(9, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 0, 0);
    checkImage(graphics.drawnImages.get(1), image, 20, 0);
    checkImage(graphics.drawnImages.get(2), image, 40, 0);
    checkImage(graphics.drawnImages.get(3), image, 0, 20);
    checkImage(graphics.drawnImages.get(4), image, 20, 20);
    checkImage(graphics.drawnImages.get(5), image, 40, 20);
    checkImage(graphics.drawnImages.get(6), image, 0, 40);
    checkImage(graphics.drawnImages.get(7), image, 20, 40);
    checkImage(graphics.drawnImages.get(8), image, 40, 40);
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateValue(20), new StaticYCoordinateValue(20));

    assertEquals(4, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 20, 20);
    checkImage(graphics.drawnImages.get(1), image, 40, 20);
    checkImage(graphics.drawnImages.get(2), image, 20, 40);
    checkImage(graphics.drawnImages.get(3), image, 40, 40);
  }
  
  public void testFillUneven() throws Exception
  {
    Image image = new MockImage(30, 30);
    attribute.fill(graphics, image, new StaticXCoordinateValue(20), new StaticYCoordinateValue(20));

    assertEquals(4, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 20, 20);
    checkImage(graphics.drawnImages.get(1), image, 50, 20);
    checkImage(graphics.drawnImages.get(2), image, 20, 50);
    checkImage(graphics.drawnImages.get(3), image, 50, 50);
  }
  
  public void testFillAligned() throws Exception
  {
    Image image = new MockImage(35, 35);
    attribute.fill(graphics, image, new AlignedXCoordinateValue(HorizontalAlignment.CENTER), new AlignedYCoordinateValue(VerticalAlignment.CENTER));

    assertEquals(4, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, -5, -5);
    checkImage(graphics.drawnImages.get(1), image, 30, -5);
    checkImage(graphics.drawnImages.get(2), image, -5, 30);
    checkImage(graphics.drawnImages.get(3), image, 30, 30);
  }
}
