//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import limelight.ui.model.Drawable;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelBackgroundPainter extends TextPanelPainter
{
  private Drawable normalDrawable;
  private Drawable focusDrawable;

  public TextPanelBackgroundPainter(TextModel boxInfo, Drawable normalDrawable, Drawable focusDrawable)
  {
    super(boxInfo);
    this.normalDrawable = normalDrawable;
    this.focusDrawable = focusDrawable;
  }

  public void paint(Graphics2D graphics)
  {
    Dimension dimension = new Dimension(boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
    normalDrawable.draw(graphics, 0, 0, dimension.width, dimension.height);
    if(boxInfo.isFocused())
      focusDrawable.draw(graphics, 0, 0, dimension.width, dimension.height);
  }
}
