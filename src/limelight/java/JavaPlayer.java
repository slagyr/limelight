package limelight.java;

import limelight.LimelightException;
import limelight.events.Event;
import limelight.events.EventHandler;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.util.StringUtil;
import org.w3c.dom.Element;
import java.lang.reflect.Method;

public class JavaPlayer implements Player
{
  private Object player;
  private Element element;
  private String eventsPrefix;

  public JavaPlayer(Object obj, Element element, String eventsPrefix)
  {
    this.player = obj;
    this.element = element;
    this.eventsPrefix = eventsPrefix;
  }

  public void cast(PropPanel prop)
  {
    EventHandler eventHandler = prop.getEventHandler();
    applyEvents(element, eventsPrefix, eventHandler);
    invokeCastEvents(prop, element);
    element = null;
    eventsPrefix = null;
  }

  private void invokeCastEvents(PropPanel prop, Element playerElement)
  {
    final CastEvent castEvent = new CastEvent(prop);
    for(Element child : Xml.loadChildElements(playerElement))
    {
      if("onCast".equals(child.getNodeName()))
      {
        String methodName = child.getTextContent().trim();
        final Method method = findMethod(methodName);
        new JavaEventAction(player, method).invoke(castEvent);
      }
    }
  }

  public void applyEvents(Element element, String eventsPrefix, EventHandler eventHandler)
  {
    for(Element eventElement : Xml.loadChildElements(element))
    {
      String name = eventElement.getNodeName();
      String methodName = eventElement.getTextContent();
      addEventActionFor(eventsPrefix, name, methodName, eventHandler);
    }
  }

  public void addEventActionFor(String eventPrefix, String eventName, String methodName, EventHandler eventHandler)
  {
    String eventClassName = eventPrefix + StringUtil.capitalCamalize(eventName.substring(2)) + "Event";

    if("limelight.ui.events.panel.CastEvent".equals(eventClassName))
      return;

    Class<? extends Event> eventClass = null;
    try
    {
      eventClass = (Class<? extends Event>) Class.forName(eventClassName);
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }

    if(eventClass == null)
      return;

    Method eventMethod = findMethod(methodName);
    final JavaEventAction action = new JavaEventAction(player, eventMethod);
    eventHandler.add(eventClass, action);
  }

  public Method findMethod(String methodName)
  {
    Method eventMethod = null;
    for(Method method : player.getClass().getMethods())
    {
      if(methodName.equals(method.getName()))
      {
        eventMethod = method;
        break;
      }
    }
    if(eventMethod == null)
      throw new NoSuchMethodError(methodName + " for " + player.getClass());
    return eventMethod;
  }

  public Object getPlayer()
  {
    return player;
  }
}
