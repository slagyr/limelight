//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class ExpandedSelectionOnShiftKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new ExpandedSelectionOnShiftKeyProcessor();

  
  @Override
  public void processKey(KeyEvent event, TextModel model)
  {
    KeyProcessor basicSelectionShiftProcessor = SelectionOnShiftKeyProcessor.instance;
    int keyCode = event.getKeyCode();

    if(keyCode == KeyEvent.KEY_UP && hasLineAboveCaret(model))
      model.moveCaretUpALine();
    else if(keyCode == KeyEvent.KEY_DOWN && hasLineBelowCaret(model))
      model.moveCaretDownALine();
    else
      basicSelectionShiftProcessor.processKey(event, model);
  }


}
