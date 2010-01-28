package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class KPSelAlt extends KeyProcessor
{
  public KPSelAlt(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {

    if (isMoveRightEvent(keyCode))
    {
      boxInfo.setCursorIndex(findNearestWordToTheRight());
      boxInfo.selectionOn = false;
    }

    else if (isMoveLeftEvent(keyCode))
    {
      boxInfo.setCursorIndex(findNearestWordToTheLeft());
      boxInfo.selectionOn = false;
    }

  }
}
