//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightError;

public class TempTextAccessor implements TextAccessor
{
  private static TextAccessor instance;

  static
  {
    instance = new TempTextAccessor();
  }

  public static TextAccessor instance()
  {
    return instance;
  }

  private TempTextAccessor()
  {
  }

  public void setText(String text, PropablePanel panel) throws LimelightError
  {
    if(text == null || text.length() == 0)
      return;
    if(panel.getTextAccessor() != this)
      throw new LimelightError("You may only set text on empty props.");
    TextPanel textPanel = new TextPanel(panel, text);
    panel.add(textPanel);
    panel.sterilize();
    panel.setTextAccessor(textPanel);
  }

  public String getText()
  {
    return "";
  }

  public void markAsDirty()
  {
  }

  public void markAsNeedingLayout()
  {
  }

  public boolean hasFocus()
  {
    return false;
  }
}
