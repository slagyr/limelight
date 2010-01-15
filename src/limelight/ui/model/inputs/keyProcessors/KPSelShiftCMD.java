package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPSelShiftCMD extends KeyProcessor
{
  public KPSelShiftCMD(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if(isMoveRightEvent(keyCode)) {
      boxInfo.cursorIndex = boxInfo.getText().length();
    }
    else if(isMoveLeftEvent(keyCode)) {
      boxInfo.cursorIndex = 0;
    }
  }
}
