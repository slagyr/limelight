package limelight.java;

import limelight.io.FileUtil;
import limelight.model.Production;
import limelight.model.Theater;
import limelight.styles.RichStyle;
import limelight.ui.model.Scene;
import org.w3c.dom.*;
import java.util.Map;

public class JavaProduction extends Production
{
  private PlayerClassLoader playerLoader;
  private Object player;

  public JavaProduction(String path)
  {
    super(path);
    playerLoader = new PlayerClassLoader(getResourceLoader().pathTo("classes"));
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
    final Theater theater = getTheater();
    for(Element stageElement : Xml.loadRootElements(stagesPath))
      Xml.toStage(theater, stageElement);
  }

  @Override
  protected Scene loadScene(String scenePath, Map<String, Object> options)
  {
    options.put("path", getResourceLoader().pathTo(scenePath));
    options.put("name", FileUtil.filename(scenePath));
    JavaScene scene = new JavaScene(this, options);

    final String propsPath = scene.getPeer().getResourceLoader().pathTo("props.xml");
    for(Element propElement : Xml.loadRootElements(propsPath))
      scene.add(Xml.toProp(propElement));

    return scene.getPeer();
  }

  @Override
  protected void loadStyles(Scene scene)
  {
    final String stylesPath = scene.getResourceLoader().pathTo("styles.xml");
    final Map<String, RichStyle> map = scene.getStylesStore();
    for(Element styleElement : Xml.loadRootElements(stylesPath))
      Xml.toStyle(styleElement, map);
  }

  @Override
  protected void prepareToOpen()
  {
  }

  @Override
  protected void finalizeClose()
  {
  }

  public PlayerClassLoader getPlayerLoader()
  {
    return playerLoader;
  }
}
