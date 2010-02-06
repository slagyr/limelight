package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnShiftKeyProcessor extends KeyProcessor
{
  public SelectionOnShiftKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    
    if(isMoveRightEvent(keyCode)){
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() + 1);
    }
    else if(isMoveLeftEvent(keyCode)){
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() - 1);
    }
    else if(isACharacter(keyCode)){
      modelInfo.deleteSelection();
      insertCharIntoTextBox(event.getKeyChar());
    }
  }
}
