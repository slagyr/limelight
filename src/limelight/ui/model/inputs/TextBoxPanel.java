//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;

// TODO MDM - Need to support password fields

public class TextBoxPanel extends TextInputPanel
{
  @Override
  protected TextModel createModel()
  {
    return new SingleLineTextModel(this);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 150);
    style.setDefault(Style.HEIGHT, 28);
    style.setDefault(Style.VERTICAL_ALIGNMENT, "center");
    style.setDefault(Style.CURSOR, "text");
    style.setDefault(Style.BACKGROUND_COLOR, "white");
    setBorderStyleDefaults(style);
    setPaddingDefaults(style);
  }
}
