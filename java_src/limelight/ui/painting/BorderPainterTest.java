package limelight.ui.painting;

import junit.framework.TestCase;

import java.awt.*;
import java.awt.geom.Line2D;

import limelight.ui.*;

public class BorderPainterTest extends TestCase
{
  private MockProp prop;
  private MockPanel panel;
  private FlatStyle style;
  private BorderPainter painter;
  private limelight.ui.Rectangle border;
  private MockGraphics graphics;

  public void setUp() throws Exception
  {
    panel = new MockPanel();
    prop = (MockProp)panel.getProp();
    style = prop.style;
    border = new limelight.ui.Rectangle(5, 5, 100, 100);
    panel.rectangleInsideMargin = border;
    painter = new BorderPainter(panel);
    graphics = new MockGraphics();
  }

  public void testNoBorder() throws Exception
  {
    painter.paint(graphics);
    assertEquals(0, graphics.drawnShapes.size());
  }

  public void testBorderOnePixelWide() throws Exception
  {
    style.setBorderColor("blue");
    style.setBorderWidth("1");

    painter.paint(graphics);

    assertEquals(4, graphics.drawnShapes.size());

    checkLine(graphics.drawnShapes.get(0), 1, Color.blue, 5, 5, 104, 5);
    checkLine(graphics.drawnShapes.get(1), 1, Color.blue, 104, 5, 104, 104);
    checkLine(graphics.drawnShapes.get(2), 1, Color.blue, 104, 104, 5, 104);
    checkLine(graphics.drawnShapes.get(3), 1, Color.blue, 5, 104, 5, 5);
  }

  public void testBorderTwoPixelsWide() throws Exception
  {
    style.setBorderColor("blue");
    style.setBorderWidth("2");

    painter.paint(graphics);

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

    painter.paint(graphics);

    assertEquals(4, graphics.drawnShapes.size());

    checkLine(graphics.drawnShapes.get(0), 1, Color.red, 7, 5, 104, 5);
    checkLine(graphics.drawnShapes.get(1), 2, Color.green, 104, 5, 104, 103);
    checkLine(graphics.drawnShapes.get(2), 3, Color.blue, 104, 103, 7, 103);
    checkLine(graphics.drawnShapes.get(3), 4, Color.black, 7, 103, 7, 5);
  }

  private void checkLine(MockGraphics.DrawnShape shape, int width, Color color, int x1, int y1, int x2, int y2)
  {
    assertEquals(width, (int)shape.stroke.getLineWidth());
    assertEquals(color, shape.color);
    Line2D line = (Line2D)shape.shape;
    assertEquals(x1, (int)line.getX1());
    assertEquals(y1, (int)line.getY1());
    assertEquals(x2, (int)line.getX2());
    assertEquals(y2, (int)line.getY2());
  }
  
}
