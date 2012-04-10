//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs.painting;

import limelight.Log;
import limelight.styles.Style;
import limelight.ui.text.TypedLayout;

import java.awt.*;

public class TextPanelTextPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelTextPainter();

  private TextPanelTextPainter()
  {
  }

  @Override
  public void paint(Graphics2D graphics, limelight.ui.model.text.TextModel model)
  {
    if(!model.hasText())
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
