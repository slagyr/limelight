//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnShiftCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnShiftCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    int keyCode = event.getKeyCode();

    if (boxInfo.isMoveRightEvent(keyCode))
    {
      boxInfo.sendCursorToEndOfLine();
    }
    else if (boxInfo.isMoveLeftEvent(keyCode))
    {
      boxInfo.sendCursorToStartOfLine();
    }
    else if (keyCode == KeyEvent.VK_UP)
    {
      boxInfo.setCaretIndex(0);
    }
    else if (keyCode == KeyEvent.VK_DOWN)
    {
      boxInfo.setCaretIndex(boxInfo.getText().length());
    }
  }
}
