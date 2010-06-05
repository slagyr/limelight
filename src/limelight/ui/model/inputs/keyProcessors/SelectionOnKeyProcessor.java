//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    boxInfo.selectionOn = false;
    int keyCode = event.getKeyCode();
    if (boxInfo.isMoveRightEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    else if (boxInfo.isMoveLeftEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    else if (isACharacter(keyCode))
    {
      boxInfo.deleteSelection();
      boxInfo.insertCharIntoTextBox(event.getKeyChar());
    }
    else if (keyCode == KeyEvent.VK_BACK_SPACE)
      boxInfo.deleteSelection();

  }
}
