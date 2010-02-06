package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnKeyProcessor extends KeyProcessor
{
  public SelectionOnKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    modelInfo.selectionOn = false;
    int keyCode = event.getKeyCode();
    if (isMoveRightEvent(keyCode))
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() + 1);
    else if (isMoveLeftEvent(keyCode))
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() - 1);
    else if (isACharacter(keyCode))
    {
      modelInfo.deleteSelection();
      insertCharIntoTextBox(event.getKeyChar());
    }
    else if (keyCode == KeyEvent.VK_BACK_SPACE)
      modelInfo.deleteSelection();

  }
}
