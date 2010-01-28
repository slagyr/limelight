package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPAltShift extends KeyProcessor
{
  public KPAltShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if(isMoveRightEvent(keyCode)){
      initSelection();
      boxInfo.setCursorIndex(findNearestWordToTheRight());
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      boxInfo.setCursorIndex(findNearestWordToTheLeft());
    }
  }
}
