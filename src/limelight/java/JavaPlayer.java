package limelight.java;

import limelight.LimelightException;
import limelight.events.Event;
import limelight.events.EventHandler;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.util.Options;
import limelight.util.StringUtil;
import limelight.util.Util;
import org.w3c.dom.Element;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class JavaPlayer implements Player
{
  private Class<?> playerClass;
  private String path;
  private Element element;
  private String eventsPrefix;
  private String name;

  public JavaPlayer(String name, String path, Class<?> playerClass, Element element, String eventsPrefix)
  {
    this.name = name;
    this.path = path;
    this.playerClass = playerClass;
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
    return name;
  }

  public Map<String, Object> applyOptions(PropPanel prop, Map<String, Object> options)
  {
    Object hand = prop.getStagehands().get(name);
    if(hand != null)
      Options.apply(hand, options);
    return options;
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
    String eventClassName = eventPrefix + StringUtil.capitalCamelCase(eventName.substring(2)) + "Event";

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
