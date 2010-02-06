package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnShiftCmdKeyProcessor extends KeyProcessor
{
  public SelectionOnShiftCmdKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();

    if(isMoveRightEvent(keyCode)) {
      modelInfo.setCursorIndex(modelInfo.getText().length());
    }
    else if(isMoveLeftEvent(keyCode)) {
      modelInfo.setCursorIndex(0);
    }
  }
}
