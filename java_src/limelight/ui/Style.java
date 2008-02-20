package limelight.ui;

public abstract class Style
{
  public static final int WIDTH = 0;
  public static final int HEIGHT = 1;
  public static final int X_OFFSET = 2;
  public static final int Y_OFFSET = 3;
  public static final int TOP_BORDER_COLOR = 4;
  public static final int RIGHT_BORDER_COLOR = 5;
  public static final int BOTTOM_BORDER_COLOR = 6;
  public static final int LEFT_BORDER_COLOR = 7;
  public static final int TOP_BORDER_WIDTH = 8;
  public static final int RIGHT_BORDER_WIDTH = 9;
  public static final int BOTTOM_BORDER_WIDTH = 10;
  public static final int LEFT_BORDER_WIDTH = 11;
  public static final int TOP_MARGIN = 12;
  public static final int RIGHT_MARGIN = 13;
  public static final int BOTTOM_MARGIN = 14;
  public static final int LEFT_MARGIN = 15;
  public static final int TOP_PADDING = 16;
  public static final int RIGHT_PADDING = 17;
  public static final int BOTTOM_PADDING = 18;
  public static final int LEFT_PADDING = 19;
  public static final int BACKGROUND_COLOR = 20;
  public static final int SECONDARY_BACKGROUND_COLOR = 21;
  public static final int BACKGROUND_IMAGE = 22;
  public static final int BACKGROUND_IMAGE_FILL_STRATEGY = 23;
  public static final int GRADIENT_ANGLE = 24;
  public static final int GRADIENT_PENETRATION = 25;
  public static final int CYCLIC_GRADIENT = 26;
  public static final int HORIZONTAL_ALIGNMENT = 27;
  public static final int VERTICAL_ALIGNMENT = 28;
  public static final int TEXT_COLOR = 29;
  public static final int FONT_FACE = 30;
  public static final int FONT_SIZE = 31;
  public static final int FONT_STYLE = 32;
  public static final int TRANSPARENCY = 34;

  protected static final int STYLE_COUNT = 35;


  protected boolean[] changes;

  public Style()
  {
    changes = new boolean[STYLE_COUNT];
  }

  protected abstract String get(int key);
  protected abstract void put(int key, String value);
  protected abstract boolean has(int key);

  public boolean changed()
  {
    for(int i = 0; i < changes.length; i++)
    {
      if(changes[i])
        return true;
    }
    return false;
  }

  public boolean changed(int key)
  {
    return changes[key];
  }

  public void flushChanges()
  {
    for (int i = 0; i < changes.length; i++)
      changes[i] = false;
  }

  public int getChangedCount()
  {
    int count = 0;
    for (int i = 0; i < changes.length; i++)
      if(changes[i])
        count++;
    return count;
  }

