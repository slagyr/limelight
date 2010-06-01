//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.ui.MockGraphics;

import java.awt.*;

public class FillStrategyValueTest extends TestCase
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
