package limelight.events;

public abstract class Event
{
  private boolean consumed;
  protected Object subject;
  
  public Event()
  {
  }

  public Event(Object subject)
  {
    this.subject = subject;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName();
  }

  public boolean isConsumed()
  {
    return consumed;
  }

  public void consume()
  {
    consumed = true;
  }

  public Object getSubject()
  {
    return subject;
  }

  public Event consumed()
  {
    consume();
    return this;
  }
}

