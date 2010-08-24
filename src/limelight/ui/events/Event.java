package limelight.ui.events;

import limelight.ui.Panel;

public abstract class Event
{
  private Panel source;
  private boolean consumed;

  public Event(Panel source)
  {
    this.source = source;
  }

  public Panel getSource()
  {
    return source;
  }

  public boolean isConsumed()
  {
    return consumed;
  }

  public void consume()
  {
    consumed = true;
  }

  public boolean isInheritable()
  {
    return false;
  }
}
