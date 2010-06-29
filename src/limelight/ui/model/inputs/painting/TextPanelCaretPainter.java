//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.ui.model.inputs.TextModel;
import limelight.util.Box;

import java.awt.*;

public class TextPanelCaretPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelCaretPainter();

  private TextPanelCaretPainter()
  {
  }

  public void paint(Graphics2D graphics, TextModel boxInfo)
  {
    if(!boxInfo.isCursorOn())
      return;

    Box caret = boxInfo.getCaretShape();
    graphics.setColor(Color.black);
    graphics.fill(caret);
  }
}
