//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.model.Production;
import limelight.model.api.Player;
import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import org.w3c.dom.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JavaProduction extends Production
{
  private PlayerClassLoader playerLoader;
  private JavaPlayer player;
  private JavaTheater javaTheater;

  public JavaProduction(String path)
  {
    super(path);
    String classes = Context.fs().pathTo(getPath(), "classes");
    playerLoader = new PlayerClassLoader(classes);
    javaTheater = new JavaTheater(getTheater());
  }

  public Player getPlayer()
  {
    return player;
  }

  @Override
  protected void illuminate()
  {
    final String productionPlayerPath = Context.fs().pathTo(getPath(), "production.xml");
    final Document document = Xml.loadDocumentFrom(productionPlayerPath);
    Element productionElement = document.getDocumentElement();

    String classpath = productionElement.getAttribute("classpath");
    if(classpath != null && classpath.length() > 0)
      playerLoader.setClasspath(Context.fs().pathTo(getPath(), classpath));

    player = JavaCastingDirector.toPlayer(productionElement, this.playerLoader, "limelight.model.events.");
    if(player != null)
      player.applyEvents(productionElement, "limelight.model.events.", getEventHandler());
  }

  @Override
  protected void loadLibraries()
  {
  }

  @Override
  protected void loadStages()
  {
    final String stagesPath = Context.fs().pathTo(getPath(), "stages.xml");
    for(Element stageElement : Xml.loadRootElements(stagesPath))
      Xml.toStage(javaTheater, stageElement);
  }

  @Override
  protected Scene loadScene(String scenePath, Map<String, Object> options)
  {
    options.put("path", scenePath);
    options.put("name", Context.fs().filename(scenePath));
    JavaScene scene = new JavaScene(this, options);

    final String propsPath = Context.fs().pathTo(scene.getPeer().getPath(), "props.xml");
    for(Element propElement : Xml.loadRootElements(propsPath))
      scene.add(Xml.toProp(propElement));

    return scene.getPeer();
  }

  @Override
  protected Map<String, RichStyle> loadStyles(String path, Map<String, RichStyle> extendableStyles)
  {
    final String stylesPath = Context.fs().join(path, "styles.xml");
    return Xml.toStyles(stylesPath, new HashMap<String, RichStyle>(), extendableStyles);
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
