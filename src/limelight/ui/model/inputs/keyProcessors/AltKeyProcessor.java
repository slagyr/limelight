//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class AltKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new AltKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    int keyCode = event.getKeyCode();
    if(model.isMoveRightEvent(keyCode))
      model.setCaretLocation(model.findNearestWordToTheRight());
    else if(model.isMoveLeftEvent(keyCode))
      model.setCaretLocation(model.findNearestWordToTheLeft());
  }

}
