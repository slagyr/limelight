//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.text.TextLocation;

public class SelectionOnCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.KEY_A:
        model.selectAll();
        break;
      case KeyEvent.KEY_V:
        model.deleteSelection();
        model.pasteClipboard();
        model.deactivateSelection();
        break;
      case KeyEvent.KEY_C:
        model.copySelection();
        break;
      case KeyEvent.KEY_X:
        model.copySelection();
        model.deleteSelection();
        model.deactivateSelection();
        break;
      case KeyEvent.KEY_RIGHT:
        model.sendCaretToEndOfLine();
        model.deactivateSelection();
        break;
      case KeyEvent.KEY_LEFT:
        model.sendCursorToStartOfLine();
        model.deactivateSelection();
        break;
      case KeyEvent.KEY_UP:
        model.setCaretLocation(TextLocation.origin);
        model.deactivateSelection();
        break;
      case KeyEvent.KEY_DOWN:
        model.setCaretLocation(model.getEndLocation());
        model.deactivateSelection();
        break;
    }
  }

}
