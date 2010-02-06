package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class AltShiftKeyProcessor extends KeyProcessor
{
  public AltShiftKeyProcessor(TextModel boxInfo)
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
    else if(isMoveRightEvent(keyCode)){
      initSelection();
      modelInfo.setCursorIndex(findNearestWordToTheRight());
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      modelInfo.setCursorIndex(findNearestWordToTheLeft());
    }
  }
}
