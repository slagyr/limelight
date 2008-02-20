package limelight.ui;

import limelight.ui.Rectangle;

public class Aligner
{
	private Rectangle area;
	private String horizontalAlignment;
	private String verticalAlignment;
	private int consumedHeight;

	public Aligner(Rectangle area, String horizontalAlignment, String verticalAlignment)
	{
		this.area = area;    
    this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;  
    consumedHeight = 0;
	}

	public int startingX(double consumedWidth)
	{
    int diff = area.width - (int)(consumedWidth + 0.5);
    if("center".equals(horizontalAlignment))
			return area.x + diff/2;
		else if("right".equals(horizontalAlignment))
			return area.x + diff;
		else
			return area.x;
	}

	public int startingY()
	{
		int diff = area.height - consumedHeight;
		if("center".equals(verticalAlignment))
			return area.y + diff/2;
		else if("bottom".equals(verticalAlignment))
			return area.y + diff;
		else
			return area.y;
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
		consumedHeight += (int)(height + 0.5);
	}

  public Rectangle getArea()
  {
    return area;
  }

  public String getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public String getVerticalAlignment()
  {
    return verticalAlignment;
  }
}
