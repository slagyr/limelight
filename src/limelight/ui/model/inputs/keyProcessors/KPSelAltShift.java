package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPSelAltShift extends KeyProcessor
{
  public KPSelAltShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if (isMoveRightEvent(keyCode))
      boxInfo.cursorIndex = findNearestWordToTheRight();

    else if (isMoveLeftEvent(keyCode))
      boxInfo.cursorIndex = findNearestWordToTheLeft();

  }
}
