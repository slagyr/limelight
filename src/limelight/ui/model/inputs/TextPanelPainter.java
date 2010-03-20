package limelight.ui.model.inputs;

import java.awt.*;

public abstract class TextPanelPainter
{
  //TODO Is this the same thing as TextModel.TOP_MARGIN 
  public static final int HEIGHT_MARGIN = 7;
  protected TextModel boxInfo;
  public boolean hasPainted;
  public TextPanelPainter(TextModel boxInfo)
  {
    this.boxInfo = boxInfo;
  }

  abstract public void paint(Graphics2D graphics);
}
