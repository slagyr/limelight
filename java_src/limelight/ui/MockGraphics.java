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
	private Color color;
	public LinkedList<DrawnShape> drawnShapes;
  private BasicStroke stroke;
  private Rectangle clip;
  public Rectangle drawnImageDestination;
  public Rectangle drawnImageSource;
  public Image drawnImage;
  public int drawnImageX;
  public int drawnImageY;

  public class DrawnShape
	{
		public Color color;
		public Shape shape;
		public BasicStroke stroke;

		public DrawnShape(Shape shape, BasicStroke stroke, Color color)
		{
			this.shape = shape;
			this.stroke = stroke;
			this.color = color;
		}
	}

	public MockGraphics()
	{
		drawnShapes = new LinkedList<DrawnShape>();
	}

	public DrawnShape drawnShape(int i)
	{
		return drawnShapes.get(i);
	}

	public void draw(Shape shape)
	{
		drawnShapes.add(new DrawnShape(shape, stroke, color));
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
	}

	public void setStroke(Stroke stroke)
	{
		this.stroke = (BasicStroke)stroke;
	}

	public void setRenderingHint(RenderingHints.Key key, Object object)
	{
	}

	public Object getRenderingHint(RenderingHints.Key key)
	{
		return null;
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
		return new MockGraphics();
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

	public Rectangle getClipBounds()
	{
		return clip;
	}

	public void clipRect(int x, int y, int width, int height)
	{
    clip = new Rectangle(x, y, width, height);
  }

	public void setClip(int x, int y, int width, int height)
	{
    clip = new Rectangle(x, y, width, height);
  }

	public Shape getClip()
	{
		return clip;
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

	public boolean drawImage(Image image, int x, int y, ImageObserver imageObserver)
	{
    drawnImage = image;
    drawnImageX = x;
    drawnImageY = y;
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
    drawnImage = image;
    drawnImageDestination = new Rectangle(dx1, dy1, dx2 - dx1, dy2 - dy1);
    drawnImageSource = new Rectangle(sx1, sy1, sx2 - sx1, sy2 - sy1);
    return true;
	}

	public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver)
	{
		return false;
	}

	public void dispose()
	{
	}
}
