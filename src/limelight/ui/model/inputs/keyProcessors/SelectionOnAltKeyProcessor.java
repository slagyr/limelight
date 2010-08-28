//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class SelectionOnAltKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnAltKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();

//    if (isACharacter(keyCode))
//    {
//      boxInfo.deleteSelection();
//      boxInfo.insertChar(event.getKeyChar());
//    }
    if (model.isMoveRightEvent(keyCode))
    {
      model.setCaretIndex(model.findNearestWordToTheRight());
      model.setSelectionOn(false);
    }

    else if (model.isMoveLeftEvent(keyCode))
    {
      model.setCaretIndex(model.findNearestWordToTheLeft());
      model.setSelectionOn(false);
    }

  }
}
