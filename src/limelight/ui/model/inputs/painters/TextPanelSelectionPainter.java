package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelSelectionPainter extends TextPanelPainter
{
  public TextPanelSelectionPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void paint(Graphics2D graphics)
  {
    if (boxInfo.text != null && boxInfo.text.length() > 0)
    {
      int x1 = boxInfo.getXPosFromIndex(boxInfo.cursorIndex);
      int x2 = boxInfo.getXPosFromIndex(boxInfo.selectionIndex);
      if (boxInfo.selectionOn)
      {
        graphics.setColor(Color.cyan);
        if (x1 > x2)
          graphics.fillRect(x2, HEIGHT_MARGIN, x1 - x2, boxInfo.getPanelHeight() - HEIGHT_MARGIN * 2);
        else
          graphics.fillRect(x1, HEIGHT_MARGIN, x2 - x1, boxInfo.getPanelHeight() - HEIGHT_MARGIN * 2);
      }
    }
  }
}
