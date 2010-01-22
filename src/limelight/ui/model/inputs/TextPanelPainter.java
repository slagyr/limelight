package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;

import java.awt.*;

public abstract class TextPanelPainter
{
  public static final int HEIGHT_MARGIN = 4;
  protected TextModel boxInfo;
  public TextPanelPainter(TextModel boxInfo)
  {
    this.boxInfo = boxInfo;
  }

  abstract public void paint(Graphics2D graphics);
}
