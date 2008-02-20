package limelight.ui;

import limelight.ui.TextPane;
import limelight.ui.TextAccessor;
import limelight.ui.Panel;
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
        throw new LimelightException("You may only set text on empty blocks.");
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
