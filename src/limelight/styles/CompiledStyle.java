package limelight.styles;

import java.awt.*;

public class CompiledStyle extends Style implements StyleObserver
{
  private Style target;
  public Object width;
  public Object height;
  public Object minWidth;
  public Object minHeight;
  public Object maxWidth;
  public Object maxHeight;
  public Object verticalScrollbar;
  public Object horizontalScrollbar;
  public Color topBorderColor;
  public Color rightBorderColor;
  public Color bottomBorderColor;
  public Color leftBorderColor;
  public int topBorderWidth;
  public int rightBorderWidth;
  public int bottomBorderWidth;
  public int leftBorderWidth;
  public int topMargin;
  public int rightMargin;
  public int bottomMargin;
  public int leftMargin;
  public int topPadding;
  public int rightPadding;
  public int bottomPadding;
  public int leftPadding;
  public Color backgroundColor;
  public Color secondaryBackgroundColor;
  public Object backgroundImage;
  public Object backgroundImageFillStrategy;
  public Object gradient;
  public int gradientAngle;
  public int gradientPenetration;
  public Object cyclicGradient;
  public Object horizontalAlignment;
  public Object verticalAlignment;
  public Color textColor;
  public String fontFace;
  public int fontSize;
  public String fontStyle;
  public int transparency;
  public int topRightRoundedCornerRadius;
  public int bottomRightRoundedCornerRadius;
  public int bottomLeftRoundedCornerRadius;
  public int topLeftRoundedCornerRadius;
  public int topRightBorderWidth;
  public int bottomRightBorderWidth;
  public int bottomLeftBorderWidth;
  public int topLeftBorderWidth;
  public Color topRightBorderColor;
  public Color bottomRightBorderColor;
  public Color bottomLeftBorderColor;
  public Color topLeftBorderColor;
  public Object floating;
  public int x;
  public int y;


  public CompiledStyle(Style target)
  {
    this.target = target;
    target.addObserver(this);
  }

  protected String get(int key)
  {
    return target.get(key);
  }

  public void put(StyleDescriptor descriptor, String value)
  {
    target.put(descriptor, value);
  }

  public void setDefault(StyleDescriptor descriptor, String value)
  {
    target.setDefault(descriptor, value);
  }

  protected String getDefaultValue(StyleDescriptor descriptor)
  {
    return target.getDefaultValue(descriptor);
  }

  public boolean changed()
  {
    return target.changed();
  }

  public boolean changed(StyleDescriptor descriptor)
  {
    return target.changed(descriptor);
  }

  public void flushChanges()
  {
    target.flushChanges();
  }

  public int getChangedCount()
  {
    return target.getChangedCount();
  }

  public void removeObserver(StyleObserver observer)
  {
    target.removeObserver(observer);
  }

  public void addObserver(StyleObserver observer)
  {
    target.addObserver(observer);
  }

  public boolean hasObserver(StyleObserver observer)
  {
    return target.hasObserver(observer);
  }

  public void styleChanged(StyleDescriptor descriptor, String value)
  {
  }
}
