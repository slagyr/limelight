//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.Context;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.styles.values.SimpleVerticalAlignmentValue;
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
  protected SimpleHorizontalAlignmentValue horizontalTextAlignment;
  protected SimpleVerticalAlignmentValue verticalTextAlignment;
  private int lastKeyPressed;

  public void setParent(limelight.ui.Panel panel)
  {
    if(panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getFrame());
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
      setDefaultStyles(propPanel.getStyle());
    }
  }

  @Override
  public Layout getDefaultLayout()
  {
    return TextInputPanelLayout.instance;
  }

  @Override
  public void consumableAreaChanged()
  {
    markAsNeedingLayout();
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

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(PropablePanel panel, String text)
  {
    this.boxInfo.setText(text);
    boxInfo.setCursorIndex(text.length());
  }

  public String getText()
  {
    if(boxInfo.getText() == null)
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
    if(cursorThread == null || !cursorThread.isAlive())
    {
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while(focused)
          {
            cursorOn = !cursorOn;
            markCursorRegionAsDirty();
            try
            {
              Thread.sleep(cursorCycleTime);
            }
            catch(InterruptedException e)
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
    markAsDirty(); //TODO shouldRelyOn keyProcessor to markAsDirty
  }

  private int calculateKeyProcessorIndex(KeyEvent e)
  {
    int index = 0;
    int modifiers = e.getModifiers();
    if(boxInfo.selectionOn)
      index += 8;
    if(isAltPressed(modifiers))
      index += 4;
    if(isShiftPressed(modifiers))
      index += 2;
    if(isCmdOrCtrlPressed(modifiers))
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
    mouseProcessor.processMouseDragged(e);
    cursorOn = true;
    markAsDirty(); //TODO should rely in processor to markAsDirty
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
    mouseProcessor.processMousePressed(e);
    cursorOn = true;
    markAsDirty(); //TODO should rely in processor to markAsDirty
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    mouseProcessor.processMouseReleased(e);
    Context.instance().keyboardFocusManager.focusPanel(this);
    if(!focused)
      focusGained(new FocusEvent(getRoot().getFrame().getWindow(), 0));
    buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  protected abstract void setDefaultStyles(Style style);

  public abstract void initKeyProcessors();

  public abstract void paintOn(Graphics2D graphics);

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
