//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelCursorPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelCursorPainter();

  private TextPanelCursorPainter()
  {
  }

  public void paint(Graphics2D graphics, TextModel boxInfo)
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
