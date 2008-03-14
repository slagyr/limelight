package limelight.ui;

public abstract class Style
{
  public static final StyleDescriptor WIDTH = new StyleDescriptor(0, null);
  public static final StyleDescriptor HEIGHT = new StyleDescriptor(1, null);
  public static final StyleDescriptor X_OFFSET = new StyleDescriptor(2, null);
  public static final StyleDescriptor Y_OFFSET = new StyleDescriptor(3, null);
  public static final StyleDescriptor TOP_BORDER_COLOR = new StyleDescriptor(4, null);
  public static final StyleDescriptor RIGHT_BORDER_COLOR = new StyleDescriptor(5, null);
  public static final StyleDescriptor BOTTOM_BORDER_COLOR = new StyleDescriptor(6, null);
  public static final StyleDescriptor LEFT_BORDER_COLOR = new StyleDescriptor(7, null);
  public static final StyleDescriptor TOP_BORDER_WIDTH = new StyleDescriptor(8, null);
  public static final StyleDescriptor RIGHT_BORDER_WIDTH = new StyleDescriptor(9, null);
  public static final StyleDescriptor BOTTOM_BORDER_WIDTH = new StyleDescriptor(10, null);
  public static final StyleDescriptor LEFT_BORDER_WIDTH = new StyleDescriptor(11, null);
  public static final StyleDescriptor TOP_MARGIN = new StyleDescriptor(12, null);
  public static final StyleDescriptor RIGHT_MARGIN = new StyleDescriptor(13, null);
  public static final StyleDescriptor BOTTOM_MARGIN = new StyleDescriptor(14, null);
  public static final StyleDescriptor LEFT_MARGIN = new StyleDescriptor(15, null);
  public static final StyleDescriptor TOP_PADDING = new StyleDescriptor(16, null);
  public static final StyleDescriptor RIGHT_PADDING = new StyleDescriptor(17, null);
  public static final StyleDescriptor BOTTOM_PADDING = new StyleDescriptor(18, null);
  public static final StyleDescriptor LEFT_PADDING = new StyleDescriptor(19, null);
  public static final StyleDescriptor BACKGROUND_COLOR = new StyleDescriptor(20, null);
  public static final StyleDescriptor SECONDARY_BACKGROUND_COLOR = new StyleDescriptor(21, null);
  public static final StyleDescriptor BACKGROUND_IMAGE = new StyleDescriptor(22, null);
  public static final StyleDescriptor BACKGROUND_IMAGE_FILL_STRATEGY = new StyleDescriptor(23, null);
  public static final StyleDescriptor GRADIENT_ANGLE = new StyleDescriptor(24, null);
  public static final StyleDescriptor GRADIENT_PENETRATION = new StyleDescriptor(25, null);
  public static final StyleDescriptor CYCLIC_GRADIENT = new StyleDescriptor(26, null);
  public static final StyleDescriptor HORIZONTAL_ALIGNMENT = new StyleDescriptor(27, null);
  public static final StyleDescriptor VERTICAL_ALIGNMENT = new StyleDescriptor(28, null);
  public static final StyleDescriptor TEXT_COLOR = new StyleDescriptor(29, null);
  public static final StyleDescriptor FONT_FACE = new StyleDescriptor(30, null);
  public static final StyleDescriptor FONT_SIZE = new StyleDescriptor(31, null);
  public static final StyleDescriptor FONT_STYLE = new StyleDescriptor(32, null);
  public static final StyleDescriptor TRANSPARENCY = new StyleDescriptor(33, null);
  public static final StyleDescriptor TOP_RIGHT_ROUNDED_CORNER_RADIUS = new StyleDescriptor(34, null);
  public static final StyleDescriptor BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS = new StyleDescriptor(35, null);
  public static final StyleDescriptor BOTTOM_LEFT_ROUNDED_CORNER_RADIUS = new StyleDescriptor(36, null);
  public static final StyleDescriptor TOP_LEFT_ROUNDED_CORNER_RADIUS = new StyleDescriptor(37, null);
  public static final StyleDescriptor TOP_RIGHT_BORDER_WIDTH = new StyleDescriptor(38, null);
  public static final StyleDescriptor BOTTOM_RIGHT_BORDER_WIDTH = new StyleDescriptor(39, null);
  public static final StyleDescriptor BOTTOM_LEFT_BORDER_WIDTH = new StyleDescriptor(40, null);
  public static final StyleDescriptor TOP_LEFT_BORDER_WIDTH = new StyleDescriptor(41, null);
  public static final StyleDescriptor TOP_RIGHT_BORDER_COLOR = new StyleDescriptor(42, null);
  public static final StyleDescriptor BOTTOM_RIGHT_BORDER_COLOR = new StyleDescriptor(43, null);
  public static final StyleDescriptor BOTTOM_LEFT_BORDER_COLOR = new StyleDescriptor(44, null);
  public static final StyleDescriptor TOP_LEFT_BORDER_COLOR = new StyleDescriptor(45, null);

