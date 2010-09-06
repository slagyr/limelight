//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.*;
import limelight.ui.Panel;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.ui.model.BasePanel;
import limelight.ui.model.Layout;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.util.Box;

import java.awt.*;

public abstract class InputPanel extends BasePanel implements TextAccessor
{
  protected InputPanel()
  {
    getEventHandler().add(FocusGainedEvent.class, MakeDirtyAction.instance);
    getEventHandler().add(FocusLostEvent.class, MakeDirtyAction.instance);
  }

  protected abstract void setDefaultStyles(Style style);

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
  public void setParent(Panel panel)
  {
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);      
      setDefaultStyles(propPanel.getStyle());
    }
  }

  private static class MakeDirtyAction implements EventAction
  {
    private static MakeDirtyAction instance = new MakeDirtyAction();

    public void invoke(Event event)
    {
      event.getPanel().markAsDirty();
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

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }
}
