//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;
import limelight.util.Colors;
import limelight.ui.MockGraphics;
import limelight.ui.model.MockPanel;
import limelight.styles.Style;

import java.awt.*;

public class BackgroundPainterTest extends TestCase
{
  private MockPanel panel;
  private Style style;
  private BackgroundPainter painter;
  private MockGraphics graphics;

  public void setUp() throws Exception
  {
    panel = new MockPanel();
    style = panel.getStyle();
    painter = new BackgroundPainter(panel);
    graphics = new MockGraphics();
  }

  public void testNoPainting() throws Exception
  {
    style.setBackgroundColor("transparent");

    painter.paint(graphics);

    assertEquals(0, graphics.filledShapes.size());
  }

//  public void testImageBackground() throws Exception
//  {
//    //???
//  }

  public void testPlainColor() throws Exception
  {
    style.setBackgroundColor("blue");

    painter.paint(graphics);

    assertEquals(1, graphics.filledShapes.size());
    assertEquals(Color.blue, graphics.filledShapes.get(0).color);
    assertEquals(null, graphics.filledShapes.get(0).paint);
  }

  public void testGradient() throws Exception
  {
    style.setBackgroundColor("blue");
    style.setGradient("on");
     
    painter.paint(graphics);

    assertEquals(1, graphics.filledShapes.size());
    MockGraphics.DrawnShape filledShape = graphics.filledShapes.get(0);
    assertEquals(GradientPaint.class, filledShape.paint.getClass());
    GradientPaint paint = (GradientPaint)filledShape.paint;
    assertEquals(Color.blue, paint.getColor1());
    assertEquals(Colors.TRANSPARENT, paint.getColor2());
  }
}
