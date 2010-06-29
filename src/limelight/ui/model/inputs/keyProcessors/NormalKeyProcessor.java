//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class NormalKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new NormalKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();
    if (isACharacter(keyCode))
      model.insertChar(event.getKeyChar());
    else if (model.isMoveRightEvent(keyCode))
      model.setCaretIndex(model.getCaretIndex() + 1);
    else if (model.isMoveLeftEvent(keyCode))
      model.setCaretIndex(model.getCaretIndex() - 1);
    else if (keyCode == KeyEvent.VK_BACK_SPACE && model.getCaretIndex() > 0)  //TODO MDM Need to also support delete key
      model.deleteEnclosedText(model.getCaretIndex() -1, model.getCaretIndex());
  }

}
