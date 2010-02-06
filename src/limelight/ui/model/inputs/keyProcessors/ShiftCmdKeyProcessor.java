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

    if(modelInfo.isMoveRightEvent(keyCode)){
      modelInfo.initSelection();
      modelInfo.sendCursorToEndOfLine();
    }
    else if(modelInfo.isMoveLeftEvent(keyCode)){
      modelInfo.initSelection();
      modelInfo.sendCursorToStartOfLine();
    }
    else if(keyCode == KeyEvent.VK_UP)
    {
      modelInfo.initSelection();
      modelInfo.setCursorIndex(0);
    }
    else if(keyCode == KeyEvent.VK_DOWN)
    {
      modelInfo.initSelection();
      modelInfo.setCursorIndex(modelInfo.getText().length());
    }
  }

}
