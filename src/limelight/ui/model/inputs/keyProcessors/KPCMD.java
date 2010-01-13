package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class KPCMD extends KeyProcessor
{
  public KPCMD(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(int keyCode)
  {
    switch (keyCode)
    {

      case KeyEvent.VK_A:
        boxInfo.selectionOn = true;
        boxInfo.cursorIndex = boxInfo.text.length();
        boxInfo.selectionIndex = 0;
        break;

      case KeyEvent.VK_V:
        boxInfo.pasteClipboard();
        break;


    }
  }
}
