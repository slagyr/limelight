//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import junit.framework.TestCase;
import limelight.styles.Style;
import limelight.ui.MockGraphics;
import limelight.ui.Painter;
import limelight.ui.model.MockPropablePanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public class BorderPainterTest extends TestCase
{
  private MockPropablePanel panel;
  private Style style;
  private Painter painter;
  private Box border;
  private MockGraphics graphics;

  public void setUp() throws Exception
  {
    panel = new MockPropablePanel();
    style = panel.getStyle();
    border = new Box(5, 5, 100, 100);
    panel.boxInsideMargins = border;
    painter = BorderPainter.instance;
    graphics = new MockGraphics();
  }

  public void testNoBorder() throws Exception
  {
    painter.paint(graphics, panel);
    assertEquals(0, graphics.drawnShapes.size());
  }

  public void testBorderOnePixelWide() throws Exception
  {
    style.setBorderColor("blue");
    style.setBorderWidth("1");

    painter.paint(graphics, panel);

    assertEquals(4, graphics.drawnShapes.size());

    checkLine(graphics.drawnShapes.get(0), 1, Color.blue, 5, 5, 104, 5);
    checkLine(graphics.drawnShapes.get(1), 1, Color.blue, 104, 5, 104, 104);
    checkLine(graphics.drawnShapes.get(2), 1, Color.blue, 104, 104, 5, 104);
    checkLine(graphics.drawnShapes.get(3), 1, Color.blue, 5, 104, 5, 5);
  }

  public void testGetLinesFromBorderWithEven() throws Exception
  {
    border = new Box(0, 0, 100, 200);
    panel.boxInsideMargins = border;
    style.setBorderWidth("4");
    style.setBorderColor("blue");

    painter.paint(graphics, panel);

    checkLine(graphics.drawnShapes.get(0), 4, Color.blue, 2, 2, 98, 2);
    checkLine(graphics.drawnShapes.get(1), 4, Color.blue, 98, 2, 98, 198);
    checkLine(graphics.drawnShapes.get(2), 4, Color.blue, 98, 198, 2, 198);
    checkLine(graphics.drawnShapes.get(3), 4, Color.blue, 2, 198, 2, 2);
  }

    public void testGetLinesFromBorderWithOdd() throws Exception
  {
    border = new Box(0, 0, 100, 200);
    panel.boxInsideMargins = border;
    style.setBorderWidth("5");
    style.setBorderColor("blue");

    painter.paint(graphics, panel);

    checkLine(graphics.drawnShapes.get(0), 5, Color.blue, 2, 2, 97, 2);
    checkLine(graphics.drawnShapes.get(1), 5, Color.blue, 97, 2, 97, 197);
    checkLine(graphics.drawnShapes.get(2), 5, Color.blue, 97, 197, 2, 197);
    checkLine(graphics.drawnShapes.get(3), 5, Color.blue, 2, 197, 2, 2);
  }

  public void testBorderTwoPixelsWide() throws Exception
  {
    style.setBorderColor("blue");
    style.setBorderWidth("2");

    painter.paint(graphics, panel);

    assertEquals(4, graphics.drawnShapes.size());

    checkLine(graphics.drawnShapes.get(0), 2, Color.blue, 6, 6, 104, 6);
    checkLine(graphics.drawnShapes.get(1), 2, Color.blue, 104, 6, 104, 104);
    checkLine(graphics.drawnShapes.get(2), 2, Color.blue, 104, 104, 6, 104);
    checkLine(graphics.drawnShapes.get(3), 2, Color.blue, 6, 104, 6, 6);
  }

  public void testBorderBuffet() throws Exception
  {
    style.setTopBorderColor("Red");
    style.setTopBorderWidth("1");
    style.setRightBorderColor("Green");
    style.setRightBorderWidth("2");
    style.setBottomBorderColor("Blue");
    style.setBottomBorderWidth("3");
    style.setLeftBorderColor("Black");
    style.setLeftBorderWidth("4");

    painter.paint(graphics, panel);

    assertEquals(4, graphics.drawnShapes.size());

    checkLine(graphics.drawnShapes.get(0), 1, Color.red, 7, 5, 104, 5);
    checkLine(graphics.drawnShapes.get(1), 2, Color.green, 104, 5, 104, 103);
    checkLine(graphics.drawnShapes.get(2), 3, Color.blue, 104, 103, 7, 103);
    checkLine(graphics.drawnShapes.get(3), 4, Color.black, 7, 103, 7, 5);
  }

  public void testRoundedCorders() throws Exception
  {
    style.setBorderWidth("2");
    style.setBorderColor("blue");
    style.setRoundedCornerRadius("10");

    painter.paint(graphics, panel);

    assertEquals(8, graphics.drawnShapes.size());
    assertEquals(Line2D.Double.class, graphics.drawnShape(0).shape.getClass());
    checkArc(graphics.drawnShape(1), 2, Color.blue);
    assertEquals(Line2D.Double.class, graphics.drawnShape(2).shape.getClass());
    checkArc(graphics.drawnShape(3), 2, Color.blue);
    assertEquals(Line2D.Double.class, graphics.drawnShape(4).shape.getClass());
    checkArc(graphics.drawnShape(5), 2, Color.blue);
    assertEquals(Line2D.Double.class, graphics.drawnShape(6).shape.getClass());
    checkArc(graphics.drawnShape(7), 2, Color.blue);
  }

  private void checkLine(MockGraphics.DrawnShape shape, int width, Color color, int x1, int y1, int x2, int y2)
  {
    assertEquals(width, (int)shape.stroke.getLineWidth());
    assertEquals(color, shape.color);
    assertEquals(false, shape.antialiasing);
    Line2D line = (Line2D)shape.shape;
    assertEquals(x1, (int)line.getX1());
    assertEquals(y1, (int)line.getY1());
    assertEquals(x2, (int)line.getX2());
    assertEquals(y2, (int)line.getY2());
  }

  private void checkArc(MockGraphics.DrawnShape shape, int width, Color color)
  {
    assertEquals(width, (int)shape.stroke.getLineWidth());
    assertEquals(color, shape.color);
    assertEquals(true, shape.antialiasing);
    assertEquals(Arc2D.Double.class, shape.shape.getClass());
  }
}

