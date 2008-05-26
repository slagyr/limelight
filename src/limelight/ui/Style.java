//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.util.LinkedList;

public abstract class Style
{
  public static final LinkedList<StyleDescriptor> STYLE_LIST = new LinkedList<StyleDescriptor>();

  public static StyleDescriptor descriptor(String name, String defaultValue)
  {
    StyleDescriptor descriptor = new StyleDescriptor(STYLE_LIST.size(), name, defaultValue);
    STYLE_LIST.add(descriptor);
    return descriptor;
  }

  public static final StyleDescriptor WIDTH = descriptor("Width", "auto");
  public static final StyleDescriptor HEIGHT = descriptor("Height", "auto");
  public static final StyleDescriptor VERTICAL_SCROLLBAR = descriptor("Vertical Scrollbar", "auto");
  public static final StyleDescriptor HORIZONTAL_SCROLLBAR = descriptor("Horizontal Scrollbar", "auto");
  public static final StyleDescriptor TOP_BORDER_COLOR = descriptor("Top Border Color", "black");
  public static final StyleDescriptor RIGHT_BORDER_COLOR = descriptor("Right Border Color", "black");
  public static final StyleDescriptor BOTTOM_BORDER_COLOR = descriptor("Bottom Border Color", "black");
  public static final StyleDescriptor LEFT_BORDER_COLOR = descriptor("Left Border Color", "black");
  public static final StyleDescriptor TOP_BORDER_WIDTH = descriptor("Top Border Width", "0");
  public static final StyleDescriptor RIGHT_BORDER_WIDTH = descriptor("Right Border Width", "0");                            
  public static final StyleDescriptor BOTTOM_BORDER_WIDTH = descriptor("Bottom Border Width", "0");
  public static final StyleDescriptor LEFT_BORDER_WIDTH = descriptor("Left Border Width", "0");
  public static final StyleDescriptor TOP_MARGIN = descriptor("Top Margin", "0");
  public static final StyleDescriptor RIGHT_MARGIN = descriptor("Right Margin", "0");
  public static final StyleDescriptor BOTTOM_MARGIN = descriptor("Bottom Margin", "0");
  public static final StyleDescriptor LEFT_MARGIN = descriptor("Left Margin", "0");
  public static final StyleDescriptor TOP_PADDING = descriptor("Top Padding", "0");
  public static final StyleDescriptor RIGHT_PADDING = descriptor("Right Padding", "0");
  public static final StyleDescriptor BOTTOM_PADDING = descriptor("Bottom Padding", "0");
  public static final StyleDescriptor LEFT_PADDING = descriptor("Left Padding", "0");
  public static final StyleDescriptor BACKGROUND_COLOR = descriptor("Background Color", "transparent");
  public static final StyleDescriptor SECONDARY_BACKGROUND_COLOR = descriptor("Secondary Background Color", "transparent");
  public static final StyleDescriptor BACKGROUND_IMAGE = descriptor("Background Image", "none");
  public static final StyleDescriptor BACKGROUND_IMAGE_FILL_STRATEGY = descriptor("Background Image Fill Strategy", "repeat");
  public static final StyleDescriptor GRADIENT = descriptor("Gradient", "off");
  public static final StyleDescriptor GRADIENT_ANGLE = descriptor("Gradient Angle", "90");
  public static final StyleDescriptor GRADIENT_PENETRATION = descriptor("Gradient Penetration", "100");
  public static final StyleDescriptor CYCLIC_GRADIENT = descriptor("Cyclic Gradient", "off");
  public static final StyleDescriptor HORIZONTAL_ALIGNMENT = descriptor("Horizontal Alignment", "left");
  public static final StyleDescriptor VERTICAL_ALIGNMENT = descriptor("Vertical Alignment", "top");
  public static final StyleDescriptor TEXT_COLOR = descriptor("Text Color", "black");
  public static final StyleDescriptor FONT_FACE = descriptor("Font Face", "Arial");
  public static final StyleDescriptor FONT_SIZE = descriptor("Font Size", "12");
  public static final StyleDescriptor FONT_STYLE = descriptor("Font Style", "plain");
  public static final StyleDescriptor TRANSPARENCY = descriptor("Transparency", "0");
  public static final StyleDescriptor TOP_RIGHT_ROUNDED_CORNER_RADIUS = descriptor("Top Right Rounded Corner Radius", "0");
  public static final StyleDescriptor BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS = descriptor("Bottom Right Rounded Corner Radius", "0");
  public static final StyleDescriptor BOTTOM_LEFT_ROUNDED_CORNER_RADIUS = descriptor("Bottom Left Rounded Corner Radius", "0");
  public static final StyleDescriptor TOP_LEFT_ROUNDED_CORNER_RADIUS = descriptor("Top Left Rounded Corner Radius", "0");
  public static final StyleDescriptor TOP_RIGHT_BORDER_WIDTH = descriptor("Top Right Border Width", "0");
  public static final StyleDescriptor BOTTOM_RIGHT_BORDER_WIDTH = descriptor("Bottom Right Border Width", "0");
  public static final StyleDescriptor BOTTOM_LEFT_BORDER_WIDTH = descriptor("Bottom Left Border Width", "0");
  public static final StyleDescriptor TOP_LEFT_BORDER_WIDTH = descriptor("Top Left Border Width", "0");
  public static final StyleDescriptor TOP_RIGHT_BORDER_COLOR = descriptor("Top Right Border Color", "black");
  public static final StyleDescriptor BOTTOM_RIGHT_BORDER_COLOR = descriptor("Bottom Right Border Color", "black");
  public static final StyleDescriptor BOTTOM_LEFT_BORDER_COLOR = descriptor("Bottom Left Border Color", "black");
  public static final StyleDescriptor TOP_LEFT_BORDER_COLOR = descriptor("Top Left Border Color", "black");
  public static final StyleDescriptor FLOAT = descriptor("Float", "off");
  public static final StyleDescriptor X = descriptor("X", "0");
  public static final StyleDescriptor Y = descriptor("Y", "0");