  public int asInt(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

  public void setWidth(String value)
  {
    put(WIDTH, value);
  }

  public String getWidth()
  {
    return get(WIDTH);
  }

  public void setHeight(String value)
  {
    put(HEIGHT, value);
  }

  public String getHeight()
  {
    return get(HEIGHT);
  }

  public void setXOffset(String value)
  {
    put(X_OFFSET, value);
  }

  public String getXOffset()
  {
    return get(X_OFFSET);
  }

  public void setYOffset(String value)
  {
    put(Y_OFFSET, value);
  }

  public String getYOffset()
  {
    return get(Y_OFFSET);
  }

  public void setTextColor(String value)
  {
    put(TEXT_COLOR, value);
  }

  public String getTextColor()
  {
    return get(TEXT_COLOR);
  }

  public void setBorderColor(String value)
  {
    put(TOP_BORDER_COLOR, value);
    put(RIGHT_BORDER_COLOR, value);
    put(BOTTOM_BORDER_COLOR, value);
    put(LEFT_BORDER_COLOR, value);
  }

  public String getTopBorderColor()
  {
    return get(TOP_BORDER_COLOR);
  }

  public String getRightBorderColor()
  {
    return get(RIGHT_BORDER_COLOR);
  }

  public String getBottomBorderColor()
  {
    return get(BOTTOM_BORDER_COLOR);
  }

  public String getLeftBorderColor()
  {
    return get(LEFT_BORDER_COLOR);
  }

  public void setTopBorderColor(String value)
  {
    put(TOP_BORDER_COLOR, value);
  }

  public void setRightBorderColor(String value)
  {
    put(RIGHT_BORDER_COLOR, value);
  }

  public void setBottomBorderColor(String value)
  {
    put(BOTTOM_BORDER_COLOR, value);
  }

  public void setLeftBorderColor(String value)
  {
    put(LEFT_BORDER_COLOR, value);
  }

  public void setBorderWidth(String pixels)
  {
    put(TOP_BORDER_WIDTH, pixels);
    put(RIGHT_BORDER_WIDTH, pixels);
    put(BOTTOM_BORDER_WIDTH, pixels);
    put(LEFT_BORDER_WIDTH, pixels);
  }

  public String getTopBorderWidth()
  {
    return get(TOP_BORDER_WIDTH);
  }

  public void setTopBorderWidth(String topBorderWidth)
  {
    put(TOP_BORDER_WIDTH, topBorderWidth);
  }

  public String getRightBorderWidth()
  {
    return get(RIGHT_BORDER_WIDTH);
  }

  public void setRightBorderWidth(String rightBorderWidth)
  {
    put(RIGHT_BORDER_WIDTH, rightBorderWidth);
  }

  public String getBottomBorderWidth()
  {
    return get(BOTTOM_BORDER_WIDTH);
  }

  public void setBottomBorderWidth(String bottomBorderWidth)
  {
    put(BOTTOM_BORDER_WIDTH, bottomBorderWidth);
  }

  public String getLeftBorderWidth()
  {
    return get(LEFT_BORDER_WIDTH);
  }

  public void setLeftBorderWidth(String leftBorderWidth)
  {
    put(LEFT_BORDER_WIDTH, leftBorderWidth);
  }

  public void setMargin(String margin)
  {
    put(TOP_MARGIN, margin);
    put(RIGHT_MARGIN, margin);
    put(BOTTOM_MARGIN, margin);
    put(LEFT_MARGIN, margin);
  }

  public String getTopMargin()
  {
    return get(TOP_MARGIN);
  }

  public void setTopMargin(String topMargin)
  {
    put(TOP_MARGIN, topMargin);
  }

  public String getRightMargin()
  {
    return get(RIGHT_MARGIN);
  }

  public void setRightMargin(String rightMargin)
  {
    put(RIGHT_MARGIN, rightMargin);
  }

  public String getBottomMargin()
  {
    return get(BOTTOM_MARGIN);
  }

  public void setBottomMargin(String bottomMargin)
  {
    put(BOTTOM_MARGIN, bottomMargin);
  }

  public String getLeftMargin()
  {
    return get(LEFT_MARGIN);
  }

  public void setLeftMargin(String leftMargin)
  {
    put(LEFT_MARGIN, leftMargin);
  }

  public void setPadding(String padding)
  {
    put(TOP_PADDING, padding);
    put(RIGHT_PADDING, padding);
    put(BOTTOM_PADDING, padding);
    put(LEFT_PADDING, padding);
  }

  public String getTopPadding()
  {
    return get(TOP_PADDING);
  }

  public void setTopPadding(String topPadding)
  {
    put(TOP_PADDING, topPadding);
  }

  public String getRightPadding()
  {
    return get(RIGHT_PADDING);
  }

  public void setRightPadding(String rightPadding)
  {
    put(RIGHT_PADDING, rightPadding);
  }

  public String getBottomPadding()
  {
    return get(BOTTOM_PADDING);
  }

  public void setBottomPadding(String bottomPadding)
  {
    put(BOTTOM_PADDING, bottomPadding);
  }

  public String getLeftPadding()
  {
    return get(LEFT_PADDING);
  }

  public void setLeftPadding(String leftPadding)
  {
    put(LEFT_PADDING, leftPadding);
  }

  public void setBackgroundColor(String backgroundColor)
  {
    put(BACKGROUND_COLOR, backgroundColor);
  }

  public String getBackgroundColor()
  {
    return get(BACKGROUND_COLOR);
  }

  public void setBackgroundImage(String backgroundImage)
  {
    put(BACKGROUND_IMAGE, backgroundImage);
  }

  public String getBackgroundImage()
  {
    return get(BACKGROUND_IMAGE);
  }

  public String getBackgroundImageFillStrategy()
  {
    return get(BACKGROUND_IMAGE_FILL_STRATEGY);
  }

  public void setBackgroundImageFillStrategy(String backgroundImageFillStrategy)
  {
    put(BACKGROUND_IMAGE_FILL_STRATEGY, backgroundImageFillStrategy);
  }

  public void setSecondaryBackgroundColor(String color)
  {
    put(SECONDARY_BACKGROUND_COLOR, color);
  }

  public String getSecondaryBackgroundColor()
  {
    return get(SECONDARY_BACKGROUND_COLOR);
  }

  public void setGradientAngle(String value)
  {
    put(GRADIENT_ANGLE, value);
  }

  public String getGradientAngle()
  {
    return get(GRADIENT_ANGLE);
  }

  public void setGradientPenetration(String value)
  {
    put(GRADIENT_PENETRATION, value);
  }

  public String getGradientPenetration()
  {
    return get(GRADIENT_PENETRATION);
  }

  public void setCyclicGradient(String value)
  {
    put(CYCLIC_GRADIENT, value);
  }

  public String getCyclicGradient()
  {
    return get(CYCLIC_GRADIENT);
  }

  public void setHorizontalAlignment(String alignment)
  {
    put(HORIZONTAL_ALIGNMENT, alignment);
  }

  public String getHorizontalAlignment()
  {
    return get(HORIZONTAL_ALIGNMENT);
  }

  public void setVerticalAlignment(String alignment)
  {
    put(VERTICAL_ALIGNMENT, alignment);
  }

  public String getVerticalAlignment()
  {
    return get(VERTICAL_ALIGNMENT);
  }

  public String getFontFace()
  {
    return get(FONT_FACE);
  }

  public void setFontFace(String fontFace)
  {
    put(FONT_FACE, fontFace);
  }

  public String getFontSize()
  {
    return get(FONT_SIZE);
  }

  public void setFontSize(String fontSize)
  {
    put(FONT_SIZE, fontSize);
  }

  public String getFontStyle()
  {
    return get(FONT_STYLE);
  }

  public void setFontStyle(String fontStyle)
  {
    put(FONT_STYLE, fontStyle);
  }

  public String getTransparency()
  {
    return get(TRANSPARENCY);
  }

  public void setTransparency(String transparency)
  {
    put(TRANSPARENCY, transparency);
  }
}
