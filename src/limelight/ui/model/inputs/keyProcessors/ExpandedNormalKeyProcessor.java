package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedNormalKeyProcessor extends KeyProcessor
{
  public ExpandedNormalKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    if (isACharacter(keyCode) || isAnExtraKey(keyCode))
      insertCharIntoTextBox(event.getKeyChar());
    else if (isMoveRightEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() + 1);
    else if (isMoveLeftEvent(keyCode))
      boxInfo.setCursorIndex(boxInfo.getCursorIndex() - 1);
    else if (keyCode == KeyEvent.VK_BACK_SPACE && boxInfo.getCursorIndex() > 0)
      boxInfo.deleteEnclosedText(boxInfo.getCursorIndex() -1, boxInfo.getCursorIndex());
    else if (isMoveUpEvent(keyCode))
      moveCursorUpALine();
    else if (isMoveDownEvent(keyCode))
      moveCursorDownALine();
  }

}
