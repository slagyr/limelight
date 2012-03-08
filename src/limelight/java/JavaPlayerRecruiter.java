//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.api.PlayerRecruiter;
import limelight.model.api.Player;
import limelight.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.HashMap;

public class JavaPlayerRecruiter implements PlayerRecruiter
{
  private ClassLoader classLoader;
  private HashMap<String, JavaPlayer> playerCache = new HashMap<String, JavaPlayer>();

  public JavaPlayerRecruiter(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }

  public boolean canRecruit(String playerName, String playersPath)
  {
    return Context.fs().exists(playerFilePath(playerName, playersPath));
  }

  private String playerFilePath(String playerName, String playersPath)
  {
    return playersPath + "/" + StringUtil.camelCase(playerName) + ".xml";
  }

  public Player recruitPlayer(String name, String dir)
  {
    String path = playerFilePath(name, dir);
    if(!playerCache.containsKey(path))
    {
      final Document document = Xml.loadDocumentFrom(path);
      final Element playerElement = document.getDocumentElement();
      final JavaPlayer player = toPlayer(name, path, playerElement, classLoader, "limelight.ui.events.panel.");
      if(player != null)
        playerCache.put(path, player);
    }
    return playerCache.get(path);
  }

  public static JavaPlayer toPlayer(String name, String path, Element element, ClassLoader classLoader, String eventsPrefix)
  {
    String className = element.getAttribute("class");
    if(className == null || className.length() == 0)
      return null;

    final Class<?> playerClass = resolveClass(classLoader, className);
    return new JavaPlayer(name, path, playerClass, element, eventsPrefix);
  }

  private static Class<?> resolveClass(ClassLoader classLoader, String className)
  {
    try
    {
      return classLoader.loadClass(className);
    }
    catch(ClassNotFoundException e)
    {
      throw new LimelightException(e);
    }
  }

  public ClassLoader getClassLoader()
  {
    return classLoader;
  }
}
