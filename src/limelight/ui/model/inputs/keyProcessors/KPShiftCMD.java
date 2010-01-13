package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPShiftCMD extends KeyProcessor
{
  public KPShiftCMD(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if(isMoveRightEvent(keyCode)){
      initSelection();
      boxInfo.cursorIndex = boxInfo.text.length();
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      boxInfo.cursorIndex = 0;
    }
  }

}
