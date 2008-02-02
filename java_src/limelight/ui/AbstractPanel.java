package limelight.ui;

import java.awt.*;

public abstract class AbstractPanel
{
  protected int height;
  protected int width;
  private int x;
  private int y;
  private Panel parent;

  public AbstractPanel()
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

  public Panel getParent()
  {
    return parent;
  }

  public void setParent(Panel parent)
  {
    this.parent = parent;
  }

  public Frame getFrame()
  {
    return getParent().getFrame();
  }

  public abstract void paint(Graphics2D graphics);

  public abstract void snapToSize();
}
