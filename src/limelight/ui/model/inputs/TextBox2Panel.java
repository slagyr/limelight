package limelight.ui.model.inputs;

import limelight.ui.model.RootPanel;
import limelight.ui.model.inputs.keyProcessors.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.util.Box;

import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.ArrayList;

public class TextBox2Panel extends TextInputPanel
{

  public TextBox2Panel()
  {
    setSize(150, 25);
    paintableRegion = new Box(0, TextModel.TOP_MARGIN, width, height - 2 * TextModel.TOP_MARGIN);
    boxInfo = new TextBoxModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
    painterComposite = new TextPanelPainterComposite(boxInfo);
    horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
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

  public void setPaintableRegion(int index)
  {
    int x = boxInfo.getXPosFromIndex(index);
    if (isNewPaintableRegion())
      paintableRegion.x = x;
    else if (isExpandingRegionToRight(x))
      paintableRegion.width = x - paintableRegion.x;
    else if (isExpandingRegionToLeft(x))
    {
      paintableRegion.width += paintableRegion.x - x;
      paintableRegion.x = x;
    }
    if(paintableRegion.x < TextModel.SIDE_TEXT_MARGIN)
      paintableRegion.x = TextModel.SIDE_TEXT_MARGIN;
  }

  private boolean isNewPaintableRegion()
  {
    return paintableRegion.x == 0 && paintableRegion.width == 0;
  }

  private boolean isExpandingRegionToLeft(int x)
  {
    return x < paintableRegion.x;
  }

  private boolean isExpandingRegionToRight(int x)
  {
    return x > paintableRegion.x + paintableRegion.width;
  }

  public void resetPaintableRegion()
  {
    int regionHeight = height - 2 * TextModel.TOP_MARGIN;
    if (boxInfo.selectionOn)
    {
      Rectangle selectionRegion = boxInfo.getSelectionRegions().get(0);
      paintableRegion = new Box(selectionRegion.x, TextModel.TOP_MARGIN, selectionRegion.width, regionHeight);
    }
    else
      paintableRegion = new Box(0, TextModel.TOP_MARGIN, 0, regionHeight);
  }

  @Override
  public void maxOutPaintableRegion()
  {
    int regionHeight = height - 2 * TextModel.TOP_MARGIN;
    int regionWidth = width - 2 * TextModel.SIDE_TEXT_MARGIN;
    paintableRegion = new Box(TextModel.SIDE_TEXT_MARGIN, TextModel.TOP_MARGIN, regionWidth,regionHeight);
  }

    @Override
  public void expandPaintableRegionToRightBound()
  {
    setPaintableRegion(boxInfo.getText().length());
  }

  @Override
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
      int regionHeight = boxInfo.getBottomPositionForCursor()- boxInfo.getTopOfStartPositionForCursor() + 1;
      int cursorX = boxInfo.getXPosFromIndex(boxInfo.getCursorIndex()) + getAbsoluteLocation().x;
      rootPanel.addDirtyRegion(new Box(cursorX, cursorY, 1, regionHeight));
    }
  }
}
