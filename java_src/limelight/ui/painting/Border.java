package limelight.ui.painting;

import limelight.ui.Rectangle;
import limelight.ui.Style;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Border
{
  private Style style;
  private int topWidth;
  private int rightWidth;
  private int bottomWidth;
  private int leftWidth;
  private int topRightWidth;
  private int bottomRightWidth;
  private int bottomLeftWidth;
  private int topLeftWidth;
  private int topRightRadius;
  private int bottomRightRadius;
  private int bottomLeftRadius;
  private int topLeftRadius;
  private Rectangle bounds;
  private Jig jig;

  public Border(Style style, Rectangle bounds) {
    this.style = style;
    this.bounds = bounds;
    
    topWidth = style.asInt(style.getTopBorderWidth());
    rightWidth = style.asInt(style.getRightBorderWidth());
    bottomWidth = style.asInt(style.getBottomBorderWidth());
    leftWidth = style.asInt(style.getLeftBorderWidth());

    topRightWidth = style.asInt(style.getTopRightBorderWidth());
    topRightRadius = style.asInt(style.getTopRightRoundedCornerRadius());
    bottomRightWidth = style.asInt(style.getBottomRightBorderWidth());
    bottomRightRadius = style.asInt(style.getBottomRightRoundedCornerRadius());
    bottomLeftWidth = style.asInt(style.getBottomLeftBorderWidth());
    bottomLeftRadius = style.asInt(style.getBottomLeftRoundedCornerRadius());
    topLeftWidth = style.asInt(style.getTopLeftBorderWidth());
    topLeftRadius = style.asInt(style.getTopLeftRoundedCornerRadius());

    jig = new CenterJig(this);
  }

  public void setBounds(Rectangle bounds)
  {
    this.bounds = bounds;
  }

  public Style getStyle() {
    return style;
  }

  public int getTopWidth()
  {
    return topWidth;
  }

  public int getRightWidth()
  {
    return rightWidth;
  }

  public int getBottomWidth()
  {
    return bottomWidth;
  }

  public int getLeftWidth()
  {
    return leftWidth;
  }

  public int getTopRightWidth()
  {
    return topRightWidth;
  }

  public int getBottomRightWidth()
  {
    return bottomRightWidth;
  }

  public int getBottomLeftWidth()
  {
    return bottomLeftWidth;
  }

  public int getTopLeftWidth()
  {
    return topLeftWidth;
  }

  public int getTopRightRadius()
  {
    return topRightRadius;
  }

  public int getBottomRightRadius()
  {
    return bottomRightRadius;
  }

  public int getBottomLeftRadius()
  {
    return bottomLeftRadius;
  }

  public int getTopLeftRadius()
  {
    return topLeftRadius;
  }

  public Rectangle getBounds()
  {
    return bounds;
  }

  public boolean hasTopBorder()
  {
    return topWidth > 0;
  }

  public boolean hasRightBorder()
  {
    return rightWidth > 0;
  }

  public boolean hasBottomBorder()
  {
    return bottomWidth > 0;
  }

  public boolean hasLeftBorder()
  {
    return leftWidth > 0;
  }

  public boolean hasTopRightCorner()
  {
    return topRightWidth > 0 && topRightRadius > 0;
  }

  public boolean hasBottomRightCorner()
  {
    return bottomRightWidth > 0 && bottomRightRadius > 0;
  }

  public boolean hasBottomLeftCorner()
  {
    return bottomLeftWidth > 0 && bottomLeftRadius > 0;
  }

  public boolean hasTopLeftCorner()
  {
    return topLeftWidth > 0 && topLeftRadius > 0;
  }

  public Line2D getTopLine()
  {
    return jig.getTopLine();
  }

  public Line2D getRightLine()
  {
    return jig.getRightLine();
  }

  public Line2D getBottomLine()
  {
    return jig.getBottomLine();
  }

  public Line2D getLeftLine()
  {
    return jig.getLeftLine();
  }

  public Arc2D getTopRightArc()
  {
    return jig.getTopRightArc();
  }

  public Arc2D getBottomRightArc()
  {
    return jig.getBottomRightArc();
  }

  public Arc2D getBottomLeftArc()
  {
    return jig.getBottomLeftArc();
  }

  public Arc2D getTopLeftArc()
  {
    return jig.getTopLeftArc();
  }

  public Shape getShapeInsideBorder()
  {
    return new BorderShape(this);
  }

  public void updateDimentions()
  {    
    if(style.changed(Style.TOP_BORDER_WIDTH))
      topWidth = style.asInt(style.getTopBorderWidth());
    if(style.changed(Style.RIGHT_BORDER_WIDTH))
      rightWidth = style.asInt(style.getRightBorderWidth());
    if(style.changed(Style.BOTTOM_BORDER_WIDTH))
      bottomWidth = style.asInt(style.getBottomBorderWidth());
    if(style.changed(Style.LEFT_BORDER_WIDTH))
      leftWidth = style.asInt(style.getLeftBorderWidth());
    if(style.changed(Style.TOP_RIGHT_BORDER_WIDTH))
      topRightWidth = style.asInt(style.getTopRightBorderWidth());
    if(style.changed(Style.BOTTOM_RIGHT_BORDER_WIDTH))
      bottomRightWidth = style.asInt(style.getBottomRightBorderWidth());
    if(style.changed(Style.BOTTOM_LEFT_BORDER_WIDTH))
      bottomLeftWidth = style.asInt(style.getBottomLeftBorderWidth());
    if(style.changed(Style.TOP_LEFT_BORDER_WIDTH))
      topLeftWidth = style.asInt(style.getTopLeftBorderWidth());
    if(style.changed(Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS))
      topRightRadius = style.asInt(style.getTopRightRoundedCornerRadius());
    if(style.changed(Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS))
      bottomRightRadius = style.asInt(style.getBottomRightRoundedCornerRadius());
    if(style.changed(Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS))
      bottomLeftRadius = style.asInt(style.getBottomLeftRoundedCornerRadius());
    if(style.changed(Style.TOP_LEFT_ROUNDED_CORNER_RADIUS))
      topLeftRadius = style.asInt(style.getTopLeftRoundedCornerRadius());
  }

  private abstract static class Jig
  {
    protected int left, right, top, bottom;
    protected Border border;

    public Jig(Border border)
    {
      this.border = border;
    }

    protected int shave(int width)
    {
      return width / 2;
    }

    protected int rightShave(int width)
    {
      return width / 2 + width % 2;
    }

    protected int bottomShave(int width)
    {
      return width / 2 + width % 2;
    }

    public Line2D getTopLine()
    {
      return new Line2D.Double(left + border.topLeftRadius, top, right - border.topRightRadius, top);
    }

    public Line2D getRightLine()
    {
      return new Line2D.Double(right, top + border.topRightRadius, right, bottom - border.bottomRightRadius);
    }

    public Line2D getBottomLine()
    {
      return new Line2D.Double(right - border.bottomRightRadius, bottom, left + border.bottomLeftRadius, bottom);
    }

    public Line2D getLeftLine()
    {
      return new Line2D.Double(left, bottom - border.bottomLeftRadius, left, top + border.topLeftRadius);
    }

    public abstract Arc2D getTopRightArc();
    public abstract Arc2D getBottomRightArc();
    public abstract Arc2D getBottomLeftArc();
    public abstract Arc2D getTopLeftArc();
  }


  //TODO MDM - There is some work to do regarding the angles of the arcs.  As the width of the border increases
  // the arcs overlap more and more.  This results in protrusions around the corners... with or without borders.
  // Based on the radius of the arc, and width of the border, we should be able to calculate a starting angle
  // and extent such that the arcs will not overlap.
  private static class CenterJig extends Jig
  {

    public CenterJig(Border border)
    {
      super(border);
      left = border.bounds.left() + shave(border.leftWidth);
      right = border.bounds.right() - rightShave(border.rightWidth) + 1;
      top = border.bounds.top() + shave(border.topWidth);
      bottom = border.bounds.bottom() - bottomShave(border.bottomWidth) + 1;
    }

    public Arc2D getTopRightArc()
    {
      int topRightArcWidth = border.topRightRadius * 2 - border.topRightWidth / 2;
      int x = border.bounds.right() - shave(border.topRightWidth) - topRightArcWidth;
      int y = border.bounds.top() + shave(border.topRightWidth);
      return new Arc2D.Double(x, y, topRightArcWidth, topRightArcWidth, 90, -90, Arc2D.OPEN);
    }

    public Arc2D getBottomRightArc()
    {
      int bottomRightArcWidth = border.bottomRightRadius * 2 - border.bottomRightWidth / 2;
      int x = border.bounds.right() - shave(border.bottomRightWidth) - bottomRightArcWidth;
      int y = border.bounds.bottom() - shave(border.bottomRightWidth) - bottomRightArcWidth;
      return new Arc2D.Double(x, y, bottomRightArcWidth, bottomRightArcWidth, 0, -90, Arc2D.OPEN);
    }

    public Arc2D getBottomLeftArc()                                                                                
    {
      int bottomLeftArcWidth = border.bottomLeftRadius * 2 - border.bottomLeftWidth / 2;
      int x = border.bounds.left() + shave(border.bottomLeftWidth);
      int y = border.bounds.bottom() - shave(border.bottomLeftWidth) - bottomLeftArcWidth;
      return new Arc2D.Double(x, y, bottomLeftArcWidth, bottomLeftArcWidth, 270, -90, Arc2D.OPEN);
    }

    public Arc2D getTopLeftArc()
    {
      int topLeftArcWidth = border.topLeftRadius * 2 - border.topLeftWidth / 2;
      int x = border.bounds.left() + shave(border.topLeftWidth);
      int y = border.bounds.top() + shave(border.topLeftWidth);
      return new Arc2D.Double(x, y, topLeftArcWidth, topLeftArcWidth, 180, -90, Arc2D.OPEN);
    }
  }

  private static class InsideJig extends Jig
  {
    public InsideJig(Border border)
    {
      super(border);
      left = border.bounds.left() + border.leftWidth;
      right = border.bounds.right() - border.rightWidth + 1;
      top = border.bounds.top() + border.topWidth;
      bottom = border.bounds.bottom() - border.bottomWidth + 1;
    }

    public Arc2D getTopRightArc()
    {
      int topRightArcWidth = border.topRightRadius * 2 - border.topRightWidth;
      int x = border.bounds.right() - border.topRightWidth - topRightArcWidth + 1;
      int y = border.bounds.top() + border.topRightWidth;
      return new Arc2D.Double(x, y, topRightArcWidth, topRightArcWidth, 90, -90, Arc2D.OPEN);
    }

    public Arc2D getBottomRightArc()
    {
      int bottomRightArcWidth = border.bottomRightRadius * 2 - border.bottomRightWidth;
      double x = border.bounds.right() - border.bottomRightWidth - bottomRightArcWidth + 1;
      double y = border.bounds.bottom() - border.bottomRightWidth - bottomRightArcWidth + 1;
      return new Arc2D.Double(x, y, bottomRightArcWidth, bottomRightArcWidth, 0, -90, Arc2D.OPEN);
    }

    public Arc2D getBottomLeftArc()
    {
      int bottomLeftArcWidth = border.bottomLeftRadius * 2 - border.bottomLeftWidth;
      int x = border.bounds.left() + border.bottomLeftWidth;
      int y = border.bounds.bottom() - border.bottomLeftWidth - bottomLeftArcWidth + 1;
      return new Arc2D.Double(x, y, bottomLeftArcWidth, bottomLeftArcWidth, 270, -90, Arc2D.OPEN);
    }

    public Arc2D getTopLeftArc()
    {
      int topLeftArcWidth = border.topLeftRadius * 2 - border.topLeftWidth;
      int x = border.bounds.left() + border.topLeftWidth;
      int y = border.bounds.top() + border.topLeftWidth;
      return new Arc2D.Double(x, y, topLeftArcWidth, topLeftArcWidth, 180, -90, Arc2D.OPEN);
    }
  }

  private class BorderShape implements Shape
  {
    private Border border;

    public BorderShape(Border border)
    {
      this.border = border;
      jig = new InsideJig(border);
    }

    public java.awt.Rectangle getBounds()
    {
      return border.bounds;
    }

    public Rectangle2D getBounds2D()
    {
      return border.bounds;
    }

    public boolean contains(double x, double y)
    {
      return border.bounds.contains(x, y);
    }

    public boolean contains(Point2D p)
    {
      return border.bounds.contains(p);
    }

    public boolean intersects(double x, double y, double w, double h)
    {
      return border.bounds.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r)
    {
      return border.bounds.intersects(r);
    }

    public boolean contains(double x, double y, double w, double h)
    {
      return border.bounds.contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r)
    {
      return border.bounds.contains(r);
    }

    public PathIterator getPathIterator(AffineTransform at)
    {
      return new BorderShapePathIterator(border, jig, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
      return getPathIterator(at);
    }
  }

  private static class BorderShapePathIterator implements PathIterator
  {
    private ArrayList<PathSegment> segments;
    private int index;

    public BorderShapePathIterator(Border border, Jig jig, AffineTransform at)
    {
      buildSegments(border, jig, at);
      index = 0;
    }

    private void buildSegments(Border border, Jig jig, AffineTransform at)
    {
      segments = new ArrayList<PathSegment>();
      double[] coords = new double[6];

      PathIterator topIterator = jig.getTopLine().getPathIterator(at);
      topIterator.currentSegment(coords);
      MovePathSegment start = new MovePathSegment(coords);
      segments.add(start);
      topIterator.next();
      topIterator.currentSegment(coords);
      segments.add(new LinePathSegment(coords));

      if(border.topRightRadius > 0)
        addArc(at, segments, coords, jig.getTopRightArc());
      addLine(at, segments, coords, jig.getRightLine());
      if(border.bottomRightRadius > 0)
        addArc(at, segments, coords, jig.getBottomRightArc());
      addLine(at, segments, coords, jig.getBottomLine());
      if(border.bottomLeftRadius > 0)
        addArc(at, segments, coords, jig.getBottomLeftArc());
      addLine(at, segments, coords, jig.getLeftLine());
      if(border.topLeftRadius > 0)
        addArc(at, segments, coords, jig.getTopLeftArc());

      start.dump(coords);
      segments.add(new LinePathSegment(coords));
    }

    private void addLine(AffineTransform at, ArrayList<PathSegment> segments, double[] coords, Line2D line)
    {
      PathIterator iterator = line.getPathIterator(at);
      iterator.currentSegment(coords);
      segments.add(new LinePathSegment(coords));
      iterator.next();
      iterator.currentSegment(coords);
      segments.add(new LinePathSegment(coords));
    }

    private void addArc(AffineTransform at, ArrayList<PathSegment> segments, double[] coords, Arc2D arc)
    {
      PathIterator iterator;
      iterator = arc.getPathIterator(at);
      iterator.currentSegment(coords); 
      segments.add(new LinePathSegment(coords));
      iterator.next();
      iterator.currentSegment(coords);
      segments.add(new CubicPathSegment(coords));
    }

    public int getWindingRule()
    {
      return PathIterator.WIND_EVEN_ODD;
    }

    public boolean isDone()
    {
      return index >= segments.size();
    }

    public void next()
    {
      index++;
    }

    public int currentSegment(float[] coords)
    {
      PathSegment segment = segments.get(index);
      segment.dump(coords);
      return segment.type();
    }

    public int currentSegment(double[] coords)
    {
      PathSegment segment = segments.get(index);
      segment.dump(coords);
      return segment.type();
    }
  }

  private abstract static class PathSegment
  {
    public abstract int type();
    public abstract void dump(double[] coords);
    public abstract void dump(float[] coords);
  }

  private static class MovePathSegment extends PathSegment
  {
    private double d1, d2;

    public MovePathSegment(double[] coords)
    {
      d1 = coords[0];
      d2 = coords[1];
    }

    public int type()
    {
      return PathIterator.SEG_MOVETO;
    }

    public void dump(double[] coords)
    {
      coords[0] = d1;
      coords[1] = d2;
    }

    public void dump(float[] coords)
    {
      coords[0] = (float)d1;
      coords[1] = (float)d2;
    }
  }

  private static class LinePathSegment extends MovePathSegment
  {
    public LinePathSegment(double[] coords)
    {
      super(coords);
    }

    public int type()
    {
      return PathIterator.SEG_LINETO;
    }
  }

  private static class CubicPathSegment extends PathSegment
  {
    private double d1, d2, d3, d4, d5, d6;

    public CubicPathSegment(double[] coords)
    {
      d1 = coords[0];
      d2 = coords[1];
      d3 = coords[2];
      d4 = coords[3];
      d5 = coords[4];
      d6 = coords[5];
    }

    public int type()
    {
      return PathIterator.SEG_CUBICTO;
    }

    public void dump(double[] coords)
    {
      coords[0] = d1;
      coords[1] = d2;
      coords[2] = d3;
      coords[3] = d4;
      coords[4] = d5;
      coords[5] = d6;
    }

    public void dump(float[] coords)
    {
      coords[0] = (float)d1;
      coords[1] = (float)d2;
      coords[2] = (float)d3;
      coords[3] = (float)d4;
      coords[4] = (float)d5;
      coords[5] = (float)d6;
    }
  }
}
