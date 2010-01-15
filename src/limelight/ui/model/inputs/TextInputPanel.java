package limelight.ui.model.inputs;

import limelight.Context;
import limelight.styles.Style;
import limelight.ui.model.BasePanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.StageFrame;
import limelight.ui.model.TextAccessor;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class TextInputPanel extends BasePanel implements TextAccessor, InputPanel, ClipboardOwner

{
  protected TextModel boxInfo = new PlainTextModel(this);
  protected boolean focused;
  protected boolean cursorOn;
  protected Thread cursorThread;
  protected int cursorCycleTime = 500;
  protected boolean clickWasInBox;
  protected ArrayList<KeyProcessor> keyProcessors;
  protected MouseProcessor mouseProcessor;

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
    this.boxInfo.text = new StringBuffer(text);
    boxInfo.cursorIndex = text.length();
  }

  public String getText()
  {
    if (boxInfo.text == null)
      return null;
    return boxInfo.text.toString();
  }

  public Point getAbsoluteLocation(){
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
            markAsDirty();

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

  private void stopCursor()
  {
    cursorOn = false;
  }

  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();
    int processorIndex = calculateProcessorIndex(e);
    keyProcessors.get(processorIndex).processKey(key);
    markAsDirty();

  }

  private int calculateProcessorIndex(KeyEvent e)
  {
    int index = 0;
    int modifiers = e.getModifiers();
    if (boxInfo.selectionOn)
      index += 8;
    if (isAltPressed(modifiers))
      index += 4;
    if(isShiftPressed(modifiers))
      index += 2;
    if(isCmdOrCtrlPressed(modifiers))
      index += 1;
    return index;
  }

  private boolean isCmdOrCtrlPressed(int modifiers)
  {
    return (modifiers > 1 && modifiers <7) || (modifiers > 10);
  }

  private boolean isShiftPressed(int modifiers)
  {
    return modifiers %2 == 1;
  }

  private boolean isAltPressed(int modifiers)
  {
    return modifiers >= 8;
  }
}
