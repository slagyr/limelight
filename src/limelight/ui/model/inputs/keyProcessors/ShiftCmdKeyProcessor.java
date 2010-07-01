//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ShiftCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new ShiftCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    int keyCode = event.getKeyCode();

    if(boxInfo.isMoveRightEvent(keyCode)){
      boxInfo.initSelection();
      boxInfo.sendCaretToEndOfLine();
    }
    else if(boxInfo.isMoveLeftEvent(keyCode)){
      boxInfo.initSelection();
      boxInfo.sendCursorToStartOfLine();
    }
    else if(keyCode == KeyEvent.VK_UP)
    {
      boxInfo.initSelection();
      boxInfo.setCaretIndex(0);
    }
    else if(keyCode == KeyEvent.VK_DOWN)
    {
      boxInfo.initSelection();
      boxInfo.setCaretIndex(boxInfo.getText().length());
    }
  }

}
