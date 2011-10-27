//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.panel;

import limelight.events.Event;
import limelight.model.api.PropProxy;
import limelight.ui.Panel;
import limelight.ui.model.Prop;

public abstract class PanelEvent extends Event
{
  private Panel source;
  private Panel recipient;

  @Override
  public String toString()
  {
    return super.toString() + ": source=" + getSource() + " recipient=" + getRecipient();
  }

  public void setSource(Panel source)
  {
    subject = source;
    this.source = source;
    recipient = source;
  }

  public Panel getSource()
  {
    return source;
  }

  public PanelEvent consumed()
  {
    consume();
    return this;
  }

  public Panel getRecipient()
  {
    return recipient;
  }

  public void setRecipient(Panel panel)
  {
    recipient = panel;
  }

  public boolean isInheritable()
  {
    return false;
  }

  public void dispatch(Panel panel)
  {
    if(source == null)
      setSource(panel);

    Panel previousRecipient = recipient;
    setRecipient(panel);
    final PanelEventHandler eventHandler = recipient.getEventHandler();
    eventHandler.dispatch(this);
    setRecipient(previousRecipient);
  }

  public PropProxy getProp()
  {
    if(recipient instanceof Prop)
      return ((Prop)recipient).getProxy();
    else
      return null;
  }
}
