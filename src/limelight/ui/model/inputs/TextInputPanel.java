//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.Context;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.model.*;
import limelight.ui.model.inputs.painting.*;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class TextInputPanel extends BasePanel implements TextAccessor, InputPanel, ClipboardOwner, TextContainer
{
  protected TextModel model;
  protected boolean focused;
  protected boolean cursorOn;
  protected Thread cursorThread;
  protected int cursorCycleTime = 500;
  protected TextPanelMouseProcessor mouseProcessor;
  private int lastKeyPressed;

  protected abstract void setDefaultStyles(Style style);
  public abstract KeyProcessor getKeyProcessorFor(int modifiers);

  protected void markCursorRegionAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if(rootPanel != null)
    {
      Box caret = model.getCaretShape().translated(getAbsoluteLocation());
      rootPanel.addDirtyRegion(caret);
    }
  }

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
      propPanel.setPainter(TextPanelPropPainter.instance);
    }
  }
  
  public void paintOn(Graphics2D graphics)
  {  
    TextPanelSelectionPainter.instance.paint(graphics, model);
    TextPanelTextPainter.instance.paint(graphics, model);
    TextPanelCaretPainter.instance.paint(graphics, model);
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

  public TextModel getModel()
  {
    return model;
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
    this.model.setText(text);
    model.setCaretIndex(text.length());
  }

  public String getText()
  {
    if(model.getText() == null)
      return null;
    return model.getText();
  }

  public Point getAbsoluteLocation()
  {
    return super.getAbsoluteLocation();
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    markAsDirty();
    getParent().markAsDirty();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    markAsDirty();
    getParent().markAsDirty();
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

  private void stopCursor()
  {
    cursorOn = false;
  }

  public void keyPressed(KeyEvent e)
  {
    KeyProcessor processor = getKeyProcessorFor(e.getModifiers());
    processor.processKey(e, model);
    lastKeyPressed = e.getKeyCode();
    cursorOn = true;
    markAsDirty(); //TODO shouldRelyOn keyProcessor to markAsDirty
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
    buttonPressed(new ActionEvent(this, 0, "blah")); //TODO Why?
  }

  public int getLastKeyPressed()
  {
    return lastKeyPressed;
  }

  public void setLastKeyPressed(int keyCode)
  {
    lastKeyPressed = keyCode;
  }

  public void setCursorOn(boolean value)
  {
    cursorOn = value;
  }

  public void setFocused(boolean value)
  {
    focused = value;
  }

  protected void setBorderStyleDefaults(Style style)
  {
    style.setDefault(Style.TOP_BORDER_WIDTH, 4);
    style.setDefault(Style.RIGHT_BORDER_WIDTH, 4);
    style.setDefault(Style.BOTTOM_BORDER_WIDTH, 4);
    style.setDefault(Style.LEFT_BORDER_WIDTH, 4);
    style.setDefault(Style.TOP_BORDER_COLOR, "transparent");
    style.setDefault(Style.RIGHT_BORDER_COLOR, "transparent");
    style.setDefault(Style.BOTTOM_BORDER_COLOR, "transparent");
    style.setDefault(Style.LEFT_BORDER_COLOR, "transparent");
  }

  public boolean hasFocus()
  {
    return Context.instance().keyboardFocusManager.getFocusedPanel() == this;
  }

  protected void setPaddingDefaults(Style style)
  {
    style.setDefault(Style.TOP_PADDING, 2);
    style.setDefault(Style.RIGHT_PADDING, 2);
    style.setDefault(Style.BOTTOM_PADDING, 2);
    style.setDefault(Style.LEFT_PADDING, 2);
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }

  public void setModel(TextModel model)
  {
    this.model = model;
  }
}
