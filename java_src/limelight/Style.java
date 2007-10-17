package limelight;

import java.util.Hashtable;

public class Style
{
	private Hashtable<String, String> styles;

	public Style()
	{
		styles = new Hashtable<String, String>();
	}

	public Hashtable<String, String> getStyles()
	{
		return styles;
	}

	public void setWidth(String value)
	{
		styles.put("width", value);
	}

	public String getWidth()
	{
		return styles.get("width");
	}

	public void setHeight(String value)
	{
		styles.put("height", value);
	}

	public String getHeight()
	{
		return styles.get("height");
	}

	public void setTextColor(String value)
	{
		styles.put("textColor", value);
	}

	public String getTextColor()
	{
		return styles.get("textColor");
	}

	public void setBorderColor(String value)
	{
		styles.put("topBorderColor", value);
		styles.put("rightBorderColor",value);
		styles.put("bottomBorderColor", value);
		styles.put("leftBorderColor", value);
	}

	public String getTopBorderColor()
	{
		return styles.get("topBorderColor");
	}

	public String getRightBorderColor()
	{
		return styles.get("rightBorderColor");
	}

	public String getBottomBorderColor()
	{
		return styles.get("bottomBorderColor");
	}

	public String getLeftBorderColor()
	{
		return styles.get("leftBorderColor");
	}

	public String getBackgroundColor()
	{
		return styles.get("backgroundColor");
	}

	public void setTopBorderColor(String value)
	{
		styles.put("topBorderColor", value);
	}

	public void setRightBorderColor(String value)
	{
		styles.put("rightBorderColor", value);
	}

	public void setBottomBorderColor(String value)
	{
		styles.put("bottomBorderColor", value);
	}

	public void setLeftBorderColor(String value)
	{
		styles.put("leftBorderColor", value);
	}

	public void setBorderWidth(String pixels)
	{
		styles.put("topBorderWidth", pixels);
		styles.put("rightBorderWidth", pixels);
		styles.put("bottomBorderWidth", pixels);
		styles.put("leftBorderWidth", pixels);
	}

	public String getTopBorderWidth()
	{
		return styles.get("topBorderWidth");
	}

	public void setTopBorderWidth(String topBorderWidth)
	{
		styles.put("topBorderWidth", topBorderWidth);
	}

	public String getRightBorderWidth()
	{
		return styles.get("rightBorderWidth");
	}

	public void setRightBorderWidth(String rightBorderWidth)
	{
		styles.put("rightBorderWidth", rightBorderWidth);
	}

	public String getBottomBorderWidth()
	{
		return styles.get("bottomBorderWidth");
	}

	public void setBottomBorderWidth(String bottomBorderWidth)
	{
		styles.put("bottomBorderWidth", bottomBorderWidth);
	}

	public String getLeftBorderWidth()
	{
		return styles.get("leftBorderWidth");
	}

	public void setLeftBorderWidth(String leftBorderWidth)
	{
		styles.put("leftBorderWidth", leftBorderWidth);
	}

	public void setMargin(String margin)
	{
		styles.put("topMargin", margin);
		styles.put("rightMargin", margin);
		styles.put("bottomMargin", margin);
		styles.put("leftMargin", margin);
	}

	public String getTopMargin()
	{
		return styles.get("topMargin");
	}

	public void setTopMargin(String topMargin)
	{
		styles.put("topMargin", topMargin);
	}

	public String getRightMargin()
	{
		return styles.get("rightMargin");
	}

	public void setRightMargin(String rightMargin)
	{
		styles.put("rightMargin", rightMargin);
	}

	public String getBottomMargin()
	{
		return styles.get("bottomMargin");
	}

	public void setBottomMargin(String bottomMargin)
	{
		styles.put("bottomMargin", bottomMargin);
	}

	public String getLeftMargin()
	{
		return styles.get("leftMargin");
	}

	public void setLeftMargin(String leftMargin)
	{
		styles.put("leftMargin", leftMargin);
	}

	public void setPadding(String padding)
	{
		styles.put("topPadding", padding);
		styles.put("rightPadding",padding);
		styles.put("bottomPadding", padding);
		styles.put("leftPadding", padding);
	}

	public String getTopPadding()
	{
		return styles.get("topPadding");
	}

	public void setTopPadding(String topPadding)
	{
		styles.put("topPadding", topPadding);
	}

	public String getRightPadding()
	{
		return styles.get("rightPadding");
	}

	public void setRightPadding(String rightPadding)
	{
		styles.put("rightPadding", rightPadding);
	}

	public String getBottomPadding()
	{
		return styles.get("bottomPadding");
	}

	public void setBottomPadding(String bottomPadding)
	{
		styles.put("bottomPadding", bottomPadding);
	}

	public String getLeftPadding()
	{
		return styles.get("leftPadding");
	}

	public void setLeftPadding(String leftPadding)
	{
		styles.put("leftPadding", leftPadding);
	}

	public void setBackgroundColor(String backgroundColor)
	{
		styles.put("backgroundColor", backgroundColor);
	}

	public void setBackgroundImage(String backgroundImage)
	{
		styles.put("backgroundImage", backgroundImage);
	}

	public String getBackgroundImage()
	{
		return styles.get("backgroundImage");
	}

	public String getBackgroundImageFillStrategy()
	{
		return styles.get("backgroundImageFillStrategy");
	}

	public void setBackgroundImageFillStrategy(String backgroundImageFillStrategy)
	{
		styles.put("backgroundImageFillStrategy", backgroundImageFillStrategy);
	}

	public void setHorizontalAlignment(String alignment)
	{
		styles.put("horizontalAlignment", alignment);
	}

	public String getHorizontalAlignment()
	{
		return styles.get("horizontalAlignment");
	}

	public void setVerticalAlignment(String alignment)
	{
		styles.put("verticalAlignment", alignment);
	}

	public String getVerticalAlignment()
	{
		return styles.get("verticalAlignment");
	}

	public String getFontFace()
	{
		return styles.get("fontFace");
	}

	public void setFontFace(String fontFace)
	{
		styles.put("fontFace", fontFace);
	}

	public String getFontSize()
	{
		return styles.get("fontSize");
	}

	public void setFontSize(String fontSize)
	{
		styles.put("fontSize", fontSize);
	}

	public String getFontStyle()
	{
		return styles.get("fontStyle");
	}

	public void setFontStyle(String fontStyle)
	{
		styles.put("fontStyle", fontStyle);
	}

}
