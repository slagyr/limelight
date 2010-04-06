//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedSelectionOnShiftKeyProcessor extends KeyProcessor
{
  public ExpandedSelectionOnShiftKeyProcessor(TextModel modelInfo)
  {
    super(modelInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    KeyProcessor basicSelectionShiftProcessor = new SelectionOnShiftKeyProcessor(modelInfo);
    int keyCode = event.getKeyCode();

    if(modelInfo.isMoveUpEvent(keyCode))
      modelInfo.moveCursorUpALine();
    else if(modelInfo.isMoveDownEvent(keyCode))
      modelInfo.moveCursorDownALine();
    else
      basicSelectionShiftProcessor.processKey(event);
  }
}
