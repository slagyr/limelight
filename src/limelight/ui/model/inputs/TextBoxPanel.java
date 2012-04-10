//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.model.text.SingleLineTextModel;
import limelight.ui.model.text.TextModel;
import limelight.ui.model.text.masking.IdentityMask;
import limelight.ui.model.text.masking.PasswordMask;
import limelight.ui.model.text.masking.TextMask;

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

  public void setInPasswordMode(boolean passwordMode)
  {
    TextMask newMask = passwordMode ? PasswordMask.instance : IdentityMask.instance;
    final TextModel model = getModel();
    if(model.getMask() != newMask)
    {
      model.setMask(newMask);
      model.clearCache();
      markAsDirty();
    }
  }

  public boolean isInPasswordMode()
  {
    return getModel().getMask() == PasswordMask.instance;
  }
}
