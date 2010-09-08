//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.text.TextLocation;

public class NormalKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new NormalKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();
    if (keyCode == KeyEvent.KEY_RIGHT && canMoveRight(model))
      model.moveCaret(+1);
    else if (keyCode == KeyEvent.KEY_LEFT && canMoveLeft(model))
      model.moveCaret(-1);
    else if (keyCode == KeyEvent.KEY_BACK_SPACE)  //TODO MDM Need to also support delete key
    {
      if(TextLocation.origin.before(model.getCaretLocation()))
      {
        TextLocation end = model.getCaretLocation();
        TextLocation start = end.moved(model.getLines(), -1);
        model.deleteEnclosedText(start, end);
      }
    }
  }

}
