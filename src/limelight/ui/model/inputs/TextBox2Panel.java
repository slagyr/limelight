package limelight.ui.model.inputs;

import limelight.ui.model.inputs.keyProcessors.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;
import limelight.ui.model.inputs.painters.TextPanelBoxPainter;
import limelight.ui.model.inputs.painters.TextPanelCursorPainter;
import limelight.util.Box;

import java.awt.datatransfer.*;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;

public class TextBox2Panel extends TextInputPanel
{

  public TextBox2Panel()
  {
    setSize(150, 25);
    paintableRegion = new Box(0,TextModel.TOP_MARGIN,0, height - 2* TextModel.TOP_MARGIN);
    boxInfo = new PlainTextModel(this);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
    paintStore = new TextPanelPainterStore(boxInfo);
    painter = paintStore.getBoxPainter();
    horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
    verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  }

  @Override
  public void initKeyProcessors()
  {
    keyProcessors.add(0, new KPKey(boxInfo));
    keyProcessors.add(1, new KPCMD(boxInfo));
    keyProcessors.add(2, new KPShift(boxInfo));
    keyProcessors.add(3, new KPShiftCMD(boxInfo));
    keyProcessors.add(4, new KPAlt(boxInfo));
    keyProcessors.add(5, new KPAltCMD(boxInfo));
    keyProcessors.add(6, new KPAltShift(boxInfo));
    keyProcessors.add(7, new KPAltShiftCMD(boxInfo));
    keyProcessors.add(8, new KPSel(boxInfo));
    keyProcessors.add(9, new KPSelCMD(boxInfo));
    keyProcessors.add(10, new KPSelShift(boxInfo));
    keyProcessors.add(11, new KPSelShiftCMD(boxInfo));
    keyProcessors.add(12, new KPSelAlt(boxInfo));
    keyProcessors.add(13, new KPSelAltCMD(boxInfo));
    keyProcessors.add(14, new KPSelAltShift(boxInfo));
    keyProcessors.add(15, new KPSelAltShiftCMD(boxInfo));
  }

  @Override
  public void paintOn(Graphics2D graphics)
  {
    painter = paintStore.getBoxPainter();
    painter.paint(graphics);

    painter = paintStore.getSelectionPainter();
    painter.paint(graphics);

    painter = paintStore.getTextPainter();
    painter.paint(graphics);

    painter = paintStore.getCursorPainter();
    painter.paint(graphics);
  }

   public void setPaintableRegion(int index)
  {
    int x = boxInfo.getXPosFromIndex(index);
    if(paintableRegion.x == 0 && paintableRegion.width == 0)
      paintableRegion.x = x;
    else if (x > paintableRegion.x && (x - paintableRegion.x) > paintableRegion.width)
      paintableRegion.width = x - paintableRegion.x;
    else if (x < paintableRegion.x){
      paintableRegion.width += paintableRegion.x - x;
      paintableRegion.x = x;
    }
  }

  public void resetPaintableRegion(){
    if(boxInfo.selectionOn){
    paintableRegion = new Box(boxInfo.getSelectionRegion().x,TextModel.TOP_MARGIN,
        boxInfo.getSelectionRegion().width, height - 2* TextModel.TOP_MARGIN);
    }
    else
      paintableRegion = new Box(0,TextModel.TOP_MARGIN,0, height - 2* TextModel.TOP_MARGIN);
  }


  public void keyReleased(KeyEvent e)
  {

  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //This doesn't have to do anything... how unsettling.
  }
}
