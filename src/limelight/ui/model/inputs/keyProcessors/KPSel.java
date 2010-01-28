package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPSel extends KeyProcessor
{
  public KPSel(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    boxInfo.selectionOn = false;

    if (isMoveRightEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    else if (isMoveLeftEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    else if (isACharacter(keyCode))
    {
      boxInfo.deleteSelection();
      insertLowercaseCharIntoTextBox(keyCode);
    }
    else if (keyCode == KeyEvent.VK_BACK_SPACE)
      boxInfo.deleteSelection();

  }
}
