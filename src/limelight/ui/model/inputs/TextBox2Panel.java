//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import limelight.ui.model.RootPanel;
import limelight.ui.model.inputs.keyProcessors.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.util.Box;

import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;

public class TextBox2Panel extends TextInputPanel
{
  public TextBox2Panel()
  {
    boxInfo = new TextBoxModel(this);
    mouseProcessor = new MouseProcessor(boxInfo);
    horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.CENTER);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 150);
    style.setDefault(Style.HEIGHT, 28);
  }

  public void keyReleased(KeyEvent e)
  {

  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //TODO This doesn't have to do anything... how unsettling.
  }

  protected void markCursorRegionAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if(rootPanel != null)
    {
      int cursorY = getAbsoluteLocation().y + boxInfo.getTopOfStartPositionForCursor();
      int regionHeight = boxInfo.getBottomPositionForCursor() - boxInfo.getTopOfStartPositionForCursor() + 1;
      int cursorX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex()) + getAbsoluteLocation().x;
      rootPanel.addDirtyRegion(new Box(cursorX, cursorY, 1, regionHeight));
    }
  }

  public KeyProcessor getKeyProcessorFor(int modifiers)
  {
    if(getModelInfo().isSelectionOn())
    {
      switch(modifiers)
      {
        case 0:
          return SelectionOnKeyProcessor.instance;
        case 1:
          return SelectionOnShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7:
          return SelectionOnShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6:
          return SelectionOnCmdKeyProcessor.instance;
        case 8:
          return SelectionOnAltKeyProcessor.instance;
        case 9:
          return SelectionOnAltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14:
          return SelectionOnAltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15:
          return SelectionOnAltShiftCmdKeyProcessor.instance;
      }
    }
    else
    {
      switch(modifiers)
      {
        case 0:
          return NormalKeyProcessor.instance;
        case 1:
          return ShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7:
          return ShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6:
          return CmdKeyProcessor.instance;
        case 8:
          return AltKeyProcessor.instance;
        case 9:
          return AltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14:
          return AltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15:
          return AltShiftCmdKeyProcessor.instance;

      }
    }
    throw new RuntimeException("Unexpected key modifiers: " + modifiers);
  }
}