  protected static final int STYLE_COUNT = STYLE_LIST.size();

  protected boolean[] changes;

  public Style()
  {
    changes = new boolean[STYLE_COUNT];
  }

  protected abstract String get(int key);
  public abstract void put(StyleDescriptor descriptor, String value);
  protected abstract boolean has(int key);  //TODO  MDM,  Delete ME!

  public String get(StyleDescriptor descriptor)
  {
    String value = get(descriptor.index);
    if(value == null)
      return descriptor.defaultValue;
    else
      return value;
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
  
  public boolean isOn(String value)
  {
    return "on".equals(value);
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

  public void setGradient(String color)
  {
    put(GRADIENT, color);
  }

  public String getGradient()
  {
    return get(GRADIENT);
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

  public void setTopRightBorderWidth(String value)
  {
    put(TOP_RIGHT_BORDER_WIDTH, value);
  }

  public String getBottomRightBorderWidth()
  {
    return get(BOTTOM_RIGHT_BORDER_WIDTH);
  }

  public void setBottomRightBorderWidth(String value)
  {
    put(BOTTOM_RIGHT_BORDER_WIDTH, value);
  }

  public String getBottomLeftBorderWidth()
  {
    return get(BOTTOM_LEFT_BORDER_WIDTH);
  }

  public void setBottomLeftBorderWidth(String value)
  {
    put(BOTTOM_LEFT_BORDER_WIDTH, value);
  }

  public String getTopLeftBorderWidth()
  {
    return get(TOP_LEFT_BORDER_WIDTH);
  }

  public void setTopLeftBorderWidth(String value)
  {
    put(TOP_LEFT_BORDER_WIDTH, value);
  }

  public String getTopRightBorderColor()
  {
    return get(TOP_RIGHT_BORDER_COLOR);
  }

  public void setTopRightBorderColor(String value)
  {
    put(TOP_RIGHT_BORDER_COLOR, value);
  }

  public String getBottomRightBorderColor()
  {
    return get(BOTTOM_RIGHT_BORDER_COLOR);
  }

  public void setBottomRightBorderColor(String value)
  {
    put(BOTTOM_RIGHT_BORDER_COLOR, value);
  }

  public String getBottomLeftBorderColor()
  {
    return get(BOTTOM_LEFT_BORDER_COLOR);
  }

  public void setBottomLeftBorderColor(String value)
  {
    put(BOTTOM_LEFT_BORDER_COLOR, value);
  }

  public String getTopLeftBorderColor()
  {
    return get(TOP_LEFT_BORDER_COLOR);
  }

  public void setTopLeftBorderColor(String value)
  {
    put(TOP_LEFT_BORDER_COLOR, value);
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

  public String getFloat()
  {
    return get(FLOAT);
  }

  public void setFloat(String value)
  {
    put(FLOAT, value);
  }

  public String getX()
  {
    return get(X);
  }

  public void setX(String value)
  {
    put(X, value);
  }

  public String getY()
  {
    return get(Y);
  }

  public void setY(String value)
  {
    put(Y, value);
  }

  public void setScrollbars(String value)
  {
    put(VERTICAL_SCROLLBAR, value);
    put(HORIZONTAL_SCROLLBAR, value);
  }

  public String getVerticalScrollbar()
  {
    return get(VERTICAL_SCROLLBAR);
  }

  public String getHorizontalScrollbar()
  {
    return get(HORIZONTAL_SCROLLBAR);
  }

  public void setVerticalScrollbar(String value)
  {
    put(VERTICAL_SCROLLBAR, value);
  }

  public void setHorizontalScrollbar(String value)
  {
    put(HORIZONTAL_SCROLLBAR, value);
  }
}
