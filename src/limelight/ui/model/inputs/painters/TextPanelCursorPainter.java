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

  @Override
  public void paint(Graphics2D graphics)
  {
    int x = boxInfo.getXPosFromIndex(boxInfo.cursorIndex);
    graphics.setColor(Color.black);    
    if(boxInfo.isCursorOn())
      graphics.drawLine(x, HEIGHT_MARGIN, x, boxInfo.getPanelHeight() - HEIGHT_MARGIN -1);
  }
}
