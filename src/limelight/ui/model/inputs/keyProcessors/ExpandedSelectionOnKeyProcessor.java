package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedSelectionOnKeyProcessor extends KeyProcessor
{
  public ExpandedSelectionOnKeyProcessor(TextModel modelInfo)
  {
    super(modelInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    KeyProcessor basicSelectionProcessor = new SelectionOnKeyProcessor(modelInfo);
    modelInfo.selectionOn = false;
    int keyCode = event.getKeyCode();
    if (isAnExtraKey(keyCode)){
      modelInfo.deleteSelection();
      insertCharIntoTextBox(event.getKeyChar());
    }
    else if (isMoveUpEvent(keyCode))
      moveCursorUpALine();
    else if (isMoveDownEvent(keyCode))
      moveCursorDownALine();
    else
      basicSelectionProcessor.processKey(event);
  }
}
