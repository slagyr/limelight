package limelight.styles.styling;

import limelight.ui.MockGraphics;
import limelight.util.Box;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;

import java.awt.*;

public class ScaleXFillStrategyAttributeTest extends FillStrategyAttributeTest
{
  private ScaleXFillStrategyAttribute attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new ScaleXFillStrategyAttribute();
    graphics = new MockGraphics();
    clip = new Box(0, 0, 60, 60);
    graphics.clip = clip;
  }

  public void testName() throws Exception
  {
    assertEquals("scale_x", attribute.toString());
  }

  public void testFill() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(0), new StaticYCoordinateAttribute(0));

    assertEquals(1, graphics.drawnImages.size());
    checkScaledImage(image, graphics.drawnImages.get(0), new Rectangle(0, 0, 20, 20), new Rectangle(0, 0, 60, 20));
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(20), new StaticYCoordinateAttribute(20));

    assertEquals(1, graphics.drawnImages.size());
    checkScaledImage(image, graphics.drawnImages.get(0), new Rectangle(0, 0, 20, 20), new Rectangle(20, 20, 60, 20));
  }

  public void testFillAligned() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new AlignedXCoordinateAttribute(HorizontalAlignment.CENTER), new AlignedYCoordinateAttribute(VerticalAlignment.CENTER));

    assertEquals(1, graphics.drawnImages.size());
    checkScaledImage(image, graphics.drawnImages.get(0), new Rectangle(0, 0, 20, 20), new Rectangle(0, 20, 60, 20));
  }
}
