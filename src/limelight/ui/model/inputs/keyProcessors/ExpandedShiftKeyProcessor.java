//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedShiftKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new ExpandedShiftKeyProcessor();

  @Override
  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    KeyProcessor basicShiftProcessor = ShiftKeyProcessor.instance;
    int keyCode = event.getKeyCode();

    if(boxInfo.isMoveUpEvent(keyCode))
    {
      boxInfo.initSelection();
      boxInfo.moveCaretUpALine();
    }
    else if(boxInfo.isMoveDownEvent(keyCode))
    {
      boxInfo.initSelection();
      boxInfo.moveCaretDownALine();

    }
    else
      basicShiftProcessor.processKey(event, boxInfo);
  }
}
