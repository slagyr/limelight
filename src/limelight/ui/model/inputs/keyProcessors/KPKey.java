package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPKey extends KeyProcessor
{

  public KPKey(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    if (isACharacter(keyCode))
    {
      insertLowercaseCharIntoTextBox(keyCode);
    }
    else if (isMoveRightEvent(keyCode))
      boxInfo.cursorIndex++;
    else if (isMoveLeftEvent(keyCode))
      boxInfo.cursorIndex--;
    else if (keyCode == KeyEvent.VK_BACK_SPACE && boxInfo.cursorIndex > 0)
      boxInfo.deleteEnclosedText(boxInfo.cursorIndex -1, boxInfo.cursorIndex);
  }

}
