//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.HorizontalAlignment;
import limelight.styles.Style;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import limelight.ui.model.RootPanel;
import limelight.ui.model.inputs.keyProcessors.*;
import limelight.util.Box;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

public class TextArea2Panel extends TextInputPanel
{

  public TextArea2Panel()
  {
    boxInfo = new TextAreaModel(this);
    mouseProcessor = new MouseProcessor(boxInfo);
    horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.TOP);
    focused = true;
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 150);
    style.setDefault(Style.HEIGHT, 75);
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }

  protected void markCursorRegionAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if (rootPanel != null)
    {
      int regionHeight = boxInfo.getHeightOfCurrentLine();
      int cursorY = boxInfo.getYPosFromIndex(boxInfo.getCursorIndex()) + getAbsoluteLocation().y;
      int cursorX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex()) + getAbsoluteLocation().x;
      rootPanel.addDirtyRegion(new Box(cursorX, cursorY - boxInfo.getYOffset(), 1, regionHeight));
    }
  }

  public KeyProcessor getKeyProcessorFor(int modifiers)
  {
    if(getModelInfo().isSelectionOn())
    {
      switch(modifiers)
      {
        case 0: return ExpandedSelectionOnKeyProcessor.instance;
        case 1: return ExpandedSelectionOnShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7: return SelectionOnShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6: return SelectionOnCmdKeyProcessor.instance;
        case 8: return SelectionOnAltKeyProcessor.instance;
        case 9: return SelectionOnAltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14: return SelectionOnAltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15: return SelectionOnAltShiftCmdKeyProcessor.instance;
      }
    }
    else
    {
      switch(modifiers)
      {
        case 0: return ExpandedNormalKeyProcessor.instance;
        case 1: return ExpandedShiftKeyProcessor.instance;
        case 3:
        case 5:
        case 7: return ShiftCmdKeyProcessor.instance;
        case 2:
        case 4:
        case 6: return CmdKeyProcessor.instance;
        case 8: return AltKeyProcessor.instance;
        case 9: return AltShiftKeyProcessor.instance;
        case 10:
        case 12:
        case 14: return AltCmdKeyProcessor.instance;
        case 11:
        case 13:
        case 15: return AltShiftCmdKeyProcessor.instance;

      }
    }
    throw new RuntimeException("Unexpected key modifiers: " + modifiers);
  }
}
