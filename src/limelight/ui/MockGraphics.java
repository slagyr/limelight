package limelight.ui;

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
	public LinkedList<DrawnShape> drawnShapes;
	public LinkedList<DrawnShape> filledShapes;
  private BasicStroke stroke;
  private Hashtable<Object, Object> hints;
  private Rectangle clip;
  private Paint paint;

  public class DrawnShape
	{
		public Color color;
		public Shape shape;
		public BasicStroke stroke;
    public boolean antialiasing;
    public Paint paint;

    public DrawnShape(Shape shape, BasicStroke stroke, Color color, boolean antialiasing)
		{
			this.shape = shape;
			this.stroke = stroke;
			this.color = color;
      this.antialiasing = antialiasing;
    }
	}

	public MockGraphics()
	{
		drawnShapes = new LinkedList<DrawnShape>();
		filledShapes = new LinkedList<DrawnShape>();
    hints = new Hashtable<Object, Object>();
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

  public boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver)
	{
		return false;
	}

	public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i, int i1)
	{
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
	}

	public void setPaint(Paint paint)
  {
    this.paint = paint;
  }

	public void setStroke(Stroke stroke)
	{
		this.stroke = (BasicStroke)stroke;
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
		return null;
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
		return null;
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

	public limelight.ui.Rectangle getClipBounds()
	{
		return clip;
	}

	public void clipRect(int i, int i1, int i2, int i3)
	{
	}

	public void setClip(int i, int i1, int i2, int i3)
	{
    clip = new Rectangle(i, i1, i2, i3);
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

	public void drawLine(int i, int i1, int i2, int i3)
	{
	}

	public void fillRect(int i, int i1, int i2, int i3)
	{
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

	public boolean drawImage(Image image, int i, int i1, ImageObserver imageObserver)
	{
		return false;
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

	public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, ImageObserver imageObserver)
	{
		return false;
	}

	public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver)
	{
		return false;
	}

	public void dispose()
	{
	}
}
