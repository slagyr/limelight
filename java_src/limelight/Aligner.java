package limelight;

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

	public int startingX(double usedWidth)
	{
System.err.println("area.width = " + area.width);    
    int diff = area.width - (int)(usedWidth + 0.5);
System.err.println("diff = " + diff);    
    if("center".equals(horizontalAlignment))
			return diff/2;
		else if("right".equals(horizontalAlignment))
			return diff;
		else
			return area.x;
	}

	public int startingY()
	{
		int diff = area.height - consumedHeight;
		if("center".equals(verticalAlignment))
			return diff/2;
		else if("bottom".equals(verticalAlignment))
			return diff;
		else
			return area.y;
	}

	public int getWidth()
	{
		return area.width;
	}

	public void addConsumedHeight(double height)
	{
		consumedHeight += (int)(height + 0.5);
	}
}
