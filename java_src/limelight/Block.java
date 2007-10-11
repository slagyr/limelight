package limelight;

import java.awt.*;

public class Block
{
	private Panel panel;

	private String text;
	private String width;
	private String height;
	private Color topBorderColor;
	private Color rightBorderColor;
	private Color bottomBorderColor;
	private Color leftBorderColor;
	private int topBorderWidth;
	private int rightBorderWidth;
	private int bottomBorderWidth;
	private int leftBorderWidth;
	private int topMargin;
	private int rightMargin;
	private int bottomMargin;
	private int leftMargin;
	private int topPadding;
	private int rightPadding;
	private int bottomPadding;
	private int leftPadding;
	private Color backgroundColor;
	private String backgroundImage;
	private String backgroundImageFillStrategy;
	private String horizontalAlignment;
	private String verticalAlignment;
	private Color textColor;
	private String fontFace;
	private int fontSize;
	private String fontStyle;


	public Block()
	{
		panel = new Panel(this);
		setBorderColor("white");
		setBackgroundColor(Color.white);
		backgroundImageFillStrategy = "repeat";
	}

	public void add(Block child)
	{
		panel.add(child.getPanel());
	}

	public Panel getPanel()
	{
		return panel;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String value)
	{
		text = value;
	}

	public void setX(int x)
	{
		panel.setLocation(x, panel.getLocation().y);
	}

	public void setY(int y)
	{
		panel.setLocation(panel.getLocation().x, y);
	}

	public void setWidth(String value)
	{
		width = value;
	}

	public String getWidth()
	{
		return width;
	}

	public void setHeight(String value)
	{
		height = value;
	}

	public String getHeight()
	{
		return height;
	}

	public void setTextColor(String value)
	{
		textColor = Colors.resolve(value);
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public void setBorderColor(String value)
	{
		Color color = Colors.resolve(value);
		topBorderColor = color;
		rightBorderColor = color;
		bottomBorderColor = color;
		leftBorderColor = color;
	}

	public Color getTopBorderColor()
	{
		return topBorderColor;
	}

	public Color getRightBorderColor()
	{
		return rightBorderColor;
	}

	public Color getBottomBorderColor()
	{
		return bottomBorderColor;
	}

	public Color getLeftBorderColor()
	{
		return leftBorderColor;
	}

	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setTopBorderColor(String value)
	{
		topBorderColor = Colors.resolve(value);
	}

	public void setRightBorderColor(String value)
	{
		rightBorderColor = Colors.resolve(value);
	}

	public void setBottomBorderColor(String value)
	{
		bottomBorderColor = Colors.resolve(value);
	}

	public void setLeftBorderColor(String value)
	{
		leftBorderColor = Colors.resolve(value);
	}

	public void setBorderWidth(int pixels)
	{
		topBorderWidth = pixels;
		rightBorderWidth = pixels;
		bottomBorderWidth = pixels;
		leftBorderWidth = pixels;
	}

	public int getTopBorderWidth()
	{
		return topBorderWidth;
	}

	public void setTopBorderWidth(int topBorderWidth)
	{
		this.topBorderWidth = topBorderWidth;
	}

	public int getRightBorderWidth()
	{
		return rightBorderWidth;
	}

	public void setRightBorderWidth(int rightBorderWidth)
	{
		this.rightBorderWidth = rightBorderWidth;
	}

	public int getBottomBorderWidth()
	{
		return bottomBorderWidth;
	}

	public void setBottomBorderWidth(int bottomBorderWidth)
	{
		this.bottomBorderWidth = bottomBorderWidth;
	}

	public int getLeftBorderWidth()
	{
		return leftBorderWidth;
	}

	public void setLeftBorderWidth(int leftBorderWidth)
	{
		this.leftBorderWidth = leftBorderWidth;
	}

	public void setMargin(int margin)
	{
		topMargin = margin;
		rightMargin = margin;
		bottomMargin = margin;
		leftMargin = margin;
	}

	public int getTopMargin()
	{
		return topMargin;
	}

	public void setTopMargin(int topMargin)
	{
		this.topMargin = topMargin;
	}

	public int getRightMargin()
	{
		return rightMargin;
	}

	public void setRightMargin(int rightMargin)
	{
		this.rightMargin = rightMargin;
	}

	public int getBottomMargin()
	{
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin)
	{
		this.bottomMargin = bottomMargin;
	}

	public int getLeftMargin()
	{
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin)
	{
		this.leftMargin = leftMargin;
	}

	public void setPadding(int padding)
	{
		topPadding = padding;
		rightPadding = padding;
		bottomPadding = padding;
		leftPadding = padding;
	}

	public int getTopPadding()
	{
		return topPadding;
	}

	public void setTopPadding(int topPadding)
	{
		this.topPadding = topPadding;
	}

	public int getRightPadding()
	{
		return rightPadding;
	}

	public void setRightPadding(int rightPadding)
	{
		this.rightPadding = rightPadding;
	}

	public int getBottomPadding()
	{
		return bottomPadding;
	}

	public void setBottomPadding(int bottomPadding)
	{
		this.bottomPadding = bottomPadding;
	}

	public int getLeftPadding()
	{
		return leftPadding;
	}

	public void setLeftPadding(int leftPadding)
	{
		this.leftPadding = leftPadding;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public void setBackgroundImage(String backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}

	public String getBackgroundImage()
	{
		return backgroundImage;
	}

	public String getBackgroundImageFillStrategy()
	{
		return backgroundImageFillStrategy;
	}

	public void setBackgroundImageFillStrategy(String backgroundImageFillStrategy)
	{
		this.backgroundImageFillStrategy = backgroundImageFillStrategy;
	}

	public void setHorizontalAlignment(String alignment)
	{
		horizontalAlignment = alignment;
	}

	public String getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	public void setVerticalAlignment(String alignment)
	{
		verticalAlignment = alignment;
	}

	public String getVerticalAlignment()
	{
		return verticalAlignment;
	}

	public String getFontFace()
	{
		return fontFace;
	}

	public void setFontFace(String fontFace)
	{
		this.fontFace = fontFace;
	}

	public int getFontSize()
	{
		return fontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}

	public String getFontStyle()
	{
		return fontStyle;
	}

	public void setFontStyle(String fontStyle)
	{
		this.fontStyle = fontStyle;
	}

	public Rectangle getRectangle()
	{
		return new Rectangle(0, 0, panel.getWidth(), panel.getHeight());	
	}

	public Rectangle getRectangleInsideMargins()
	{
		Rectangle r = getRectangle();
		r.shave(topMargin, rightMargin, bottomMargin, leftMargin);
		return r;
	}

	public Rectangle getRectangleInsideBorders()
	{
		Rectangle r = getRectangleInsideMargins();
		r.shave(topBorderWidth, rightBorderWidth, bottomBorderWidth, leftBorderWidth);
		return r;
	}

	public Rectangle getRectangleInsidePadding()
	{
		Rectangle r = getRectangleInsideBorders();
		r.shave(topPadding, rightPadding, bottomPadding, leftPadding);
		return r;
	}
}
