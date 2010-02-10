package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedNormalKeyProcessor extends KeyProcessor
{
  public ExpandedNormalKeyProcessor(TextModel modelInfo)
  {
    super(modelInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    KeyProcessor basicKeyProcessor = new NormalKeyProcessor(modelInfo);
    int keyCode = event.getKeyCode();
    if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB)
      modelInfo.insertCharIntoTextBox(event.getKeyChar());
    else if (modelInfo.isMoveUpEvent(keyCode))
      modelInfo.moveCursorUpALine();
    else if (modelInfo.isMoveDownEvent(keyCode))
      modelInfo.moveCursorDownALine();
    else
      basicKeyProcessor.processKey(event);
  }

}
