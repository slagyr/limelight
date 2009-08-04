package limelight.styles.styling;

import limelight.ui.MockGraphics;
import limelight.util.Box;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;

import java.awt.*;

public class RepeatXFillStrategyAttributeTest extends FillStrategyAttributeTest
{
  private RepeatXFillStrategyAttribute attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new RepeatXFillStrategyAttribute();
    graphics = new MockGraphics();
    clip = new Box(0, 0, 60, 60);
    graphics.clip = clip;
  }

  public void testName() throws Exception
  {
    assertEquals("repeat_x", attribute.toString());
  }

  public void testFill() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(0), new StaticYCoordinateAttribute(0));

    assertEquals(3, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 0, 0);
    checkImage(graphics.drawnImages.get(1), image, 20, 0);
    checkImage(graphics.drawnImages.get(2), image, 40, 0);
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(20), new StaticYCoordinateAttribute(20));

    assertEquals(2, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 20, 20);
    checkImage(graphics.drawnImages.get(1), image, 40, 20);
  }

  public void testFillUneven() throws Exception
  {
    Image image = new MockImage(30, 30);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(20), new StaticYCoordinateAttribute(20));

    assertEquals(2, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, 20, 20);
    checkImage(graphics.drawnImages.get(1), image, 50, 20);
  }
  
  public void testFillAligned() throws Exception
  {
    Image image = new MockImage(35, 35);
    attribute.fill(graphics, image, new AlignedXCoordinateAttribute(HorizontalAlignment.CENTER), new AlignedYCoordinateAttribute(VerticalAlignment.CENTER));

    assertEquals(2, graphics.drawnImages.size());
    checkImage(graphics.drawnImages.get(0), image, -5, 12);
    checkImage(graphics.drawnImages.get(1), image, 30, 12);
  }


}
