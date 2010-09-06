//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.model.inputs.keyProcessors.*;
import limelight.util.Colors;

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

  public KeyProcessor getKeyProcessorFor(int modifiers)
  {
    if(getModel().isSelectionOn())
    {
      switch(modifiers)
      {
        case 0:
          return SelectionOnKeyProcessor.instance;
        case 1:
          return SelectionOnShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7:
          return SelectionOnShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6:
          return SelectionOnCmdKeyProcessor.instance;
        case 8:
          return SelectionOnAltKeyProcessor.instance;
        case 9:
          return SelectionOnAltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14:
          return SelectionOnAltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15:
          return SelectionOnAltShiftCmdKeyProcessor.instance;
      }
    }
    else
    {
      switch(modifiers)
      {
        case 0:
          return NormalKeyProcessor.instance;
        case 1:
          return ShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7:
          return ShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6:
          return CmdKeyProcessor.instance;
        case 8:
          return AltKeyProcessor.instance;
        case 9:
          return AltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14:
          return AltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15:
          return AltShiftCmdKeyProcessor.instance;

      }
    }
    throw new RuntimeException("Unexpected key modifiers: " + modifiers);
  }
}
