//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.LimelightException;

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

  public void setText(String text, Prop panel) throws LimelightException
  {
    if(text == null || text.length() == 0)
      return;
    if(panel.getTextAccessor() != this)
      throw new LimelightException("You may only set text on empty props.");
    TextPanel textPanel = new TextPanel(panel);
    panel.add(textPanel);
    panel.sterilize();
    panel.setTextAccessor(textPanel);
    textPanel.setText(text, panel);
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
