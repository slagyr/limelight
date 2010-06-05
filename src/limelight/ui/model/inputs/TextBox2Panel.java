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
import java.awt.*;
import java.util.ArrayList;

public class TextBox2Panel extends TextInputPanel
{
  public TextBox2Panel()
  {
    boxInfo = new TextBoxModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
    painterComposite = new TextPanelPainterComposite(boxInfo);
    horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.CENTER);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 150);
    style.setDefault(Style.HEIGHT, 28);
  }

  public void initKeyProcessors()
  {
    keyProcessors.add(0, NormalKeyProcessor.instance);
    keyProcessors.add(1, CmdKeyProcessor.instance);
    keyProcessors.add(2, ShiftKeyProcessor.instance);
    keyProcessors.add(3, ShiftCmdKeyProcessor.instance);
    keyProcessors.add(4, AltKeyProcessor.instance);
    keyProcessors.add(5, AltCmdKeyProcessor.instance);
    keyProcessors.add(6, AltShiftKeyProcessor.instance);
    keyProcessors.add(7, AltShiftCmdKeyProcessor.instance);
    keyProcessors.add(8, SelectionOnKeyProcessor.instance);
    keyProcessors.add(9, SelectionOnCmdKeyProcessor.instance);
    keyProcessors.add(10, SelectionOnShiftKeyProcessor.instance);
    keyProcessors.add(11, SelectionOnShiftCmdKeyProcessor.instance);
    keyProcessors.add(12, SelectionOnAltKeyProcessor.instance);
    keyProcessors.add(13, SelectionOnAltCmdKeyProcessor.instance);
    keyProcessors.add(14, SelectionOnAltShiftKeyProcessor.instance);
    keyProcessors.add(15, SelectionOnAltShiftCmdKeyProcessor.instance);
  }

  public void paintOn(Graphics2D graphics)
  {
    painterComposite.paint(graphics);
  }


  public void keyReleased(KeyEvent e)
  {

  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //This doesn't have to do anything... how unsettling.
  }

  protected void markCursorRegionAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if (rootPanel != null)
    {
      int cursorY =  getAbsoluteLocation().y + boxInfo.getTopOfStartPositionForCursor();
      int regionHeight = boxInfo.getBottomPositionForCursor() - boxInfo.getTopOfStartPositionForCursor() + 1;
      int cursorX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex()) + getAbsoluteLocation().x;
      rootPanel.addDirtyRegion(new Box(cursorX, cursorY, 1, regionHeight));
    }
  }

  
}
