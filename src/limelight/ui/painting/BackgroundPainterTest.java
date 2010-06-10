//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;
import limelight.ui.Painter;
import limelight.util.Colors;
import limelight.ui.MockGraphics;
import limelight.ui.model.MockPropablePanel;
import limelight.styles.Style;
import limelight.styles.compiling.RealStyleAttributeCompilerFactory;

import java.awt.*;

public class BackgroundPainterTest extends TestCase
{
  static
  {
    RealStyleAttributeCompilerFactory.install();
  }

  private Style style;
  private Painter painter;
  private MockGraphics graphics;
  private MockPropablePanel panel;

  public void setUp() throws Exception
  {
    panel = new MockPropablePanel();
    style = panel.getStyle();
    painter = BackgroundPainter.instance;
    graphics = new MockGraphics();
  }

  public void testNoPainting() throws Exception
  {
    style.setBackgroundColor("transparent");

    painter.paint(graphics, panel);

    assertEquals(0, graphics.filledShapes.size());
  }

//  public void testImageBackground() throws Exception
//  {
//    //???
//  }

  public void testPlainColor() throws Exception
  {
    style.setBackgroundColor("blue");

    painter.paint(graphics, panel);

    assertEquals(1, graphics.filledShapes.size());
    assertEquals(Color.blue, graphics.filledShapes.get(0).color);
    assertEquals(null, graphics.filledShapes.get(0).paint);
  }

  public void testGradient() throws Exception
  {
    style.setBackgroundColor("blue");
    style.setGradient("on");
     
    painter.paint(graphics, panel);

    assertEquals(1, graphics.filledShapes.size());
    MockGraphics.DrawnShape filledShape = graphics.filledShapes.get(0);
    assertEquals(GradientPaint.class, filledShape.paint.getClass());
    GradientPaint paint = (GradientPaint)filledShape.paint;
    assertEquals(Color.blue, paint.getColor1());
    assertEquals(Colors.TRANSPARENT, paint.getColor2());
  }
}
