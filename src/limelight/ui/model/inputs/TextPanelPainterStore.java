package limelight.ui.model.inputs;

import limelight.ui.model.inputs.painters.*;
import sun.text.Normalizer;

public class TextPanelPainterStore
{
  private TextPanelCursorPainter cursorPainter;
  private TextPanelBoxPainter boxPainter;
  private TextPanelSelectionPainter selectionPainter;
  private TextPanelTextPainter textPainter;

  public TextPanelPainterStore(TextModel boxInfo)
    {
      cursorPainter = new TextPanelCursorPainter(boxInfo);
      boxPainter = new TextPanelBoxPainter(boxInfo);
      selectionPainter = new TextPanelSelectionPainter(boxInfo);
      textPainter = new TextPanelTextPainter(boxInfo);
    }

  public TextPanelCursorPainter getCursorPainter()
  {
    return cursorPainter;
  }

  public TextPanelBoxPainter getBoxPainter()
  {
    return boxPainter;
  }

  public TextPanelSelectionPainter getSelectionPainter()
  {
    return selectionPainter;
  }

  public TextPanelTextPainter getTextPainter()
  {
    return textPainter;
  }
}
