//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnAltKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnAltKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    int keyCode = event.getKeyCode();

    if (isACharacter(keyCode))
    {
      boxInfo.deleteSelection();
      boxInfo.insertCharIntoTextBox(event.getKeyChar());
    }
    else if (boxInfo.isMoveRightEvent(keyCode))
    {
      boxInfo.setCaretIndex(boxInfo.findNearestWordToTheRight());
      boxInfo.setSelectionOn(false);
    }

    else if (boxInfo.isMoveLeftEvent(keyCode))
    {
      boxInfo.setCaretIndex(boxInfo.findNearestWordToTheLeft());
      boxInfo.setSelectionOn(false);
    }

  }
}
