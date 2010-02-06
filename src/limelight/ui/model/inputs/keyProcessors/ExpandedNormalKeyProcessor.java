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
    if (isAnExtraKey(keyCode))
      insertCharIntoTextBox(event.getKeyChar());
    else if (isMoveUpEvent(keyCode))
      moveCursorUpALine();
    else if (isMoveDownEvent(keyCode))
      moveCursorDownALine();
    else
      basicKeyProcessor.processKey(event);
  }

}
