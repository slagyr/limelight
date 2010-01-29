package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class CmdKeyProcessor extends KeyProcessor
{
  public CmdKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.VK_A:
        selectAll();
        break;

      case KeyEvent.VK_V:
        boxInfo.pasteClipboard();
        break;
      case KeyEvent.VK_RIGHT:
        boxInfo.setCursorIndex(boxInfo.text.length());
        break;
      case KeyEvent.VK_LEFT:
        boxInfo.setCursorIndex(0);
        break;


    }
  }
}
