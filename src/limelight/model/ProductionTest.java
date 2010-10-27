package limelight.model;

import limelight.About;
import limelight.Context;
import limelight.model.api.*;
import limelight.model.events.*;
import limelight.ui.model.MockScene;
import limelight.ui.model.MockStage;
import limelight.ui.model.Scene;
import limelight.ui.model.inputs.MockEventAction;
import limelight.util.ResourceLoader;
import limelight.util.Util;
import limelight.util.Version;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

public class ProductionTest
{
  private FakeProduction production;
  private MockStudio studio;
  private MockEventAction action;

  @Before
  public void setUp() throws Exception
  {
    production = new FakeProduction("/foo/bar");
    studio = new MockStudio();
    Context.instance().studio = studio;
    action = new MockEventAction();
  }

  @Test
  public void nameIsSetBasedOnPath() throws Exception
  {
    assertEquals("bar", production.getName());
  }

  @Test
  public void settingTheName() throws Exception
  {
    production.setName("foo");

    assertEquals("foo", production.getName());
  }

  @Test
  public void allowClose() throws Exception
  {
    assertEquals(true, production.allowClose());

    production.setAllowClose(false);

    assertEquals(false, production.allowClose());
  }

  @Test
  public void hasLoaderWithRoot() throws Exception
  {
    ResourceLoader loader = production.getResourceLoader();

    assertEquals("/foo/bar", loader.getRoot());
  }

  @Test
  public void accessingTheProxy() throws Exception
  {
    ProductionProxy proxy = new FakeProductionProxy();
    assertEquals(null, production.getProxy());
    production.setProxy(proxy);

    assertEquals(proxy, production.getProxy());
  }

  @Test
  public void accessingTheCastingDirector() throws Exception
  {
    CastingDirector director = new FakeCastingDirector();

    production.setCastingDirector(director);

    assertEquals(director, production.getCastingDirector());
  }
  
  @Test
  public void settingProxySetsAllProxies() throws Exception
  {
    FakeProductionProxy proxy = new FakeProductionProxy();
    proxy.castingDirector = new FakeCastingDirector();
    proxy.theater = new MockTheaterProxy();

    production.setProxy(proxy);

    assertSame(proxy, production.getProxy());
    assertSame(proxy.castingDirector, production.getCastingDirector());
    assertSame(proxy.theater, production.getTheater().getProxy());
  }
  
  @Test
  public void minimumLimelightVersion() throws Exception
  {
    assertEquals("0.0.0", production.getMinimumLimelightVersion());

    production.setMinimumLimelightVersion("9.9.9");

    assertEquals("9.9.9", production.getMinimumLimelightVersion());
  }

  @Test
  public void closing() throws Exception
  {
    production.open();
    assertNull(production.getClosingThread());

    production.close();

    assertNotNull(production.getClosingThread());
    production.getClosingThread().join();
    assertEquals(true, production.closeFinalized);
    assertEquals(false, production.isOpen());
  }
  
    @Test
  public void isLimelightVersionCompatible() throws Exception
  {
    Version version = About.version;
    assertEquals(true, production.isLimelightVersionCompatible());

    production.setMinimumLimelightVersion(version.toString());
    assertEquals(true, production.isLimelightVersionCompatible());

    final String minusOne = new Version(version.getMajor(), version.getMinor(), version.getPatch() - 1).toString();
    production.setMinimumLimelightVersion(minusOne);
    assertEquals(true, production.isLimelightVersionCompatible());

    final String plusOne = new Version(version.getMajor(), version.getMinor(), version.getPatch() + 1).toString();
    production.setMinimumLimelightVersion(plusOne);
    assertEquals(false, production.isLimelightVersionCompatible());
  }
  
  @Test
  public void canProceedWithCompatibility() throws Exception
  {
    assertEquals(true, production.canProceedWithCompatibility());

    production.setMinimumLimelightVersion("99.99.99");
    studio.shouldProceedWithIncompatibleVersion = false;
    assertEquals(false, production.canProceedWithCompatibility());

    studio.shouldProceedWithIncompatibleVersion = true;
    assertEquals(true, production.canProceedWithCompatibility());
  }
  
  @Test
  public void illuminateProduction() throws Exception
  {
    production.getEventHandler().add(ProductionCreatedEvent.class, action);

    production.illuminateProduction();

    assertEquals(true, production.illuminated);
    assertEquals(true, action.invoked);
  }

  @Test
  public void loadProduction() throws Exception
  {
    production.getEventHandler().add(ProductionLoadedEvent.class, action);

    production.loadProduction();

    assertEquals(true, production.librariesLoaded);
    assertEquals(true, production.stagesLoaded);
    assertEquals(true, action.invoked);
  }
  
  @Test
  public void openScene() throws Exception
  {
    MockStage stage = new MockStage();
    Scene scene = new MockScene();
    production.stubbedScene = scene;

    production.openScene("scenePath", stage, Util.toMap());

    assertEquals("scenePath", production.openedScenePath);
    assertEquals(scene, production.loadStylesScene);
    assertEquals(scene, stage.getScene());
    assertEquals(true, stage.opened);
  }
           
  @Test
  public void closeProduction() throws Exception
  {
    production.open();
    MockEventAction closingAction = new MockEventAction();
    MockEventAction closedAction = new MockEventAction();
    production.getEventHandler().add(ProductionClosingEvent.class, closingAction);
    production.getEventHandler().add(ProductionClosedEvent.class, closedAction);

    production.close();
    production.getClosingThread().join();

    assertEquals(true, closingAction.invoked);
    assertEquals(false, production.getTheater().isOpen());
    assertEquals(true, closedAction.invoked);    
  }
  
  @Test
  public void openDefaultScenes() throws Exception
  {
    production.getEventHandler().add(ProductionOpenedEvent.class, action);
    MockStage stage = new MockStage();
    production.getTheater().add(stage);
    stage.setDefaultSceneName("defaultScene");
    Scene scene = new MockScene();
    production.stubbedScene = scene;

    production.openDefaultScenes(Util.toMap());

    assertEquals(true, action.invoked);
    assertEquals(true, stage.isOpen());
    assertEquals(scene, stage.getScene());
  }
}
