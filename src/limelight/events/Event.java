//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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

