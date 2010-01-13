package limelight.ui.model.inputs;

import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.font.TextLayout;


public class PlainTextModel extends TextModel
{

  public PlainTextModel(TextInputPanel myBox)
  {
    this.myBox = myBox;
    cursorX = LEFT_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //this doesn't have to do anything...
  }
}
