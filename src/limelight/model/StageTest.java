//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.Context;
import limelight.model.api.FakePropProxy;
import limelight.model.api.MockStageProxy;
import limelight.ui.events.panel.SceneOpenedEvent;
import limelight.ui.events.stage.StageClosedEvent;
import limelight.ui.events.stage.StageClosingEvent;
import limelight.ui.model.*;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StageTest
{
  private Stage stage;

  @Before
  public void setUp() throws Exception
  {
    Context.instance().frameManager = new InertFrameManager();
    Context.instance().keyboardFocusManager = new limelight.ui.KeyboardFocusManager();
    stage = new TestableStage("default", new MockStageProxy());
  }

  @Test
  public void shouldVitality() throws Exception
  {
    assertEquals(true, stage.isVital());

    stage.setVital(false);

    assertEquals(false, stage.isVital());
  }

  @Test
  public void shouldShouldAllowClose() throws Exception
  {
    assertEquals(true, stage.shouldAllowClose());

    ScenePanel scene = new ScenePanel(new FakePropProxy());
    stage.setScene(scene);
    scene.setShouldAllowClose(true);
    assertEquals(true, stage.shouldAllowClose());

    scene.setShouldAllowClose(false);
    assertEquals(false, stage.shouldAllowClose());
  }
  
  @Test
  public void defaultSceneName() throws Exception
  {
    assertEquals(null, stage.getDefaultSceneName());

    stage.setDefaultSceneName("blah");

    assertEquals("blah", stage.getDefaultSceneName());
  }

  @Test
  public void shouldRemainHidden() throws Exception
  {
    assertEquals(false, stage.shouldRemainHidden());
    stage.setShouldRemainHidden(true);
    assertEquals(true, stage.shouldRemainHidden());

    stage.open();
    assertEquals(false, stage.isVisible());

    stage.hide();
    stage.show();
    assertEquals(false, stage.isVisible());
  }
  
  @Test
  public void shouldNotRemainHidden() throws Exception
  {
    stage.setShouldRemainHidden(false);

    stage.open();

    assertEquals(true, stage.isVisible());
  }
  
  @Test
  public void closing() throws Exception
  {
    MockEventAction closingAction = new MockEventAction();
    MockEventAction closedAction = new MockEventAction();
    stage.getEventHandler().add(StageClosingEvent.class, closingAction);
    stage.getEventHandler().add(StageClosedEvent.class, closedAction);

    stage.close();

    assertEquals(true, closingAction.invoked);
    assertEquals(false, stage.isVisible());
    assertEquals(false, stage.isOpen());
    assertEquals(true, closedAction.invoked);
  }
  
  @Test
  public void scenesOnClosingStageGetStageSetToNull() throws Exception
  {
    MockScene scene = new MockScene();
    stage.setScene(scene);
    stage.open();

    stage.close();

    assertEquals(null, stage.getScene());
    assertEquals(null, scene.getStage());
  }

  @Test
  public void settingTheScene() throws Exception
  {
    MockScene scene = new MockScene();

    stage.setScene(scene);

    assertEquals(stage, scene.getStage());
  }

  @Test
  public void whatHappensToPreviousSceneWhenSettingTheScene() throws Exception
  {
    MockScene originalScene = new MockScene();
    stage.setScene(originalScene);

    MockScene newScene = new MockScene();
    stage.setScene(newScene);

    assertEquals(null, originalScene.getStage());
  }

  @Test
  public void settingTheSceneOnAnOpenedStage() throws Exception
  {
    stage.open();
    MockScene scene = new MockScene();
    MockEventAction action = new MockEventAction();
    scene.getEventHandler().add(SceneOpenedEvent.class, action);

    stage.setScene(scene);

    assertEquals(stage, scene.getStage());
    assertEquals(true, action.invoked);
  }
  
  @Test
  public void openingAStageWithAScene() throws Exception
  {
    MockScene scene = new MockScene();
    MockEventAction action = new MockEventAction();
    scene.getEventHandler().add(SceneOpenedEvent.class, action);

    stage.setScene(scene);

    assertEquals(false, action.invoked);

    stage.open();

    assertEquals(true, action.invoked);
  }
}
