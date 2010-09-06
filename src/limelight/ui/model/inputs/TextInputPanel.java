//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.*;
import limelight.ui.events.*;
import limelight.ui.model.*;
import limelight.ui.model.inputs.painting.*;
import limelight.util.Box;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

public abstract class TextInputPanel extends InputPanel implements TextAccessor, ClipboardOwner, TextContainer
{
  protected TextModel model;
  protected Thread cursorThread;
  protected int cursorCycleTime = 500;
  protected TextPanelMouseProcessor mouseProcessor;
  private int lastKeyPressed;

  protected TextInputPanel()
  {
    model = createModel();
    mouseProcessor = new TextPanelMouseProcessor(model);
    getEventHandler().add(MousePressedEvent.class, mouseProcessor);
    getEventHandler().add(MouseReleasedEvent.class, mouseProcessor);
    getEventHandler().add(MouseDraggedEvent.class, mouseProcessor);
    getEventHandler().add(FocusGainedEvent.class, FocusGainedAction.instance);
    getEventHandler().add(FocusLostEvent.class, FocusLostAction.instance);
    getEventHandler().add(KeyPressedEvent.class, KeyPressedAction.instance);
    getEventHandler().add(CharTypedEvent.class, CharTypedAction.instance);
  }

  protected abstract TextModel createModel();

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
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
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

  private void startCaret()
  {
    if(cursorThread == null || !cursorThread.isAlive())
    {
      // TODO MDM - Use Animation instead
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while(hasFocus())
          {
            markCursorRegionAsDirty();
            try
            {
              Thread.sleep(cursorCycleTime);
            }
            catch(InterruptedException e)
            {
              e.printStackTrace();
            }
            model.setCaretOn(!model.isCaretOn());
          }
        }
      });
      cursorThread.start();
    }
  }

  private void stopCaret()
  {
    model.setCaretOn(false);
  }

  public int getLastKeyPressed()
  {
    return lastKeyPressed;
  }

  public void setLastKeyPressed(int keyCode)
  {
    lastKeyPressed = keyCode;
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

  private static class FocusGainedAction implements EventAction
  {
    private static FocusGainedAction instance = new FocusGainedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      final TextInputPanel panel = (TextInputPanel) event.getPanel();
      panel.markAsDirty();
      panel.getParent().markAsDirty();
      panel.startCaret();
    }
  }

  private static class FocusLostAction implements EventAction
  {
    private static FocusLostAction instance = new FocusLostAction();

    public void invoke(limelight.ui.events.Event event)
    {
      final TextInputPanel panel = (TextInputPanel) event.getPanel();
      panel.stopCaret();
      panel.markAsDirty();
      panel.getParent().markAsDirty();
    }
  }

  private static class KeyPressedAction implements EventAction
  {
    private static KeyPressedAction instance = new KeyPressedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      final TextInputPanel panel = (TextInputPanel) event.getPanel();
      KeyPressedEvent press = (KeyPressedEvent)event;
      KeyProcessor processor = panel.getKeyProcessorFor(press.getModifiers());
      processor.processKey(press, panel.model);
      panel.lastKeyPressed = press.getKeyCode();
      panel.model.setCaretOn(true);
      panel.markAsDirty();
    }
  }

  private static class CharTypedAction implements EventAction
  {
    private static CharTypedAction instance = new CharTypedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      final TextInputPanel panel = (TextInputPanel) event.getPanel();
      CharTypedEvent typeEvent = (CharTypedEvent)event;
      if(!(typeEvent.isCommandDown() || typeEvent.isControlDown()))
      {
        final char aChar = typeEvent.getChar();
        if(Character.getType(aChar) != Character.CONTROL || Character.isWhitespace(aChar))
          panel.getModel().insertChar(aChar);
      }
      panel.markAsDirty();
    }
  }


}
