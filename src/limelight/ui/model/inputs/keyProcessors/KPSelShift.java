package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPSelShift extends KeyProcessor
{
  public KPSelShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if(isMoveRightEvent(keyCode)){
      boxInfo.cursorIndex ++;
    }
    else if(isMoveLeftEvent(keyCode)){
      boxInfo.cursorIndex --;
    }
    else if(isACharacter(keyCode)){
      boxInfo.deleteSelection();
      insertUppercaseCharIntoTextBox(keyCode);
    }
  }
}
