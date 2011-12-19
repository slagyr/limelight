package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.events.Event;
import limelight.events.EventHandler;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.util.StringUtil;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class JavaPlayer implements Player
{
  private Class<?> playerClass;
  private String path;
  private Element element;
  private String eventsPrefix;

  public JavaPlayer(Class<?> playerClass, String path, Element element, String eventsPrefix)
  {
    this.playerClass = playerClass;
    this.path = path;
    this.element = element;
    this.eventsPrefix = eventsPrefix;
  }

  public Object instantiatePlayer()
  {
    try
    {
      final Constructor constructor = playerClass.getConstructor();
      return constructor.newInstance();
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }

  public Object cast(PropPanel prop)
  {
    EventHandler eventHandler = prop.getEventHandler();
    Object player = cast(eventHandler);
    invokeCastEvents(player, prop, element);
    return player;
  }

  public Object cast(EventHandler eventHandler)
  {
    Object player = instantiatePlayer();
    applyEvents(player, element, eventsPrefix, eventHandler);
    return player;
  }

  public String getPath()
  {
    return path;
  }

  public String getName()
  {
    return Context.fs().baseName(path);
  }

  private void invokeCastEvents(Object player, PropPanel prop, Element playerElement)
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

  public void applyEvents(Object player, Element element, String eventsPrefix, EventHandler eventHandler)
  {
    for(Element eventElement : Xml.loadChildElements(element))
    {
      String name = eventElement.getNodeName();
      String methodName = eventElement.getTextContent();
      addEventActionFor(player, eventsPrefix, name, methodName, eventHandler);
    }
  }

  public void addEventActionFor(Object player, String eventPrefix, String eventName, String methodName, EventHandler eventHandler)
  {
    String eventClassName = eventPrefix + StringUtil.capitalCamalize(eventName.substring(2)) + "Event";

    if("limelight.ui.events.panel.CastEvent".equals(eventClassName))
      return;

    Class<? extends Event> eventClass;
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
    for(Method method : playerClass.getMethods())
    {
      if(methodName.equals(method.getName()))
        return method;
    }
    throw new NoSuchMethodError(methodName + " for " + playerClass);
  }

  public Class<?> getPlayerClass()
  {
    return playerClass;
  }
}
