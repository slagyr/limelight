package limelight.ui;

import limelight.LimelightError;
import limelight.LimelightException;

import java.awt.*;

public abstract class Panel
{
  protected int height;
  protected int width;
  private int x;
  private int y;
  private BlockPanel parent;

  public Panel()
  {
    width = 50;
    height = 50;
  }

  public int getHeight()
  {
    return height;
  }

  public int getWidth()
  {
    return width;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public void setSize(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public void setWidth(int width)
  {
    this.width = width;
  }

  public void setHeight(int height)
  {
    this.height = height;
  }

  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void setX(int x)
  {
    this.x = x;
  }

  public void setY(int y)
  {
    this.y = y;
  }

  public limelight.Rectangle getInternalRectangle()
  {
    return new limelight.Rectangle(0, 0, getWidth(), getHeight());
  }

  public limelight.Rectangle getExternalRectangle()
  {
    return new limelight.Rectangle(x, y, getWidth(), getHeight());
  }

  public BlockPanel getParent()
  {
    return parent;
  }

  public void setParent(BlockPanel parent)
  {
    this.parent = parent;
  }

  public Block getBlock()
  {
    return parent.getBlock();
  }

  public Frame getFrame()
  {
    return getParent().getFrame();
  }

  public boolean containsPoint(Point point)
  {
    return  point.x >= x &&
            point.x < x + width &&
            point.y >= y &&
            point.y < y + height;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    return this;
  }

  public boolean isAncestor(Panel panel)
  {
    if(parent == null)
      return false;
    else if(parent == panel)
      return true;
    else
      return parent.isAncestor(panel);
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    Panel ancestor = getParent();
    while(ancestor != null && !panel.isAncestor(ancestor))
      ancestor = ancestor.getParent();

    if(ancestor == null)
      throw new LimelightError("No common ancestor found! Do the panels belong to the same tree?");

    return ancestor;
  }

  public Point getAbsoluteLocation()
  {
    int x = this.x;
    int y = this.y;

    Panel p = parent;
    while(p != null)
    {
      x += p.getX();
      y += p.getY();
      p = p.getParent();
    }

    return new Point(x, y);
  }

  public abstract void paint(java.awt.Rectangle clip);

  public abstract void snapToSize();
}
