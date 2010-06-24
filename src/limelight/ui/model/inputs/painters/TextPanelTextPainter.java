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
  public void paint(Graphics2D graphics, TextModel model)
  {
    if(model.getText() == null || model.getText().length() == 0)
      return;

    float x = model.getXOffset();
    float y = model.getYOffset();

    graphics.setColor(model.getPanel().getStyle().getCompiledTextColor().getColor());
    for(TypedLayout layout : model.getTypedLayouts())
    {
      y += layout.getAscent();
      layout.draw(graphics, x, y + 1);
      y += layout.getDescent() + layout.getLeading();

    }
  }

}
