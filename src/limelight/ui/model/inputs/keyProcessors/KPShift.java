package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPShift extends KeyProcessor
{
  public KPShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if (isACharacter(keyCode))
      insertUppercaseCharIntoTextBox(keyCode);
    else if(isMoveRightEvent(keyCode)){
      initSelection();
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    }
  }
}
