package limelight;

public abstract class Style
{
  protected abstract String get(String key);

  protected abstract void put(String key, String value);

  protected abstract boolean has(Object key);

  public abstract int checksum();

  public void setWidth(String value)
  {
    put("width", value);
  }

  public String getWidth()
  {
    return get("width");
  }

  public void setHeight(String value)
  {
    put("height", value);
  }

  public String getHeight()
  {
    return get("height");
  }

  public void setTextColor(String value)
  {
    put("textColor", value);
  }

  public String getTextColor()
  {
    return get("textColor");
  }

  public void setBorderColor(String value)
  {
    put("topBorderColor", value);
    put("rightBorderColor", value);
    put("bottomBorderColor", value);
    put("leftBorderColor", value);
  }

  public String getTopBorderColor()
  {
    return get("topBorderColor");
  }

  public String getRightBorderColor()
  {
    return get("rightBorderColor");
  }

  public String getBottomBorderColor()
  {
    return get("bottomBorderColor");
  }

  public String getLeftBorderColor()
  {
    return get("leftBorderColor");
  }

  public String getBackgroundColor()
  {
    return get("backgroundColor");
  }

  public void setTopBorderColor(String value)
  {
    put("topBorderColor", value);
  }

  public void setRightBorderColor(String value)
  {
    put("rightBorderColor", value);
  }

  public void setBottomBorderColor(String value)
  {
    put("bottomBorderColor", value);
  }

  public void setLeftBorderColor(String value)
  {
    put("leftBorderColor", value);
  }

  public void setBorderWidth(String pixels)
  {
    put("topBorderWidth", pixels);
    put("rightBorderWidth", pixels);
    put("bottomBorderWidth", pixels);
    put("leftBorderWidth", pixels);
  }

  public String getTopBorderWidth()
  {
    return get("topBorderWidth");
  }

  public void setTopBorderWidth(String topBorderWidth)
  {
    put("topBorderWidth", topBorderWidth);
  }

  public String getRightBorderWidth()
  {
    return get("rightBorderWidth");
  }

  public void setRightBorderWidth(String rightBorderWidth)
  {
    put("rightBorderWidth", rightBorderWidth);
  }

  public String getBottomBorderWidth()
  {
    return get("bottomBorderWidth");
  }

  public void setBottomBorderWidth(String bottomBorderWidth)
  {
    put("bottomBorderWidth", bottomBorderWidth);
  }

  public String getLeftBorderWidth()
  {
    return get("leftBorderWidth");
  }

  public void setLeftBorderWidth(String leftBorderWidth)
  {
    put("leftBorderWidth", leftBorderWidth);
  }

  public void setXOffset(String value)
  {
    put("xOffset", value);
  }

  public String getXOffset()
  {
    return get("xOffset");
  }

  public void setYOffset(String value)
  {
    put("yOffset", value);
  }

  public String getYOffset()
  {
    return get("yOffset");
  }

  public void setMargin(String margin)
  {
    put("topMargin", margin);
    put("rightMargin", margin);
    put("bottomMargin", margin);
    put("leftMargin", margin);
  }

  public String getTopMargin()
  {
    return get("topMargin");
  }

  public void setTopMargin(String topMargin)
  {
    put("topMargin", topMargin);
  }

  public String getRightMargin()
  {
    return get("rightMargin");
  }

  public void setRightMargin(String rightMargin)
  {
    put("rightMargin", rightMargin);
  }

  public String getBottomMargin()
  {
    return get("bottomMargin");
  }

  public void setBottomMargin(String bottomMargin)
  {
    put("bottomMargin", bottomMargin);
  }

  public String getLeftMargin()
  {
    return get("leftMargin");
  }

  public void setLeftMargin(String leftMargin)
  {
    put("leftMargin", leftMargin);
  }

  public void setPadding(String padding)
  {
    put("topPadding", padding);
    put("rightPadding", padding);
    put("bottomPadding", padding);
    put("leftPadding", padding);
  }

  public String getTopPadding()
  {
    return get("topPadding");
  }

  public void setTopPadding(String topPadding)
  {
    put("topPadding", topPadding);
  }

  public String getRightPadding()
  {
    return get("rightPadding");
  }

  public void setRightPadding(String rightPadding)
  {
    put("rightPadding", rightPadding);
  }

  public String getBottomPadding()
  {
    return get("bottomPadding");
  }

  public void setBottomPadding(String bottomPadding)
  {
    put("bottomPadding", bottomPadding);
  }

  public String getLeftPadding()
  {
    return get("leftPadding");
  }

  public void setLeftPadding(String leftPadding)
  {
    put("leftPadding", leftPadding);
  }

  public void setBackgroundColor(String backgroundColor)
  {
    put("backgroundColor", backgroundColor);
  }

  public void setBackgroundImage(String backgroundImage)
  {
    put("backgroundImage", backgroundImage);
  }

  public String getBackgroundImage()
  {
    return get("backgroundImage");
  }

  public String getBackgroundImageFillStrategy()
  {
    return get("backgroundImageFillStrategy");
  }

  public void setBackgroundImageFillStrategy(String backgroundImageFillStrategy)
  {
    put("backgroundImageFillStrategy", backgroundImageFillStrategy);
  }

  public void setHorizontalAlignment(String alignment)
  {
    put("horizontalAlignment", alignment);
  }

  public String getHorizontalAlignment()
  {
    return get("horizontalAlignment");
  }

  public void setVerticalAlignment(String alignment)
  {
    put("verticalAlignment", alignment);
  }

  public String getVerticalAlignment()
  {
    return get("verticalAlignment");
  }

  public String getFontFace()
  {
    return get("fontFace");
  }

  public void setFontFace(String fontFace)
  {
    put("fontFace", fontFace);
  }

  public String getFontSize()
  {
    return get("fontSize");
  }

  public void setFontSize(String fontSize)
  {
    put("fontSize", fontSize);
  }

  public String getFontStyle()
  {
    return get("fontStyle");
  }

  public void setFontStyle(String fontStyle)
  {
    put("fontStyle", fontStyle);
  }

  public String getTransparency()
  {
    return get("transparency");
  }

  public void setTransparency(String transparency)
  {
    put("transparency", transparency);
  }

  public void setSecondaryBackgroundColor(String color)
  {
    put("secondaryBackgroundColor", color);
  }

  public String getSecondaryBackgroundColor()
  {
    return get("secondaryBackgroundColor");
  }

  public void setGradientAngle(String value)
  {
    put("gradientAngle", value);
  }

  public String getGradientAngle()
  {
    return get("gradientAngle");
  }

  public void setGradientPenetration(String value)
  {
    put("gradientPenetration", value);
  }

  public String getGradientPenetration()
  {
    return get("gradientPenetration");
  }

  public void setCyclicGradient(String value)
  {
    put("cyclicGradient", value);
  }

  public String getCyclicGradient()
  {
    return get("cyclicGradient");
  }
}
