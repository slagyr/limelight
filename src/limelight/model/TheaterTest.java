package limelight.model;

import limelight.LimelightException;
import limelight.model.api.MockTheaterProxy;
import limelight.ui.events.stage.StageActivatedEvent;
import limelight.ui.events.stage.StageClosedEvent;
import limelight.ui.events.stage.StageDeactivatedEvent;
import limelight.ui.model.MockStage;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class TheaterTest
{
  private FakeProduction production;
  private Theater theater;
  private MockStage defaultStage;

  @Before
  public void setUp() throws Exception
  {
    production = new FakeProduction();
    theater = new Theater(production);
    defaultStage = new MockStage("default");
  }

  @Test
  public void getProduction() throws Exception
  {
    assertEquals(production, theater.getProduction());
  }

  @Test
  public void stagesCanBeAdded() throws Exception
  {
    theater.add(defaultStage);

    assertEquals(1, theater.getStages().size());
    assertSame(defaultStage, theater.getStages().get(0));
    assertSame(theater, defaultStage.getTheater());
  }

  @Test
  public void modifyingTheReturnedStagesIsNotDamaging() throws Exception
  {
    theater.add(defaultStage);
    theater.getStages().clear();
    assertEquals(1, theater.getStages().size());
  }

  @Test
  public void activeStage() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    theater.add(defaultStage);
    theater.add(stage2);

    assertEquals(null, theater.getActiveStage());

    new StageActivatedEvent().dispatch(stage2);
    assertEquals(stage2, theater.getActiveStage());

    new StageActivatedEvent().dispatch(defaultStage);
    assertEquals(defaultStage, theater.getActiveStage());
  }

  @Test
  public void removedStagesDoNotGetActivated() throws Exception
  {
    theater.add(defaultStage);
    theater.remove(defaultStage);                    

    new StageActivatedEvent().dispatch(defaultStage);
    assertEquals(null, theater.getActiveStage());
  }
  
  @Test
  public void getStageByName() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    MockStage stage3 = new MockStage("three");
    theater.add(defaultStage);
    theater.add(stage2);
    theater.add(stage3);

    assertEquals(defaultStage, theater.get("default"));
    assertEquals(stage2, theater.get("two"));
    assertEquals(stage3, theater.get("three"));
  }
  
  @Test
  public void doesNotAllowDuplicateNames() throws Exception
  {
    MockStage stage2 = new MockStage("default");
    theater.add(defaultStage);

    try
    {
      theater.add(stage2);
      fail("should throw error");
    }
    catch(LimelightException e)
    {
      assertEquals("Duplicate stage name: 'default'", e.getMessage());
    }
  }

  @Test
  public void defaultStage() throws Exception
  {
    theater.setProxy(new MockTheaterProxy());

    Stage defaultStage = theater.getDefaultStage();
    assertEquals("Limelight", defaultStage.getName());
    assertEquals(theater, defaultStage.getTheater());
    assertSame(defaultStage, theater.getDefaultStage());
  }

  @Test
  public void closedStagesAreRemoved() throws Exception
  {
    theater.add(defaultStage);

    new StageClosedEvent().dispatch(defaultStage);

    assertEquals(null, theater.get("default"));
    assertEquals(null, theater.getActiveStage());
  }

  @Test
  public void tryToCloseTheProductionWhenEmptied() throws Exception
  {
    theater.add(defaultStage);
    new StageClosedEvent().dispatch(defaultStage);

    assertEquals(true, production.closeAttempted);
  }

  @Test
  public void doesntTryToCloseTheProductionWhenEmptiedButHasVitalStages() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    theater.add(defaultStage);
    theater.add(stage2);
    stage2.setVital(true);

    new StageClosedEvent().dispatch(defaultStage);

    assertEquals(false, production.closeAttempted);
  }

  @Test
  public void canClose() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    MockStage stage3 = new MockStage("three");
    theater.add(defaultStage);
    theater.add(stage2);
    theater.add(stage3);

    theater.close();

    assertEquals(0, theater.getStages().size());
    assertEquals(null, theater.getActiveStage());
    assertEquals(false, theater.isOpen());
  }

  @Test
  public void knowsWhenStagesAreDeactivated() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    theater.add(defaultStage);
    theater.add(stage2);

    new StageActivatedEvent().dispatch(defaultStage);
    new StageDeactivatedEvent().dispatch(defaultStage);
    assertEquals(null, theater.getActiveStage());
  }

  @Test
  public void attemptsToCloseProductionWhenAllStagesAreHidden() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    defaultStage.setVital(false);
    stage2.setVital(false);
    theater.add(defaultStage);
    theater.add(stage2);

    defaultStage.visible = false;
    stage2.visible = false;
    new StageDeactivatedEvent().dispatch(defaultStage);
    new StageDeactivatedEvent().dispatch(stage2);

    assertEquals(true, production.closeAttempted);
  }

  @Test
  public void doesntAttemptsToCloseProductionWhenAllStagesAreHiddenButStillHasVitalStage() throws Exception
  {
    MockStage stage2 = new MockStage("two");
    defaultStage.setVital(false);
    stage2.setVital(true);
    theater.add(defaultStage);
    theater.add(stage2);

    defaultStage.visible = false;
    stage2.visible = false;
    new StageDeactivatedEvent().dispatch(defaultStage);
    new StageDeactivatedEvent().dispatch(stage2);

    assertEquals(false, production.closeAttempted);
  }
}
