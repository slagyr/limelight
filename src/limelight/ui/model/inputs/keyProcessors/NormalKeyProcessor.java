//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class NormalKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new NormalKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();
    if (model.isMoveRightEvent(keyCode))
      model.moveCaret(+1);
    else if (model.isMoveLeftEvent(keyCode))
      model.moveCaret(-1);
    else if (keyCode == KeyEvent.KEY_BACK_SPACE)  //TODO MDM Need to also support delete key
    {
      if(model.getCaretIndex() > 0)
        model.deleteEnclosedText(model.getCaretIndex() -1, model.getCaretIndex());
    }
  }

}
