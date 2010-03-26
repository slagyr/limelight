//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.styles.StyleObserver;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StyledText
{
  private String text;
  private LinkedList<String> styles;
  private RichStyle style;

  public StyledText(String text, LinkedList<String> styles)
  {
    setText(text);
    style = new RichStyle();
    this.styles = styles;
  }

  public StyledText(String text, String... styles)
  {
    this(text, new LinkedList<String>());
    for(String style : styles)
      this.styles.add(style);
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public List<String> getStyles()
  {
    return styles;
  }

  public void setupStyles(Map<String, Style> styleMap, RichStyle defaultStyle, StyleObserver observer)
  {
    for(String styleName : styles)
    {
      RichStyle extension = (RichStyle) styleMap.get(styleName);
      if(extension != null)
        style.addExtension(extension);
    }
    style.addExtension(defaultStyle);
    style.addObserver(observer);
  }

  public void teardownStyles()
  {
    style.clearExtensions();
  }

  public RichStyle getStyle()
  {
    return style;
  }

  public Font getFont()
  {
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
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
