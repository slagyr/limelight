package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnAltShiftKeyProcessor extends KeyProcessor
{
  public SelectionOnAltShiftKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    if (isACharacter(keyCode))
    {
      modelInfo.deleteSelection();
      insertCharIntoTextBox(event.getKeyChar());
    }
    else if (isMoveRightEvent(keyCode))
      modelInfo.setCursorIndex(findNearestWordToTheRight());

    else if (isMoveLeftEvent(keyCode))
      modelInfo.setCursorIndex(findNearestWordToTheLeft());

  }
}
