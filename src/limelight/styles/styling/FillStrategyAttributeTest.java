package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.ui.MockGraphics;

import java.awt.*;

public class FillStrategyAttributeTest extends TestCase
{
  public void checkImage(MockGraphics.DrawnImage drawnImage, Image image, int x, int y)
  {
    assertEquals(image, drawnImage.image);
    assertEquals(x, drawnImage.x);
    assertEquals(y, drawnImage.y);
  }

  protected void checkScaledImage(Image image, MockGraphics.DrawnImage drawnImage, Rectangle source, Rectangle area)
  {
    assertEquals(image, drawnImage.image);
    assertEquals(source, drawnImage.source);
    assertEquals(area, drawnImage.destination);
  }

  public void testToKeepAntHappy() throws Exception
  {
    
  }
}
