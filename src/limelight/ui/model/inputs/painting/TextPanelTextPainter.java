//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.styles.Style;
import limelight.styles.abstrstyling.HorizontalAlignmentValue;
import limelight.ui.text.TypedLayout;
import limelight.ui.model.inputs.TextModel;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

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

    float y = model.getYOffset();

    Style style = model.getContainer().getStyle();
    graphics.setColor(style.getCompiledTextColor().getColor());

    for(TypedLayout line : model.getLines())
    {      
      int x = model.getXOffset(line);
      y += line.getAscent();
      line.draw(graphics, x, y);
      y += line.getDescent() + line.getLeading();
    }
  }

}
