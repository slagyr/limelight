package limelight.java;

import limelight.events.EventAction;
import limelight.io.FakeFileSystem;
import limelight.io.StreamReader;
import limelight.model.Stage;
import limelight.model.events.ProductionCreatedEvent;
import limelight.styles.RichStyle;
import limelight.ui.model.FramedStage;
import limelight.ui.model.PropPanel;
import limelight.ui.model.Scene;
import limelight.ui.model.ScenePanel;
import limelight.util.OptionsMap;
import limelight.util.TestUtil;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JavaProductionTest
{
  private FakeFileSystem fs;
  private JavaProduction production;

  @Before
  public void setUp() throws Exception
  {
    fs = FakeFileSystem.installed();
    production = new JavaProduction("/testProduction");
  }
  
  @Test
  public void loadsStages() throws Exception
  {
    fs.createTextFile("/testProduction/stages.xml", "<stages><egats/></stages>");
    production.loadStages();

    final Stage stage = production.getTheater().get("egats");
    assertNotNull(stage);
    assertEquals("egats", stage.getName());
  }

  @Test
  public void loadsStagesWithOptions() throws Exception
  {
    fs.createTextFile("/testProduction/stages.xml", "<stages><egats title='Eureka!'/></stages>");
    production.loadStages();

    final Stage stage = production.getTheater().get("egats");
    assertEquals(FramedStage.class, stage.getClass());
    FramedStage framedStage = (FramedStage)stage;
    assertEquals("Eureka!", framedStage.getTitle());
  }

  @Test
  public void loadsScene() throws Exception
  {
    final Scene result = production.loadScene("aScene", new OptionsMap());

    assertEquals(ScenePanel.class, result.getClass());
    ScenePanel scene = (ScenePanel)result;
    assertEquals(JavaScene.class, scene.getProxy().getClass());
    scene.illuminate();
    assertEquals("aScene", scene.getName());
  }

  @Test
  public void loadsSceneWithOptions() throws Exception
  {
    final Map<String,Object> options = Util.toMap("backgroundColor", "red", "shouldAllowClose", false);
    ScenePanel result = (ScenePanel)production.loadScene("aScene", options);

    result.illuminate();
    assertEquals(false, result.shouldAllowClose());
    assertEquals("#ff0000ff", result.getStyle().getBackgroundColor());
  }

  @Test
  public void loadsSceneWithProps() throws Exception
  {
    fs.createTextFile("/testProduction/aScene/props.xml", "<props><child><grandchild/></child><child2/></props>");
    final Map<String,Object> options = Util.toMap("backgroundColor", "red", "shouldAllowClose", false);
    ScenePanel scene = (ScenePanel)production.loadScene("aScene", options);

    scene.illuminate();
    assertEquals(2, scene.getChildren().size());
    final PropPanel child1 = (PropPanel)scene.getChildren().get(0);
    assertEquals("child", child1.getName());
    final PropPanel child2 = (PropPanel)scene.getChildren().get(1);
    assertEquals("child2", child2.getName());

    assertEquals(1, child1.getChildren().size());
    final PropPanel grandChild = (PropPanel)child1.getChildren().get(0);
    assertEquals("grandchild", grandChild.getName());
    assertEquals(0, child2.getChildren().size());
  }
  
  @Test
  public void loadEmptyStylesForScene() throws Exception
  {
    final Scene scene = production.loadScene("aScene", new OptionsMap());

    production.loadStyles(scene);

    assertEquals(0, scene.getStylesStore().size());
  }
  
  @Test
  public void loadRealsStylesForScene() throws Exception
  {
    fs.createTextFile("/testProduction/aScene/styles.xml", "<styles><high x='0' y='99' float='on'/><far x='99' y='0' float='off'/></styles>");
    final Scene scene = production.loadScene("aScene", new OptionsMap());

    production.loadStyles(scene);

    assertEquals(2, scene.getStylesStore().size());
    final RichStyle high = scene.getStylesStore().get("high");
    assertEquals("0", high.getX());
    assertEquals("99", high.getY());
    assertEquals("on", high.getFloat());
    final RichStyle far = scene.getStylesStore().get("far");
    assertEquals("99", far.getX());
    assertEquals("0", far.getY());
    assertEquals("off", far.getFloat());
  }

  @Test
  public void illuminatingTheProductionWithNoPlayer() throws Exception
  {
    fs.createTextFile("/testProduction/production.xml", "<production></production>");

    production.illuminate();

    assertEquals(null, production.getPlayer());
    assertEquals("/testProduction/classes", production.getPlayerLoader().getClasspath());
  }

  @Test
  public void illuminatingTheProductionWithPlayer() throws Exception
  {
    writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/production.xml", "<production classpath='classes' class='SamplePlayer'></production>");

    production.illuminate();

    final Object player = production.getPlayer();
    assertNotNull(player);
    assertEquals("SamplePlayer", player.getClass().getName());
  }

  @Test
  public void illuminatingTheProductionWithAction() throws Exception
  {
    writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/production.xml", "<production classpath='classes' class='SamplePlayer'><onProductionCreated>sampleAction</onProductionCreated></production>");

    production.illuminate();

    final Object player = production.getPlayer();
    final List<EventAction> onCreatedActions = production.getEventHandler().getActions(ProductionCreatedEvent.class);
    assertEquals(1, onCreatedActions.size());

    new ProductionCreatedEvent().dispatch(production);
    assertEquals(1, player.getClass().getField("invocations").get(player));
  }

  public static void writeSamplePlayerTo(OutputStream outputStream) throws IOException
  {
    final String playerClassFile = TestUtil.dataDirPath("SamplePlayer.class");
    StreamReader reader = new StreamReader(new FileInputStream(playerClassFile));
    final byte[] classBytes = reader.readAllBytes();
    reader.close();
    outputStream.write(classBytes);
    outputStream.close();
  }
}
