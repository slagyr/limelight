//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.text.TextLocation;

public class ShiftCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new ShiftCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();

    if(model.isMoveRightEvent(keyCode))
    {
      model.startSelection(model.getCaretLocation());
      model.sendCaretToEndOfLine();
    }
    else if(model.isMoveLeftEvent(keyCode))
    {
      model.startSelection(model.getCaretLocation());
      model.sendCursorToStartOfLine();
    }
    else if(keyCode == KeyEvent.KEY_UP)
    {
      model.startSelection(model.getCaretLocation());
      model.setCaretLocation(TextLocation.origin);
    }
    else if(keyCode == KeyEvent.KEY_DOWN)
    {
      model.startSelection(model.getCaretLocation());
      model.setCaretLocation(model.getEndLocation());
    }
  }

}
