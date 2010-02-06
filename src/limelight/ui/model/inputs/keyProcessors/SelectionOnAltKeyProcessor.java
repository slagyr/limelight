package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnAltKeyProcessor extends KeyProcessor
{
  public SelectionOnAltKeyProcessor(TextModel boxInfo)
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
    {
      modelInfo.setCursorIndex(findNearestWordToTheRight());
      modelInfo.selectionOn = false;
    }

    else if (isMoveLeftEvent(keyCode))
    {
      modelInfo.setCursorIndex(findNearestWordToTheLeft());
      modelInfo.selectionOn = false;
    }

  }
}
