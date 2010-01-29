package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPSelAlt extends KeyProcessor
{
  public KPSelAlt(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();

    if (isACharacter(keyCode))
    {
      boxInfo.deleteSelection();
      insertCharIntoTextBox(event.getKeyChar());
    }
    else if (isMoveRightEvent(keyCode))
    {
      boxInfo.setCursorIndex(findNearestWordToTheRight());
      boxInfo.selectionOn = false;
    }

    else if (isMoveLeftEvent(keyCode))
    {
      boxInfo.setCursorIndex(findNearestWordToTheLeft());
      boxInfo.selectionOn = false;
    }

  }
}
