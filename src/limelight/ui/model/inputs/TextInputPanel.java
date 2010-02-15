package limelight.ui.model.inputs;

import limelight.Context;
import limelight.styles.Style;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.ui.model.*;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class TextInputPanel extends BasePanel implements TextAccessor, InputPanel, ClipboardOwner

{
  protected TextModel boxInfo;
  protected boolean focused;
  protected boolean cursorOn;
  protected Thread cursorThread;
  protected int cursorCycleTime = 500;
  protected ArrayList<KeyProcessor> keyProcessors;
  protected MouseProcessor mouseProcessor;
  protected TextPanelPainterComposite painterComposite;
  protected Box paintableRegion;
  protected SimpleHorizontalAlignmentAttribute horizontalTextAlignment;
  protected SimpleVerticalAlignmentAttribute verticalTextAlignment;
  private int lastKeyPressed;

  public void setParent(limelight.ui.Panel panel)
  {
    if (panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if (panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
  }

  public TextModel getModelInfo()
  {
    return boxInfo;
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.boxInfo.setText(text);
    boxInfo.setCursorIndex(text.length());
  }

  public String getText()
  {
    if (boxInfo.getText() == null)
      return null;
    return boxInfo.getText();
  }

  public Point getAbsoluteLocation()
  {
    return super.getAbsoluteLocation();
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    markAsDirty();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    markAsDirty();
    super.focusLost(e);

  }

  private void startCursor()
  {
    if (cursorThread == null || !cursorThread.isAlive())
    {
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while (focused)
          {
            cursorOn = !cursorOn;
            markCursorRegionAsDirty();
            try
            {
              Thread.sleep(cursorCycleTime);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace();
            }
          }
        }
      });
      cursorThread.start();
    }
  }

  protected abstract void markCursorRegionAsDirty();

  private void stopCursor()
  {
    cursorOn = false;
  }

  public void keyPressed(KeyEvent e)
  {
    int processorIndex = calculateKeyProcessorIndex(e);
    keyProcessors.get(processorIndex).processKey(e);
    lastKeyPressed = e.getKeyCode();
    cursorOn = true;
    markPaintableRegionAsDirty();
  }

  private int calculateKeyProcessorIndex(KeyEvent e)
  {
    int index = 0;
    int modifiers = e.getModifiers();
    if (boxInfo.selectionOn)
      index += 8;
    if (isAltPressed(modifiers))
      index += 4;
    if (isShiftPressed(modifiers))
      index += 2;
    if (isCmdOrCtrlPressed(modifiers))
      index += 1;
    return index;
  }

  private boolean isCmdOrCtrlPressed(int modifiers)
  {
    return (modifiers > 1 && modifiers < 7) || (modifiers > 10);
  }

  private boolean isShiftPressed(int modifiers)
  {
    return modifiers % 2 == 1;
  }

  private boolean isAltPressed(int modifiers)
  {
    return modifiers >= 8;
  }

  public boolean isCursorOn()
  {
    return cursorOn;
  }

  public boolean isFocused()
  {
    return focused;
  }

  public void mouseDragged(MouseEvent e)
  {
    System.out.println(" from input panel e.getX() = " + e.getX());
    mouseProcessor.processMouseDragged(e);
    cursorOn = true;
    markPaintableRegionAsDirty();    
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
    mouseProcessor.processMousePressed(e);
    cursorOn = true;
    markPaintableRegionAsDirty();
    resetPaintableRegion();
  }

  private void markPaintableRegionAsDirty()
  {
    if(boxInfo.getCursorIndex() < boxInfo.getText().length())
      expandPaintableRegionToRightBound();
    if(isTextMaxed())
      maxOutPaintableRegion();
    RootPanel rootPanel = getRoot();
    if (rootPanel != null && paintableRegion != null){
      rootPanel.addDirtyRegion(new Box(paintableRegion.x + getAbsoluteLocation().x - 3,
          paintableRegion.y + getAbsoluteLocation().y, paintableRegion.width + 6, paintableRegion.height));
    }
  }

  protected abstract void expandPaintableRegionToRightBound();


  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    mouseProcessor.processMouseReleased(e);
    Context.instance().keyboardFocusManager.focusPanel(this);
    if(!focused)
      focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public abstract void initKeyProcessors();

  public abstract void paintOn(Graphics2D graphics);

  public abstract void setPaintableRegion(int index);

  public Shape getPaintableRegion(){
    return paintableRegion;
  }

  public abstract void resetPaintableRegion();

  public abstract void maxOutPaintableRegion();

  public abstract boolean isTextMaxed();

  public int getLastKeyPressed()
  {
    return lastKeyPressed;
  }

  public void setLastKeyPressed(int keyCode)
  {
    lastKeyPressed = keyCode;
  }
}
