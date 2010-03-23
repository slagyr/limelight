package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelCursorPainter extends TextPanelPainter
{
  public TextPanelCursorPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void paint(Graphics2D graphics)
  {
    int x = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex());
    int y = boxInfo.getTopOfStartPositionForCursor();
    graphics.setColor(Color.black);    
    if(boxInfo.isCursorOn())
    {
      int cursorBottom = boxInfo.getBottomPositionForCursor();
      graphics.drawLine(x, y, x, cursorBottom);
    }
  }
}
