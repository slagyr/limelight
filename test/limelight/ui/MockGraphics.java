//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.util.Box;

import java.awt.*;
import java.awt.font.*;
import java.awt.image.*;
import java.awt.image.renderable.RenderableImage;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.util.*;

public class MockGraphics extends java.awt.Graphics2D
{
  public Color color;
  public final LinkedList<DrawnShape> drawnShapes;
  public final LinkedList<DrawnShape> filledShapes;
  public final LinkedList<DrawnLine> drawnLines;
  public ArrayList<DrawnImage> drawnImages = new ArrayList<DrawnImage>();
  public BasicStroke stroke;
  public final Hashtable<Object, Object> hints;
  public Box clip;
  public Paint paint;
  public final LinkedList<MockGraphics> subGraphics = new LinkedList<MockGraphics>();
  public Box clippedRectangle;
  private Composite composite;

  public class DrawnShape
  {
    public final Color color;
    public final Shape shape;
    public final BasicStroke stroke;
    public final boolean antialiasing;
    public Paint paint;

    public DrawnShape(Shape shape, BasicStroke stroke, Color color, boolean antialiasing)
    {
      this.shape = shape;
      this.stroke = stroke;
      this.color = color;
      this.antialiasing = antialiasing;
    }
  }

  public class DrawnImage
  {
    public final Image image;
    public int x;
    public int y;
    public Rectangle destination;
    public Rectangle source;

    public DrawnImage(Image image, int x, int y)
    {
      this.image = image;
      this.x = x;
      this.y = y;
    }

    public DrawnImage(Image image, Rectangle destination, Rectangle source)
    {
      this.image = image;
      this.destination = destination;
      this.source = source;
    }
  }

  public class DrawnLine
  {
    public int x1, y1, x2, y2;

    public DrawnLine(int x1, int y1, int x2, int y2)
    {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }
  }

  public MockGraphics()
  {
    drawnShapes = new LinkedList<DrawnShape>();
    filledShapes = new LinkedList<DrawnShape>();
    hints = new Hashtable<Object, Object>();
    drawnLines = new LinkedList<DrawnLine>();
  }

  public DrawnShape drawnShape(int i)
  {
    return drawnShapes.get(i);
  }

  public void draw(Shape shape)
  {
    drawnShapes.add(new DrawnShape(shape, stroke, color, antialiasing()));
  }

  private boolean antialiasing()
  {
    return hints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
  }

