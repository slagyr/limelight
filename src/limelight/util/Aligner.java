//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class Aligner
{
  public static interface HorizontalAligner
  {
    int startingX(Box area, double consumedWidth);
  }

  public static interface VerticalAligner
  {
    int startingY(Box area, int consumedHeight);
  }

  public static VerticalAligner TOP = new VerticalAligner()
  {
    public int startingY(Box area, int consumedHeight)
    {
      return area.y;
    }
  };
  public static VerticalAligner VERTICAL_CENTER = new VerticalAligner()
  {
    public int startingY(Box area, int consumedHeight)
    {
      return area.y + ( (area.height - consumedHeight) / 2 );
    }
  };
  public static VerticalAligner BOTTOM = new VerticalAligner()
  {
    public int startingY(Box area, int consumedHeight)
    {
      return area.y + area.height - consumedHeight;
    }
  };
  public static HorizontalAligner LEFT = new HorizontalAligner(){

    public int startingX(Box area, double consumedWidth)
    {
      return area.x;
    }
  };
  public static HorizontalAligner HORIZONTAL_CENTER = new HorizontalAligner(){

    public int startingX(Box area, double consumedWidth)
    {
      return area.x + (area.width - (int) (consumedWidth + 0.5)) / 2;
    }
  };
  public static HorizontalAligner RIGHT = new HorizontalAligner(){

    public int startingX(Box area, double consumedWidth)
    {
      return area.x + (area.width - (int) (consumedWidth + 0.5));
    }
  };


  private Box area;
  private HorizontalAligner horizontalAlignment;
  private VerticalAligner verticalAlignment;
  private int consumedHeight;

  public Aligner(Box area, HorizontalAligner horizontalAlignment, VerticalAligner verticalAlignment)
  {
    this.area = area;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    consumedHeight = 0;
  }

  public int startingX(double consumedWidth)
  {
    return horizontalAlignment.startingX(area, consumedWidth);
  }

  public int startingY()
  {
    return verticalAlignment.startingY(area, consumedHeight);
  }

  public int getWidth()
  {
    return area.width;
  }

  public int getConsumedHeight()
  {
    return consumedHeight;
  }

  public void addConsumedHeight(double height)
  {
    consumedHeight += (int) (height + 0.5);
  }

  public Box getArea()
  {
    return area;
  }
}
