package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPSelShift extends KeyProcessor
{
  public KPSelShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if(isMoveRightEvent(keyCode)){
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    }
    else if(isMoveLeftEvent(keyCode)){
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    }
    else if(isACharacter(keyCode)){
      boxInfo.deleteSelection();
      insertUppercaseCharIntoTextBox(keyCode);
    }
  }
}
