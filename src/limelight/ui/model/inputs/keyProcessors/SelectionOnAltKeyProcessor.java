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
      modelInfo.insertCharIntoTextBox(event.getKeyChar());
    }
    else if (modelInfo.isMoveRightEvent(keyCode))
    {
      modelInfo.setCursorIndex(modelInfo.findNearestWordToTheRight());
      modelInfo.selectionOn = false;
    }

    else if (modelInfo.isMoveLeftEvent(keyCode))
    {
      modelInfo.setCursorIndex(modelInfo.findNearestWordToTheLeft());
      modelInfo.selectionOn = false;
    }

  }
}
