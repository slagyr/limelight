//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.util.Util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StyledText
{
  private String text;
  private String style;
  private List<String> parentStyles;

  public StyledText(String text, String style, List<String> parentStyles)
  {
    setText(text);
    this.style = style;
    this.parentStyles = new LinkedList<String>(parentStyles);
    Collections.reverse(this.parentStyles);
  }

  public StyledText(String text, String style)
  {
    this(text, style, new LinkedList<String>());
  }

  public boolean equals(Object other)
  {
    if(other == this)
    {
      return true;
    }

    if(!(other instanceof StyledText))
    {
      return false;
    }

    StyledText otherText = (StyledText)other;
    return Util.equal(text, otherText.getText()) &&
            Util.equal(style, otherText.getStyleName()) &&
            Util.equal(parentStyles, otherText.parentStyles);
  }

  public String getText()
  {
    return text;
  }

  public String getStyleName()
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

  public List<String> getParentStyles()
  {
    return parentStyles;
  }
}
