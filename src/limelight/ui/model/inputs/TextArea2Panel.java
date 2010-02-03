package limelight.ui.model.inputs;

import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.ui.model.inputs.keyProcessors.*;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class TextArea2Panel   extends TextInputPanel
{

  public TextArea2Panel()
  {
    setSize(150, 100);
    paintableRegion = new Box(0, TextModel.TOP_MARGIN, width, height - 2 * TextModel.TOP_MARGIN);
    boxInfo = new TextBoxModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
    painterComposite = new TextPanelPainterComposite(boxInfo);
    horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
    focused = true;
  }

  @Override
  protected void expandPaintableRegionToRightBound()
  {
  }

  @Override
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
  public void paintOn(Graphics2D graphics)
  {
    painterComposite.paint(graphics);    
  }

  @Override
  public void setPaintableRegion(int index)
  {
    paintableRegion.x = 0;
    paintableRegion.y = 0;
    paintableRegion.width = width;
    paintableRegion.height = height;
  }

  @Override
  public void resetPaintableRegion()
  {
    paintableRegion.x = 0;
    paintableRegion.y = 0;
    paintableRegion.width = width;
    paintableRegion.height = height;
  }

  @Override
  public void maxOutPaintableRegion()
  {
    paintableRegion.x = 0;
    paintableRegion.y = 0;
    paintableRegion.width = width;
    paintableRegion.height = height;
  }

  @Override
  public boolean isTextMaxed()
  {
    return false;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
