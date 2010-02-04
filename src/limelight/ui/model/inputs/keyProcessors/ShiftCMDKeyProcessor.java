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
      boxInfo.setCursorIndex(boxInfo.getText().length());
    }
    else if(isMoveLeftEvent(keyCode)){
      initSelection();
      boxInfo.setCursorIndex(0);
    }
  }

}
