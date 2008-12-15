//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.util.Util;

public class StyledText
{
  private String text;
  private String style;

  public boolean equals(Object other)
  {
    if(other instanceof StyledText)
    {
      StyledText otherText = (StyledText)other;
      return Util.equal(text, otherText.getText()) && Util.equal(style, otherText.getStyle());
    }
    return false;
  }

  public StyledText(String text, String style)
  {
    setText(text);
    setStyle(style);
  }

  public String getText()
  {
    return text;
  }

  public String getStyle()
  {
    return style;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public void setText(String text)
  {
    this.text = text;
  }

}
