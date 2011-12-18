//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.api.PlayerRecruiter;
import limelight.model.api.Player;
import limelight.model.api.PropProxy;
import limelight.ui.model.PropPanel;
import limelight.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;

public class JavaPlayerRecruiter implements PlayerRecruiter
{
  private ClassLoader classLoader;

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
    return playersPath + "/" + StringUtil.camalize(playerName) + ".xml";
  }

  public Player recruitPlayer(PropProxy propProxy, String playerName, String playersDir)
  {
    String playerPath = playerFilePath(playerName, playersDir);
    final Document document = Xml.loadDocumentFrom(playerPath);
    final Element playerElement = document.getDocumentElement();
    return toPlayer(playerPath, playerElement, classLoader, "limelight.ui.events.panel.");
  }

  public static JavaPlayer toPlayer(String path, Element element, ClassLoader classLoader, String eventsPrefix)
  {
    String className = element.getAttribute("class");
    if(className == null || className.length() == 0)
      return null;

    final Object rawPlayer = resolvePlayer(classLoader, className);
    return new JavaPlayer(rawPlayer, path, element, eventsPrefix);
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
