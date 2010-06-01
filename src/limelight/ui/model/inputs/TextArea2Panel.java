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
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class TextArea2Panel extends TextInputPanel
{

  public TextArea2Panel()
  {
    boxInfo = new TextAreaModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
    painterComposite = new TextPanelPainterComposite(boxInfo);
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

  @Override
  public void initKeyProcessors()
  {
    keyProcessors.add(0, new ExpandedNormalKeyProcessor(boxInfo));
    keyProcessors.add(1, new CmdKeyProcessor(boxInfo));
    keyProcessors.add(2, new ExpandedShiftKeyProcessor(boxInfo));
    keyProcessors.add(3, new ShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(4, new AltKeyProcessor(boxInfo));
    keyProcessors.add(5, new AltCmdKeyProcessor(boxInfo));
    keyProcessors.add(6, new AltShiftKeyProcessor(boxInfo));
    keyProcessors.add(7, new AltShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(8, new ExpandedSelectionOnKeyProcessor(boxInfo));
    keyProcessors.add(9, new SelectionOnCmdKeyProcessor(boxInfo));
    keyProcessors.add(10, new ExpandedSelectionOnShiftKeyProcessor(boxInfo));
    keyProcessors.add(11, new SelectionOnShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(12, new SelectionOnAltKeyProcessor(boxInfo));
    keyProcessors.add(13, new SelectionOnAltCmdKeyProcessor(boxInfo));
    keyProcessors.add(14, new SelectionOnAltShiftKeyProcessor(boxInfo));
    keyProcessors.add(15, new SelectionOnAltShiftCmdKeyProcessor(boxInfo));
  }

  @Override
  public void paintOn(Graphics2D graphics)
  {
    painterComposite.paint(graphics);
  }

  @Override
  public boolean isTextMaxed()
  {
    return false;
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
      rootPanel.addDirtyRegion(new Box(cursorX, cursorY - boxInfo.yOffset, 1, regionHeight));
    }
  }
}
