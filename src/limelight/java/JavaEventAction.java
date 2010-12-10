package limelight.java;

import limelight.LimelightException;
import limelight.events.Event;
import limelight.events.EventAction;
import java.lang.reflect.Method;

public class JavaEventAction implements EventAction
{
  private Object player;
  private Method method;
  private boolean takesEvent;

  public JavaEventAction(Object player, Method method)
  {
    this.player = player;
    this.method = method;
    takesEvent = method.getParameterTypes().length == 1;
  }

  public void invoke(Event event)
  {
    try
    {
      if(takesEvent)
        method.invoke(player, event);
      else
        method.invoke(player);
    }
    catch(Exception e)
    {
e.printStackTrace();
      throw new LimelightException(e);
    }
  }
}
