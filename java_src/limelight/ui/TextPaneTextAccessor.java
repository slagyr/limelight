package limelight.ui;

import limelight.LimelightException;

public class TextPaneTextAccessor implements TextAccessor
{
  private Panel panel;
  private TextPanel textPanel;

  public TextPaneTextAccessor(Panel panel)
  {
    this.panel = panel;
  }

  public void setText(String text) throws LimelightException
  {
    if(textPanel == null)
    {
      if(text == null || text.length() == 0)
        return;
      if(panel.getChildren().size() > 0)
        throw new LimelightException("You may only set text on empty blocks.");
      textPanel = new TextPanel(panel, text);
      panel.add(textPanel);
      panel.sterilize();
    }
    else
      textPanel.setText(text);
  }

  public String getText()
  {
    return textPanel == null ? "" : textPanel.getText();
  }
}

