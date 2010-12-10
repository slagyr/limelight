package limelight.java;

import limelight.Context;
import limelight.model.api.CastingDirector;
import limelight.model.api.PropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    final String path = playersPath + "/" + StringUtil.camalize(playerName) + ".xml";
    return path;
  }

  public void castPlayer(PropProxy propProxy, String playerName, String playersPath)
  {
    String playerPath = playerFilePath(playerName, playersPath);
    JavaProp prop = (JavaProp) propProxy;
    final Document document = Xml.loadDocumentFrom(playerPath);
    final Element playerElement = document.getDocumentElement();
    final Object player = JavaPlayers.toPlayer(playerElement, classLoader, "limelight.ui.events.panel.", prop.getPeer().getEventHandler());
    prop.addPlayer(player);
    invokeCastEvents(prop, playerElement, player);
  }

  private void invokeCastEvents(JavaProp prop, Element playerElement, Object player)
  {
    final CastEvent castEvent = new CastEvent(prop.getPeer());
    for(Element child : Xml.loadChildElements(playerElement))
    {
      if("onCast".equals(child.getNodeName()))
      {
        String methodName = child.getTextContent().trim();
        final Method method = JavaPlayers.findMethod(methodName, player);
        new JavaEventAction(player, method).invoke(castEvent);
      }
    }
  }

  public ClassLoader getClassLoader()
  {
    return classLoader;
  }
}
