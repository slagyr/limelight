package limelight.ui;

import limelight.LimelightError;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class Panel
{
  protected int height;
  protected int width;
  private int x;
  private int y;
  private ParentPanel parent;
  private Point absoluteLocation;
  private Rectangle absoluteBounds;

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

  public Point getLocation()
  {
    return new Point(x, y);
  }

  public Point getAbsoluteLocation()
  {
    if(absoluteLocation == null)
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
      absoluteLocation = new Point(x, y);
    }
    return absoluteLocation;
  }

  public Rectangle getInternalRectangle()
  {
    return new Rectangle(0, 0, getWidth(), getHeight());
  }

  public Rectangle getBounds()
  {
    return new Rectangle(x, y, getWidth(), getHeight());
  }

  public Rectangle getAbsoluteBounds()
  {
    if(absoluteBounds == null)
    {
      Point absoluteLocation = getAbsoluteLocation();
      absoluteBounds = new Rectangle(absoluteLocation.x, absoluteLocation.y, getWidth(), getHeight());
    }
    return absoluteBounds;
  }

  public ParentPanel getParent()
  {
    return parent;
  }

  public void setParent(ParentPanel parent)
  {
    this.parent = parent;
  }

  public boolean hasChildren()
  {
    return false;
  }

  public LinkedList<Panel> getChildren()
  {
    return null;
  }

  public Block getBlock()
  {
    return parent.getBlock();
  }

  public Frame getFrame()
  {
    if(getParent() == null)
      System.err.println("Panel without parent = " + this);
    return getParent().getFrame();
  }

  public Graphics2D getClippedGraphics()
  {
    return (Graphics2D)parent.getClippedGraphics().create(x, y, width, height);
  }

  public boolean containsRelativePoint(Point point)
  {
    return point.x >= x &&
           point.x < x + width &&
           point.y >= y &&
           point.y < y + height;
  }

  public boolean containsAbsolutePoint(Point point)
  {
    Point absoluteLocation = getAbsoluteLocation();
    return point.x >= absoluteLocation.x &&
           point.x < absoluteLocation.x + width &&
           point.y >= absoluteLocation.y &&
           point.y < absoluteLocation.y + height;
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

  public boolean usesBuffer()
  {
    return false;
  }

  public BufferedImage getBuffer()
  {
    throw new LimelightError("Can't get buffer from a panel that doesn't use buffers");
  }

  public void doLayout()
  {
    absoluteLocation = null;
    absoluteBounds = null;

    snapToSize();
  }

  public void mousePressed(MouseEvent e)
  {
    getBlock().mouse_pressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    getBlock().mouse_released(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    getBlock().mouse_clicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    getBlock().mouse_dragged(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    getBlock().mouse_entered(e);
    getBlock().hover_on();
  }

  public void mouseExited(MouseEvent e)
  {
    getBlock().mouse_exited(e);
    getBlock().hover_off();
  }

  public void mouseMoved(MouseEvent e)
  {
    getBlock().mouse_moved(e);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    getParent().mouseWheelMoved(e);
  }

  public abstract void snapToSize();
  public abstract void paintOn(Graphics2D graphics);

}
