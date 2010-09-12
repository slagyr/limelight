//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.*;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.ui.model.*;
import limelight.ui.painting.DefaultPainter;

import static junit.framework.Assert.assertEquals;

public abstract class InputPanel extends PanelBase implements TextAccessor
{
  protected InputPanel()
  {
    final EventHandler handler = getEventHandler();
    handler.add(FocusGainedEvent.class, MakeDirtyAction.instance);
    handler.add(FocusLostEvent.class, MakeDirtyAction.instance);

    handler.add(MousePressedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseReleasedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseClickedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseMovedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseDraggedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseEnteredEvent.class, PropogateToParentAction.instance);
    handler.add(MouseExitedEvent.class, PropogateToParentAction.instance);
    handler.add(MouseWheelEvent.class, PropogateToParentAction.instance);
    handler.add(KeyPressedEvent.class, PropogateToParentAction.instance);
    handler.add(KeyReleasedEvent.class, PropogateToParentAction.instance);
    handler.add(CharTypedEvent.class, PropogateToParentAction.instance);
    handler.add(FocusGainedEvent.class, PropogateToParentAction.instance);
    handler.add(FocusLostEvent.class, PropogateToParentAction.instance);
    handler.add(ValueChangedEvent.class, PropogateToParentAction.instance);
  }

  protected abstract void setDefaultStyles(Style style);
  public abstract void setText(String text);

  public void setText(String text, PropablePanel panel)
  {
    setText(text);
  }

  @Override
  public Layout getDefaultLayout()
  {
    return InputPanelLayout.instance;
  }

  @Override
  public boolean hasFocus()
  {
    return getRoot().getKeyListener().getFocusedPanel() == this;
  }

  @Override
  public void setParent(ParentPanelBase panel)
  {
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);      
      setDefaultStyles(propPanel.getStyle());
      propPanel.setPainter(getPropPainter(propPanel));
    }
  }

  protected Painter getPropPainter(PropPanel propPanel)
  {
    return DefaultPainter.instance;
  }

  protected void valueChanged()
  {
    new ValueChangedEvent(this).dispatch(this);
  }

  private static class MakeDirtyAction implements EventAction
  {
    private static MakeDirtyAction instance = new MakeDirtyAction();

    public void invoke(Event event)
    {
      final Panel panel = event.getRecipient();
      panel.markAsDirty();
      if(panel.getParent() != null)
        panel.getParent().markAsDirty();
    }
  }

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }
}
