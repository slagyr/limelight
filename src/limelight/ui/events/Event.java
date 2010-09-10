package limelight.ui.events;

import limelight.ui.Panel;
import limelight.ui.model.EventHandler;
import limelight.ui.model.inputs.TextAreaPanel;

public abstract class Event
{
  private Panel source;
  private boolean consumed;
  private Panel recipient;

  public Event(Panel source)
  {
    this.source = source;
    recipient = source;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + ": source=" + getSource() + " recipient=" + getRecipient();
  }

  public Panel getSource()
  {
    return source;
  }

  public Panel getRecipient()
  {
    return recipient;
  }

  public void setRecipient(Panel panel)
  {
    recipient = panel;
  }

  public boolean isConsumed()
  {
    return consumed;
  }

  public void consume()
  {
    consumed = true;
  }

  public Event consumed()
  {
    consume();
    return this;
  }

  public boolean isInheritable()
  {
    return false;
  }

  public void dispatch(Panel panel)
  {
    Panel previousRecipient = recipient;
    setRecipient(panel);
    final EventHandler eventHandler = recipient.getEventHandler();
    eventHandler.dispatch(this);
    setRecipient(previousRecipient);
  }
}
