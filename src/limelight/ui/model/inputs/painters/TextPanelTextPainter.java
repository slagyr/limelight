//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import limelight.ui.TypedLayout;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelTextPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelTextPainter();

  private TextPanelTextPainter()
  {
  }

  @Override
  public void paint(Graphics2D graphics, TextModel boxInfo)
  {
    if(boxInfo.getText() == null || boxInfo.getText().length() == 0)
      return;

//    boxInfo.setOffset(boxInfo.calculateXOffset(), boxInfo.calculateYOffset());

    float textX = boxInfo.getXOffset();//boxInfo.getXAlignmentOffset();
    float textY = boxInfo.getYAlignmentOffset();

    graphics.setColor(boxInfo.getPanel().getStyle().getCompiledTextColor().getColor());
    for(TypedLayout layout : boxInfo.getTypedLayouts())
    {
      textY += layout.getAscent();
      layout.draw(graphics, textX, textY + 1);
      textY += layout.getDescent() + layout.getLeading();

    }
  }

}
