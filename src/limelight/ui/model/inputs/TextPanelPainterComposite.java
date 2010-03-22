package limelight.ui.model.inputs;

import limelight.ui.model.inputs.painters.*;
import limelight.util.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextPanelPainterComposite
{
  private TextPanelPainter cursorPainter;
  private TextPanelPainter panelBackgroundPainter;
  private TextPanelPainter selectionPainter;
  private TextPanelPainter textPainter;

  public TextPanelPainterComposite(TextModel boxInfo)
  {
    cursorPainter = new TextPanelCursorPainter(boxInfo);
    panelBackgroundPainter = new TextPanelBackgroundPainter(boxInfo);
    selectionPainter = new TextPanelSelectionPainter(boxInfo);
    textPainter = new TextPanelTextPainter(boxInfo);
  }

  public void paint(Graphics2D graphics)
  {
    panelBackgroundPainter.paint(graphics);
    selectionPainter.paint(graphics);
    textPainter.paint(graphics);
    cursorPainter.paint(graphics);
  }

  public TextPanelPainter getCursorPainter()
  {
    return cursorPainter;
  }

  public TextPanelPainter getPanelBackgroundPainter()
  {
    return panelBackgroundPainter;
  }

  public TextPanelPainter getSelectionPainter()
  {
    return selectionPainter;
  }

  public TextPanelPainter getTextPainter()
  {
    return textPainter;
  }

  public void setCursorPainter(TextPanelPainter cursorPainter)
  {
    this.cursorPainter = cursorPainter;
  }

  public void setPanelBackgroundPainter(TextPanelPainter panelBackgroundPainter)
  {
    this.panelBackgroundPainter = panelBackgroundPainter;
  }

  public void setSelectionPainter(TextPanelPainter selectionPainter)
  {
    this.selectionPainter = selectionPainter;
  }

  public void setTextPainter(TextPanelPainter textPainter)
  {
    this.textPainter = textPainter;
  }
}
