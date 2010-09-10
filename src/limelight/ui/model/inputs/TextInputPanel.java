//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.background.Animation;
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
  protected TextPanelMouseProcessor mouseProcessor;
  private CaretAnimator caretAnimator;

  protected TextInputPanel()
  {
    model = createModel();
    mouseProcessor = new TextPanelMouseProcessor(model);
    getEventHandler().add(MousePressedEvent.class, DelegateToMouseProcessorAction.instance);
    getEventHandler().add(MouseDraggedEvent.class, DelegateToMouseProcessorAction.instance);
    getEventHandler().add(FocusGainedEvent.class, FocusGainedAction.instance);
    getEventHandler().add(FocusLostEvent.class, FocusLostAction.instance);
    getEventHandler().add(KeyPressedEvent.class, KeyPressedAction.instance);
    getEventHandler().add(CharTypedEvent.class, CharTypedAction.instance);
  }

  protected abstract TextModel createModel();

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
    if(panel == null)
      caretAnimator.stop();
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

  public void setText(String text)
  {
    if(getText().equals(text))
      return;

    model.setText(text);
    valueChanged();
  }

  public String getText()
  {
    return model.getText();
  }

  public Point getAbsoluteLocation()
  {
    return super.getAbsoluteLocation();
  }

  private void startCaret()
  {
    if(caretAnimator == null)
      caretAnimator = new CaretAnimator(this);

    caretAnimator.start();
  }

  private void stopCaret()
  {
    caretAnimator.stop();
    model.setCaretOn(false);
  }

  public boolean isCaretBlinking()
  {
    return caretAnimator != null && caretAnimator.isRunning();
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

  private static class CaretAnimator extends Animation
  {
    private TextInputPanel panel;

    public CaretAnimator(TextInputPanel panel)
    {
      super(2);
      this.panel = panel;
    }

    @Override
    protected void doUpdate()
    {
      TextModel model = panel.getModel();
      model.setCaretOn(!model.isCaretOn());
      panel.markAsDirty();
    }
  }

  private static class FocusGainedAction implements EventAction
  {
    private static FocusGainedAction instance = new FocusGainedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;

      final TextInputPanel panel = (TextInputPanel) event.getRecipient();
      panel.markAsDirty();
      panel.getParent().markAsDirty();
      panel.startCaret();
      panel.getModel().resetChangeFlag();
    }
  }

  private static class FocusLostAction implements EventAction
  {
    private static FocusLostAction instance = new FocusLostAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;
      
      final TextInputPanel panel = (TextInputPanel) event.getRecipient();
      panel.stopCaret();
      panel.markAsDirty();
      panel.getParent().markAsDirty();
      if(panel.getModel().hasChanged())
        panel.valueChanged();
    }
  }

  private static class DelegateToMouseProcessorAction implements EventAction
  {
    private static DelegateToMouseProcessorAction instance = new DelegateToMouseProcessorAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;

      final TextInputPanel panel = (TextInputPanel) event.getRecipient();
      if(event instanceof MousePressedEvent)
        panel.mouseProcessor.processMousePressed((MousePressedEvent) event);
      else if(event instanceof MouseDraggedEvent)
        panel.mouseProcessor.processMouseDragged((MouseDraggedEvent) event);
    }
  }

  private static class KeyPressedAction implements EventAction
  {
    private static KeyPressedAction instance = new KeyPressedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;

      final TextInputPanel panel = (TextInputPanel) event.getRecipient();
      KeyPressedEvent press = (KeyPressedEvent) event;
      TextInputKeyProcessor.instance.processKey(press, panel.model);
      panel.model.setCaretOn(true);
      panel.markAsDirty();
    }
  }

  private static class CharTypedAction implements EventAction
  {
    private static CharTypedAction instance = new CharTypedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;

      final TextInputPanel panel = (TextInputPanel) event.getRecipient();
      CharTypedEvent typeEvent = (CharTypedEvent) event;
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