  public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs)
  {
    return false;
  }

  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y)
  {
  }

  public boolean drawImage(Image image, int x, int y, ImageObserver imageObserver)
  {
    drawnImages.add(new DrawnImage(image, x, y));
    return true;
  }

  public boolean drawImage(Image image, int i, int i1, int i2, int i3, ImageObserver imageObserver)
  {
    return false;
  }

  public boolean drawImage(Image image, int i, int i1, Color color, ImageObserver imageObserver)
  {
    return false;
  }

  public boolean drawImage(Image image, int i, int i1, int i2, int i3, Color color, ImageObserver imageObserver)
  {
    return false;
  }

  public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver imageObserver)
  {
    drawnImages.add(new DrawnImage(image, new Rectangle(dx1, dy1, dx2 - dx1, dy2 - dy1), new Rectangle(sx1, sy1, sx2 - sx1, sy2 - sy1)));
    return true;
  }

  public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver)
  {
    return false;
  }

  public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform)
  {
  }

  public void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform)
  {
  }

  public void drawString(String string, int i, int i1)
  {
  }

  public void drawString(String string, float v, float v1)
  {
  }

  public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i, int i1)
  {
  }

  public void drawString(AttributedCharacterIterator attributedCharacterIterator, float v, float v1)
  {
  }

  public void drawGlyphVector(GlyphVector glyphVector, float v, float v1)
  {
  }

  public void fill(Shape shape)
  {
    DrawnShape theShape = new DrawnShape(shape, stroke, color, antialiasing());
    theShape.paint = paint;
    filledShapes.add(theShape);
  }

  public boolean hit(java.awt.Rectangle rectangle, Shape shape, boolean b)
  {
    return false;
  }

  public GraphicsConfiguration getDeviceConfiguration()
  {
    return null;
  }

  public void setComposite(Composite composite)
  {
    this.composite = composite;
  }

  public void setPaint(Paint paint)
  {
    this.paint = paint;
  }

  public void setStroke(Stroke stroke)
  {
    this.stroke = (BasicStroke) stroke;
  }

  public void setRenderingHint(RenderingHints.Key key, Object object)
  {
    hints.put(key, object);
  }

  public Object getRenderingHint(RenderingHints.Key key)
  {
    return hints.get(key);
  }

  public void setRenderingHints(Map<?, ?> map)
  {
  }

  public void addRenderingHints(Map<?, ?> map)
  {
  }

  public RenderingHints getRenderingHints()
  {
    return null;
  }

  public void translate(int i, int i1)
  {
  }

  public void translate(double v, double v1)
  {
  }

  public void rotate(double v)
  {
  }

  public void rotate(double v, double v1, double v2)
  {
  }

  public void scale(double v, double v1)
  {
  }

  public void shear(double v, double v1)
  {
  }

  public void transform(AffineTransform affineTransform)
  {
  }

  public void setTransform(AffineTransform affineTransform)
  {
  }

  public AffineTransform getTransform()
  {
    return null;
  }

  public Paint getPaint()
  {
    return null;
  }

  public Composite getComposite()
  {
    return composite;
  }

  public void setBackground(Color color)
  {
  }

  public Color getBackground()
  {
    return null;
  }

  public Stroke getStroke()
  {
    return stroke;
  }

  public void clip(Shape shape)
  {
  }

  public FontRenderContext getFontRenderContext()
  {
    return null;
  }

  public Graphics create()
  {
    MockGraphics createdGraphics = new MockGraphics();
    subGraphics.add(createdGraphics);
    return createdGraphics;
  }

  public Graphics create(int x, int y, int width, int height)
  {
    MockGraphics createdGraphics = new MockGraphics();
    subGraphics.add(createdGraphics);
    createdGraphics.clip = new Box(x, y, width, height);
    return createdGraphics;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public void setPaintMode()
  {
  }

  public void setXORMode(Color color)
  {
  }

  public Font getFont()
  {
    return null;
  }

  public void setFont(Font font)
  {
  }

  public FontMetrics getFontMetrics(Font font)
  {
    return null;
  }

  public Box getClipBounds()
  {
    return clip;
  }

  public void clipRect(int x, int y, int width, int height)
  {
    clippedRectangle = new Box(x, y, width, height);
    clip = clippedRectangle;
  }

  public void setClip(int i, int i1, int i2, int i3)
  {
    clip = new Box(i, i1, i2, i3);
  }

  public Shape getClip()
  {
    return null;
  }

  public void setClip(Shape shape)
  {
  }

  public void copyArea(int i, int i1, int i2, int i3, int i4, int i5)
  {
  }

  public void drawLine(int x1, int y1, int x2, int y2)
  {
    drawnLines.add(new DrawnLine(x1, y1, x2, y2));
  }

  public void fillRect(int x1, int y1, int x2, int y2)
  {
    DrawnShape theShape = new DrawnShape(new Rectangle(x1, y1, x2, y2), stroke, color, antialiasing());
    theShape.paint = paint;
    filledShapes.add(theShape);
  }

  public void drawRect(int x1, int y1, int x2, int y2)
  {
    DrawnShape theShape = new DrawnShape(new Rectangle(x1, y1, x2, y2), stroke, color, antialiasing());
    theShape.paint = paint;
    drawnShapes.add(theShape);
  }

  public void clearRect(int i, int i1, int i2, int i3)
  {
  }

  public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5)
  {
  }

  public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5)
  {
  }

  public void drawOval(int i, int i1, int i2, int i3)
  {
  }

  public void fillOval(int i, int i1, int i2, int i3)
  {
  }

  public void drawArc(int i, int i1, int i2, int i3, int i4, int i5)
  {
  }

  public void fillArc(int i, int i1, int i2, int i3, int i4, int i5)
  {
  }

  public void drawPolyline(int[] ints, int[] ints1, int i)
  {
  }

  public void drawPolygon(int[] ints, int[] ints1, int i)
  {
  }

  public void fillPolygon(int[] ints, int[] ints1, int i)
  {
  }

  public void dispose()
  {
  }


}
