package limelight.ui;

public class Rectangle extends java.awt.Rectangle
{
	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle(java.awt.Rectangle r)
	{
		x = r.x;
		y = r.y;
		width = r.width;
		height = r.height;
	}

	public void shave(int top, int right, int bottom, int left)
	{
		y = y + top;
		x = x + left;
		width = width - left - right;
		height = height - top - bottom;
	}

	public int top()
	{
		return y;
	}

	public int right()
	{
		return x + width;
	}

	public int bottom()
	{
		return y + height;
	}

	public int left()
	{
		return x;
	}

	public String toString()
	{
		return "Rectangle: x: " + x + ",  y: " + y + ", width: " + width + ", height: " + height;
	}
}
