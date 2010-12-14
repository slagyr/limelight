package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.Production;
import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import org.w3c.dom.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JavaProduction extends Production
{
  private PlayerClassLoader playerLoader;
  private Object player;
  private JavaTheater javaTheater;

  public JavaProduction(String path)
  {
    super(path);
    playerLoader = new PlayerClassLoader(getResourceLoader().pathTo("classes"));
    javaTheater = new JavaTheater(getTheater());
  }

  public Object getPlayer()
  {
    return player;
  }

  @Override
  protected void illuminate()
  {
    final String productionPlayerPath = getResourceLoader().pathTo("production.xml");
    final Document document = Xml.loadDocumentFrom(productionPlayerPath);
    Element productionElement = document.getDocumentElement();

    String classpath = productionElement.getAttribute("classpath");
    if(classpath != null && classpath.length() > 0)
      playerLoader.setClasspath(getResourceLoader().pathTo(classpath));

    player = JavaPlayers.toPlayer(productionElement, this.playerLoader, "limelight.model.events.", getEventHandler());
  }

  @Override
  protected void loadLibraries()
  {
  }

  @Override
  protected void loadStages()
  {
    final String stagesPath = getResourceLoader().pathTo("stages.xml");
    for(Element stageElement : Xml.loadRootElements(stagesPath))
      Xml.toStage(javaTheater, stageElement);
  }

  @Override
  protected Scene loadScene(String scenePath, Map<String, Object> options)
  {
    options.put("path", getResourceLoader().pathTo(scenePath));
    options.put("name", Context.fs().filename(scenePath));
    JavaScene scene = new JavaScene(this, options);

    final String propsPath = scene.getPeer().getResourceLoader().pathTo("props.xml");
    for(Element propElement : Xml.loadRootElements(propsPath))
      scene.add(Xml.toProp(propElement));

    return scene.getPeer();
  }

  @Override
  protected Map<String, RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles)
  {
    final String stylesPath = Context.fs().join(path, "styles.xml");
    final Map<String, RichStyle> map = new HashMap<String, RichStyle>();
    for(Element styleElement : Xml.loadRootElements(stylesPath))
      Xml.toStyle(styleElement, map);
    return map;
  }

  @Override
  protected void prepareToOpen()
  {
  }

  @Override
  protected void finalizeClose()
  {
  }

  @Override
  public Object send(String name, Object... args)
  {
    try
    {
      for(Method method : player.getClass().getMethods())
      {
        if(method.getName().equals(name))
          return method.invoke(player, args);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new LimelightException("Failed to send call to Production player: " + name, e);
    }
    throw new LimelightException("No method found on Production player: " + name);
  }

  public PlayerClassLoader getPlayerLoader()
  {
    return playerLoader;
  }
}
