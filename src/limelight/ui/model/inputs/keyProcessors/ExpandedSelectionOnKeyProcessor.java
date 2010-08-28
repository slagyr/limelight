//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class ExpandedSelectionOnKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new ExpandedSelectionOnKeyProcessor();

  // TODO MDM - DELETE ME, I duplicate the non-selectionOn version
  @Override
  public void processKey(KeyEvent event, TextModel model)
  {
    model.setSelectionOn(false);
    int keyCode = event.getKeyCode();
//    if (keyCode == KeyEvent.KEY_ENTER || keyCode == KeyEvent.KEY_TAB){
//      boxInfo.deleteSelection();
//      boxInfo.insertChar(event.getKeyChar());
//    }
    if (model.isMoveUpEvent(keyCode))
      model.moveCaretUpALine();
    else if (model.isMoveDownEvent(keyCode))
      model.moveCaretDownALine();
    else
      SelectionOnKeyProcessor.instance.processKey(event, model);
  }
}
