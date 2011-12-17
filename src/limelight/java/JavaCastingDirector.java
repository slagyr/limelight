//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.api.CastingDirector;
import limelight.model.api.Player;
import limelight.model.api.PropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class JavaCastingDirector implements CastingDirector
{
  private ClassLoader classLoader;

  public JavaCastingDirector(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }

  public boolean hasPlayer(String playerName, String playersPath)
  {
    return Context.fs().exists(playerFilePath(playerName, playersPath));
  }

  private String playerFilePath(String playerName, String playersPath)
  {
    return playersPath + "/" + StringUtil.camalize(playerName) + ".xml";
  }

  public void castPlayer(PropProxy propProxy, String playerName, String playersPath)
  {
    String playerPath = playerFilePath(playerName, playersPath);
    final Document document = Xml.loadDocumentFrom(playerPath);
    final Element playerElement = document.getDocumentElement();
    final Player player = toPlayer(playerElement, classLoader, "limelight.ui.events.panel.");

    if(propProxy instanceof JavaProp)
      ((JavaProp) propProxy).addPlayer(player);

    final PropPanel prop = (PropPanel) propProxy.getPeer();
    player.cast(prop);
  }

  public static JavaPlayer toPlayer(Element element, ClassLoader classLoader, String eventsPrefix)
  {
    String className = element.getAttribute("class");
    if(className == null || className.length() == 0)
      return null;

    final Object rawPlayer = resolvePlayer(classLoader, className);
    return new JavaPlayer(rawPlayer, element, eventsPrefix);
  }

  public static Object resolvePlayer(ClassLoader loader, String name)
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

  public ClassLoader getClassLoader()
  {
    return classLoader;
  }
}
