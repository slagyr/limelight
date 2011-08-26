//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.LimelightException;
import limelight.events.Event;
import limelight.events.EventHandler;
import limelight.util.StringUtil;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class JavaPlayers
{
  public static Object toPlayer(Element element, ClassLoader classLoader, String eventsPrefix, EventHandler eventHandler)
  {
    String className = element.getAttribute("class");
    Object player = null;
    if(className != null && className.length() > 0)
    {
      player = loadPlayer(classLoader, className);    
      for(Element eventElement : Xml.loadChildElements(element))
      {
        String name = eventElement.getNodeName();
        String methodName = eventElement.getTextContent();
        JavaPlayers.addEventActionFor(eventsPrefix, name, methodName, player, eventHandler);
      }
    }
    return player;
  }

  public static void addEventActionFor(String eventPrefix, String eventName, String methodName, Object player, EventHandler eventHandler)
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

    Method eventMethod = findMethod(methodName, player);
    final JavaEventAction action = new JavaEventAction(player, eventMethod);
    eventHandler.add(eventClass, action);
  }

  public static Method findMethod(String methodName, Object player)
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

  public static Object loadPlayer(ClassLoader loader, String name)
  {
    try
    {
      Class playerClass = loader.loadClass(name);
      final Constructor constructor = playerClass.getConstructor();
      return constructor.newInstance();
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }
}
