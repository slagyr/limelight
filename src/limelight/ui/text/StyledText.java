//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.styles.RichStyle;
import limelight.styles.StyleObserver;
import limelight.ui.Fonts;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StyledText
{
  private String text;
  private LinkedList<String> styleNames;
  private RichStyle style;

  public StyledText(String text, RichStyle style, List<String> styleNames)
  {
    setText(text);
    this.style = style == null ? new RichStyle() : style;
    this.styleNames = new LinkedList<String>(styleNames);
  }

  public StyledText(String text, LinkedList<String> styleNames)
  {
    this(text, null, styleNames);
  }

  public StyledText(String text, String... styleNames)
  {
    this(text, new LinkedList<String>());
    for(String style : styleNames)
      this.styleNames.add(style);
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public List<String> getStyleNames()
  {
    return styleNames;
  }

  public void setupStyles(Map<String, RichStyle> styleMap, RichStyle defaultStyle, StyleObserver observer)
  {
    if(style.hasObserver(observer))
      return;
    
    for(String styleName : styleNames)
    {
      RichStyle extension = styleMap.get(styleName);
      if(extension != null)
        style.addExtension(extension);
    }
    style.addExtension(defaultStyle);
    style.addObserver(observer);
  }

  public void teardownStyles()
  {
    style.tearDown();
  }

  public RichStyle getStyle()
  {
    return style;
  }

  public Font getFont()
  {
    return Fonts.fromStyle(style);
  }

  public Color getColor()
  {
    return style.getCompiledTextColor().getColor();
  }

  public String toString()
  {
    return text + "(font: " + getFont() + ", color: " + getColor() + ")";
  }
}
