//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class SelectionOnShiftCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnShiftCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();

    if (model.isMoveRightEvent(keyCode))
    {
      model.sendCaretToEndOfLine();
    }
    else if (model.isMoveLeftEvent(keyCode))
    {
      model.sendCursorToStartOfLine();
    }
    else if (keyCode == KeyEvent.KEY_UP)
    {
      model.setCaretIndex(0);
    }
    else if (keyCode == KeyEvent.KEY_DOWN)
    {
      model.setCaretIndex(model.getText().length());
    }
  }
}
