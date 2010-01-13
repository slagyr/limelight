package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPShift extends KeyProcessor
{
  public KPShift(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if (isACharacter(keyCode))
      insertCharIntoTextBox(KeyEvent.getKeyText(keyCode).charAt(0));
    else if(isMoveRightEvent(keyCode)){
      initSelection();
      boxInfo.cursorIndex++;
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      boxInfo.cursorIndex--;
    }
  }
}
