//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package utilities;

import limelight.About;
import limelight.Boot;
import limelight.builtin.BuiltinBeacon;
import limelight.java.JavaPlayer;
import limelight.java.JavaProduction;
import limelight.java.JavaProp;
import limelight.java.JavaScene;
import limelight.model.Stage;
import limelight.ui.model.FramedStage;
import limelight.ui.model.StageFrame;
import limelight.util.Mouse;
import org.junit.*;

import java.awt.*;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class UtilitiesTest
{
  private JavaProduction production;
  private Utilities utilities;
  private Object dialogResponse;
  private Thread thread;

  @BeforeClass
  public static void suiteSetUp()
  {
    StageFrame.hiddenMode = true;
  }

  @Before
  public void setUp() throws Exception
  {
    assumeTrue(!GraphicsEnvironment.isHeadless()); //TODO MDM - Need to see if we can get these tests viable without needing a graphics env
    Boot.boot("start-background-threads", false);
    production = new JavaProduction(BuiltinBeacon.getBuiltinProductionsPath() + "/utilities");
    production.open();
    utilities = (Utilities) production.getBackstage().get("utilities");
  }

  @After
  public void tearDown() throws Exception
  {
    if(thread != null && thread.isAlive())
      thread.interrupt();
    if(production != null)
    {
      for(Stage stage : production.getTheater().getStages())
        stage.close();
    }
  }

  private void startProceedWithIncompatibleVersion()
  {
    thread = new Thread(new Runnable()
    {
      public void run()
      {
        dialogResponse = utilities.canProceedWithIncompatibleVersion("Some Production", "1.2.3");
      }
    });
    thread.start();
  }

  private void startAlert(final String message)
  {
    thread = new Thread(new Runnable()
    {
      public void run()
      {
        dialogResponse = utilities.alert(message);
      }
    });
    thread.start();
  }

  private static interface Condition
  {
    boolean isMet();
  }

  private void waitFor(Condition condition) throws Exception
  {
    int checks = 0;
    for(; checks < 50 && !condition.isMet(); checks++)
      Thread.sleep(100);
    if(checks == 50)
      throw new Exception("The condition was never met.");
  }

  private FramedStage waitForStage(final String name) throws Exception
  {
    waitFor(new Condition()
    {
      public boolean isMet()
      {
        final Stage stage = production.getTheater().get(name);
        return stage != null && stage.isVisible();
      }
    });

    return (FramedStage) production.getTheater().get(name);
  }

  @Test
  public void productionDoesntAllowClose() throws Exception
  {
    assertEquals(false, production.allowClose());
  }

  @Test
  public void opensWithNoScenesOrStagesOpen() throws Exception
  {
    assertEquals(0, production.getTheater().getStages().size());
  }

  @Test
  public void alertStage() throws Exception
  {
    startAlert("A Friendly Message.");
    final FramedStage stage = waitForStage("Alert");
    assertNotNull(stage);

    assertEquals("Alert", stage.getTitle());

    assertEquals("center", stage.getXLocationStyle().toString());
    assertEquals("center", stage.getYLocationStyle().toString());
    assertEquals("400", stage.getWidthStyle().toString());
    assertEquals("auto", stage.getHeightStyle().toString());
    assertEquals(false, stage.isFramed());
    assertEquals(false, stage.isVital());
    assertEquals(true, stage.isAlwaysOnTop());
  }

  @Test
  public void alertScene() throws Exception
  {
    startAlert("A friendly message.");
    FramedStage alertStage = waitForStage("Alert");
    JavaScene scene = (JavaScene) alertStage.getScene().getProxy();

    assertEquals("Limelight Alert", scene.findProp("title").getText());
    assertEquals("A friendly message.", scene.findProp("advice").getText());
  }

  @Test
  public void alertSceneClosesWhenClickingOk() throws Exception
  {
    startAlert("A friendly message.");
    FramedStage alertStage = waitForStage("Alert");
    JavaScene scene = (JavaScene) alertStage.getScene().getProxy();

    final JavaProp okButton = scene.findProp("okButton");
    assertNotNull(okButton);

    Mouse.click(okButton);

    waitForDialogResponse();
    assertEquals(true, dialogResponse);
  }

  private void waitForDialogResponse()
    throws Exception
  {
    waitFor(new Condition()
    {
      public boolean isMet()
      {
        return dialogResponse != null;
      }
    });
  }

  @Test
  public void stageHeightWithSmallText() throws Exception
  {
    startAlert("A friendly message.");
    FramedStage alertStage = waitForStage("Alert");
    Thread.sleep(100);
    JavaScene scene = (JavaScene) alertStage.getScene().getProxy();

    JavaProp advice = scene.findProp("advice");

    assertEquals(true, advice.getBounds().height < 100);
  }

  @Test
  public void stageHeightWithLargeText() throws Exception
  {
    String message = "";
    for(int i = 0; i < 100; i++)
      message += "This is line number" + i + "\n";
    startAlert(message);
    FramedStage alertStage = waitForStage("Alert");
    Thread.sleep(100);
    JavaScene scene = (JavaScene) alertStage.getScene().getProxy();

    JavaProp advice = scene.findProp("advice");

    assertEquals(500, advice.getBounds().height);
    assertEquals("on", advice.getStyle().getVerticalScrollbar());
  }

  @Test
  public void stageForIncompatibleVersion() throws Exception
  {
    startProceedWithIncompatibleVersion();
    FramedStage stage = waitForStage("Incompatible Version");

    assertEquals("center", stage.getXLocationStyle().toString());
    assertEquals("center", stage.getYLocationStyle().toString());
    assertEquals("400", stage.getWidthStyle().toString());
    assertEquals("auto", stage.getHeightStyle().toString());
    assertEquals("#ffffffff", stage.getBackgroundColor());
    assertEquals(false, stage.isFramed());
    assertEquals(true, stage.isAlwaysOnTop());
    assertEquals(false, stage.isVital());
  }

  @Test
  public void incompatibleVersionSceneComponents() throws Exception
  {
    startProceedWithIncompatibleVersion();
    FramedStage stage = waitForStage("Incompatible Version");
    JavaScene scene = (JavaScene) stage.getScene().getProxy();

    assertEquals("Some Production", scene.findProp("productionNameLabel").getText());
    assertEquals("1.2.3", scene.findProp("requiredVersionLabel").getText());
    assertEquals(About.version.toString(), scene.findProp("currentVersionLabel").getText());
  }

  @Test
  public void returnsTrueWhenClickingProceed() throws Exception
  {
    startProceedWithIncompatibleVersion();
    FramedStage stage = waitForStage("Incompatible Version");
    JavaScene scene = (JavaScene) stage.getScene().getProxy();

    Mouse.click(scene.findProp("proceedButton"));
    waitForDialogResponse();

    assertEquals(true, dialogResponse);
  }

  @Test
  public void returnsFalseWhenClickingCancel() throws Exception
  {
    startProceedWithIncompatibleVersion();
    FramedStage stage = waitForStage("Incompatible Version");
    JavaScene scene = (JavaScene) stage.getScene().getProxy();

    Mouse.click(scene.findProp("cancelButton"));
    waitForDialogResponse();

    assertEquals(false, dialogResponse);
  }

  @Test
  public void riggerScene() throws Exception
  {
    utilities.openRigger();
    final Stage stage = production.getTheater().get("rigger");
    assertEquals(true, stage.isOpen());
    assertEquals("rigger", stage.getScene().getAbsoluteName());
  }
}
