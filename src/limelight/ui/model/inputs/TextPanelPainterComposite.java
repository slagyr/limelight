package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.painters.*;
import limelight.util.Box;
import sun.text.Normalizer;

import java.awt.*;

public class TextPanelPainterComposite
{
  private TextPanelPainter cursorPainter;
  private TextPanelPainter boxPainter;
  private TextPanelPainter selectionPainter;
  private TextPanelPainter textPainter;

  public TextPanelPainterComposite(TextModel boxInfo)
  {
    cursorPainter = new TextPanelCursorPainter(boxInfo);
    boxPainter = new TextPanelBoxPainter(boxInfo);
    selectionPainter = new TextPanelSelectionPainter(boxInfo);
    textPainter = new TextPanelTextPainter(boxInfo);
  }

  public void paint(Graphics2D graphics)
  {
    boxPainter.paint(graphics);
    selectionPainter.paint(graphics);
    textPainter.paint(graphics);
    cursorPainter.paint(graphics);
  }

  public TextPanelPainter getCursorPainter()
  {
    return cursorPainter;
  }

  public TextPanelPainter getBoxPainter()
  {
    return boxPainter;
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

  public void setBoxPainter(TextPanelPainter boxPainter)
  {
    this.boxPainter = boxPainter;
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
