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
    keyProcessors.add(0, ExpandedNormalKeyProcessor.instance);
    keyProcessors.add(1, CmdKeyProcessor.instance);
    keyProcessors.add(2, ExpandedShiftKeyProcessor.instance);
    keyProcessors.add(3, ShiftCmdKeyProcessor.instance);
    keyProcessors.add(4, AltKeyProcessor.instance);
    keyProcessors.add(5, AltCmdKeyProcessor.instance);
    keyProcessors.add(6, AltShiftKeyProcessor.instance);
    keyProcessors.add(7, AltShiftCmdKeyProcessor.instance);
    keyProcessors.add(8, ExpandedSelectionOnKeyProcessor.instance);
    keyProcessors.add(9, SelectionOnCmdKeyProcessor.instance);
    keyProcessors.add(10, ExpandedSelectionOnShiftKeyProcessor.instance);
    keyProcessors.add(11, SelectionOnShiftCmdKeyProcessor.instance);
    keyProcessors.add(12, SelectionOnAltKeyProcessor.instance);
    keyProcessors.add(13, SelectionOnAltCmdKeyProcessor.instance);
    keyProcessors.add(14, SelectionOnAltShiftKeyProcessor.instance);
    keyProcessors.add(15, SelectionOnAltShiftCmdKeyProcessor.instance);
  }

  @Override
  public void paintOn(Graphics2D graphics)
  {
    painterComposite.paint(graphics);
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
