//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.model.TextPane;
import limelight.ui.model.TextAccessor;
import limelight.ui.model.Panel;
import limelight.LimelightException;

public class TextPaneTextAccessor implements TextAccessor
{
  private Panel panel;
  private TextPane textPane;

  public TextPaneTextAccessor(Panel panel)
  {
    this.panel = panel;
  }

  public void setText(String text) throws LimelightException
  {
    if(textPane == null)
    {
      if(text == null || text.length() == 0)
        return;
      if(panel.getComponents().length > 0)
        throw new LimelightException("You may only set text on empty props.");
      textPane = new TextPane(panel, text);
      panel.add(textPane);
      panel.sterilize();
    }
    else
      textPane.setText(text);
  }

  public String getText()
  {
    return textPane == null ? "" : textPane.getText();
  }
}
