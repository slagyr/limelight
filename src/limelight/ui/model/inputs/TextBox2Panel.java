package limelight.ui.model.inputs;

import limelight.ui.model.inputs.keyProcessors.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;

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

  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);


  public TextBox2Panel()
  {
    setSize(150, 25);
    keyProcessors = new ArrayList<KeyProcessor>(16);
    initKeyProcessors();
    mouseProcessor = new MouseProcessor(boxInfo);
  }
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

  public void paintOn(Graphics2D graphics)
  {
    graphics.setColor(Color.lightGray);
    graphics.fillRect(0, 0, width, height);
    Dimension textDimensions;

    if (focused)
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);

    graphics.drawRect(0, 0, width - 1, height - 1);

    if (boxInfo.text != null && boxInfo.text.length() > 0)
    {
      graphics.setColor(Color.BLACK);
      textDimensions = boxInfo.calculateTextDimensions();

      boxInfo.calculateTextXOffset(width, textDimensions.width);

      int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + TextModel.LEFT_TEXT_MARGIN - boxInfo.xOffset;

      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + boxInfo.textLayout.getAscent();
      boxInfo.setCursorAndSelectionStartX();
      if (boxInfo.selectionOn)
      {
        graphics.setColor(Color.cyan);
        if (boxInfo.cursorX > boxInfo.selectionStartX)
          graphics.fillRect(boxInfo.selectionStartX, 4, boxInfo.cursorX - boxInfo.selectionStartX, height - 8);
        else
          graphics.fillRect(boxInfo.cursorX, 4, boxInfo.selectionStartX - boxInfo.cursorX, height - 8);

      }
      graphics.setColor(Color.black);
      boxInfo.textLayout.draw(graphics, textX, textY + 1);
    }

    else
      boxInfo.cursorX = 2;

    if (cursorOn)

    {
      graphics.setColor(Color.black);
      graphics.drawLine(boxInfo.cursorX, 4, boxInfo.cursorX, height - 4);
    }

  }


  public void mouseDragged(MouseEvent e)
  {
    mouseProcessor.processMouseDragged(e);
    markAsDirty();
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
    mouseProcessor.processMousePressed(e);
    markAsDirty();

  }


  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    mouseProcessor.processMouseReleased(e);
    markAsDirty();
    Context.instance().keyboardFocusManager.focusPanel(this);
    focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }


  public void keyReleased(KeyEvent e)
  {

  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //This doesn't have to do anything... how unsettling.
  }
}
