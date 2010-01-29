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

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    if (isACharacter(keyCode))
    {
      insertCharIntoTextBox(event.getKeyChar());
    }
    else if (isMoveRightEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    else if (isMoveLeftEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    else if (keyCode == KeyEvent.VK_BACK_SPACE && boxInfo.getCursorIndex() > 0)
      boxInfo.deleteEnclosedText(boxInfo.getCursorIndex() -1, boxInfo.getCursorIndex());
  }

}
