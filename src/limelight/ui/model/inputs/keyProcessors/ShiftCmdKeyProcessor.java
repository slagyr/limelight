package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ShiftCmdKeyProcessor extends KeyProcessor
{
  public ShiftCmdKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    
    if(isMoveRightEvent(keyCode)){
      initSelection();
      modelInfo.setCursorIndex(modelInfo.getText().length());
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      modelInfo.setCursorIndex(0);
    }
  }

}
