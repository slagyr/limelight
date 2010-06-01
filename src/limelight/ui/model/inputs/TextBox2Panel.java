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
    keyProcessors.add(0, new NormalKeyProcessor(boxInfo));
    keyProcessors.add(1, new CmdKeyProcessor(boxInfo));
    keyProcessors.add(2, new ShiftKeyProcessor(boxInfo));
    keyProcessors.add(3, new ShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(4, new AltKeyProcessor(boxInfo));
    keyProcessors.add(5, new AltCmdKeyProcessor(boxInfo));
    keyProcessors.add(6, new AltShiftKeyProcessor(boxInfo));
    keyProcessors.add(7, new AltShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(8, new SelectionOnKeyProcessor(boxInfo));
    keyProcessors.add(9, new SelectionOnCmdKeyProcessor(boxInfo));
    keyProcessors.add(10, new SelectionOnShiftKeyProcessor(boxInfo));
    keyProcessors.add(11, new SelectionOnShiftCmdKeyProcessor(boxInfo));
    keyProcessors.add(12, new SelectionOnAltKeyProcessor(boxInfo));
    keyProcessors.add(13, new SelectionOnAltCmdKeyProcessor(boxInfo));
    keyProcessors.add(14, new SelectionOnAltShiftKeyProcessor(boxInfo));
    keyProcessors.add(15, new SelectionOnAltShiftCmdKeyProcessor(boxInfo));
  }

  @Override
  public void doLayout()
  {
    System.err.println("limelight.ui.model.inputs.TextBox2Panel.doLayout: " + neededLayout);
    super.doLayout();
  }

  public void paintOn(Graphics2D graphics)
  {
    painterComposite.paint(graphics);
  }

  //TODO Delete me?
  public boolean isTextMaxed()
  {
    return boxInfo.isBoxFull();
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