  protected static final int STYLE_COUNT = 46;

  protected boolean[] changes;

  public Style()
  {
    changes = new boolean[STYLE_COUNT];
  }

  protected abstract String get(int key);
  protected abstract void put(StyleDescriptor descriptor, String value);
  protected abstract boolean has(int key);

  protected String get(StyleDescriptor descriptor)
  {
    return get(descriptor.index);
  }

  public boolean changed()
  {
    for(int i = 0; i < changes.length; i++)
    {
      if(changes[i])
        return true;
    }
    return false;
  }

  public boolean changed(StyleDescriptor descriptor)
  {
    return changes[descriptor.index];
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
    put(TOP_RIGHT_BORDER_COLOR, value);
    put(RIGHT_BORDER_COLOR, value);
    put(BOTTOM_RIGHT_BORDER_COLOR, value);
    put(BOTTOM_BORDER_COLOR, value);
    put(BOTTOM_LEFT_BORDER_COLOR, value);
    put(LEFT_BORDER_COLOR, value);
    put(TOP_LEFT_BORDER_COLOR, value);
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
    put(TOP_RIGHT_BORDER_WIDTH, pixels);
    put(RIGHT_BORDER_WIDTH, pixels);
    put(BOTTOM_RIGHT_BORDER_WIDTH, pixels);
    put(BOTTOM_BORDER_WIDTH, pixels);
    put(BOTTOM_LEFT_BORDER_WIDTH, pixels);
    put(LEFT_BORDER_WIDTH, pixels);
    put(TOP_LEFT_BORDER_WIDTH, pixels);
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

  public String getTopRightBorderWidth()
  {
    return get(TOP_RIGHT_BORDER_WIDTH);
  }

  public String getBottomRightBorderWidth()
  {
    return get(BOTTOM_RIGHT_BORDER_WIDTH);
  }

  public String getBottomLeftBorderWidth()
  {
    return get(BOTTOM_LEFT_BORDER_WIDTH);
  }

  public String getTopLeftBorderWidth()
  {
    return get(TOP_LEFT_BORDER_WIDTH);
  }

  public String getTopRightBorderColor()
  {
    return get(TOP_RIGHT_BORDER_COLOR);
  }

  public String getBottomRightBorderColor()
  {
    return get(BOTTOM_RIGHT_BORDER_COLOR);
  }

  public String getBottomLeftBorderColor()
  {
    return get(BOTTOM_LEFT_BORDER_COLOR);
  }

  public String getTopLeftBorderColor()
  {
    return get(TOP_LEFT_BORDER_COLOR);
  }

  public void setRoundedCornerRadius(String radius)
  {
    put(TOP_RIGHT_ROUNDED_CORNER_RADIUS, radius);
    put(BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS, radius);
    put(BOTTOM_LEFT_ROUNDED_CORNER_RADIUS, radius);
    put(TOP_LEFT_ROUNDED_CORNER_RADIUS, radius);
  }

  public void setTopRightRoundedCornerRadius(String value)
  {
    put(TOP_RIGHT_ROUNDED_CORNER_RADIUS, value);
  }

  public String getTopRightRoundedCornerRadius()
  {
    return get(TOP_RIGHT_ROUNDED_CORNER_RADIUS);
  }

  public void setBottomRightRoundedCornerRadius(String value)
  {
    put(BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS, value);
  }

  public String getBottomRightRoundedCornerRadius()
  {
    return get(BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS);
  }

  public void setBottomLeftRoundedCornerRadius(String value)
  {
    put(BOTTOM_LEFT_ROUNDED_CORNER_RADIUS, value);
  }

  public String getBottomLeftRoundedCornerRadius()
  {
    return get(BOTTOM_LEFT_ROUNDED_CORNER_RADIUS);
  }

  public void setTopLeftRoundedCornerRadius(String value)
  {
    put(TOP_LEFT_ROUNDED_CORNER_RADIUS, value);
  }

  public String getTopLeftRoundedCornerRadius()
  {
    return get(TOP_LEFT_ROUNDED_CORNER_RADIUS);
  }

  public void setTopRoundedCornerRadius(String radius)
  {
    put(TOP_RIGHT_ROUNDED_CORNER_RADIUS, radius);
    put(TOP_LEFT_ROUNDED_CORNER_RADIUS, radius);
  }

  public void setRightRoundedCornerRadius(String radius)
  {
    put(TOP_RIGHT_ROUNDED_CORNER_RADIUS, radius);
    put(BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS, radius);
  }

  public void setBottomRoundedCornerRadius(String radius)
  {
    put(BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS, radius);
    put(BOTTOM_LEFT_ROUNDED_CORNER_RADIUS, radius);
  }

  public void setLeftRoundedCornerRadius(String radius)
  {
    put(BOTTOM_LEFT_ROUNDED_CORNER_RADIUS, radius);
    put(TOP_LEFT_ROUNDED_CORNER_RADIUS, radius);
  }
}
