//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model;

import limelight.LimelightException;
import limelight.Log;
import limelight.events.Event;
import limelight.events.EventAction;
import limelight.model.api.TheaterProxy;
import limelight.ui.events.stage.StageActivatedEvent;
import limelight.ui.events.stage.StageClosedEvent;
import limelight.ui.events.stage.StageDeactivatedEvent;
import limelight.util.Util;

import java.io.PushbackReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Theater
{
  private Production production;
  private Queue<Stage> stages = new ConcurrentLinkedQueue<Stage>();
  private Stage activeStage;
  private TheaterProxy proxy;
  private boolean open = true;

  public Theater(Production production)
  {
    this.production = production;
  }

  public Production getProduction()
  {
    return production;
  }

  public void setProxy(TheaterProxy proxy)
  {
    this.proxy = proxy;
  }

  public TheaterProxy getProxy()
  {
    return proxy;
  }

  public void add(Stage stage)
  {
    Log.info("Theater - adding stage: " + stage.getName());
    if(get(stage.getName()) != null)
      throw new LimelightException("Duplicate stage name: '" + stage.getName() + "'");
    stages.add(stage);
    stage.getEventHandler().add(StageActivatedEvent.class, ActivateStageAction.instance);
    stage.getEventHandler().add(StageDeactivatedEvent.class, DeactivateStageAction.instance);
    stage.getEventHandler().add(StageClosedEvent.class, StageClosedAction.instance);
    stage.setTheater(this);
  }

  public void remove(Stage stage)
  {
    if(stage.isOpen())
      stage.close();
    stage.getEventHandler().remove(StageActivatedEvent.class, ActivateStageAction.instance);
    stage.getEventHandler().remove(StageDeactivatedEvent.class, DeactivateStageAction.instance);
    stage.getEventHandler().remove(StageClosedEvent.class, StageClosedAction.instance);
    stages.remove(stage);

    checkForEmptyTheater();
  }

  public Stage get(String name)
  {
    if(name == null)
      return null;

    for(Stage stage : stages)
    {
      if(stage.getName().equals(name))
        return stage;
    }
    return null;
  }

  public List<Stage> getStages()
  {
    return new LinkedList<Stage>(stages);
  }

  public boolean hasStages()
  {
    return !stages.isEmpty();
  }

  public Stage getActiveStage()
  {
    return activeStage;
  }

  public Stage getDefaultStage()
  {
    if(get("Limelight") == null)
      add(proxy.buildStage("Limelight", Util.toMap()));
    return get("Limelight");
  }

  public boolean isOpen()
  {
    return open;
  }

  public void close()
  {
    for(Stage stage : getStages())
      remove(stage);
    open = false;
  }

  private void checkForEmptyTheater()
  {
    if(stages.isEmpty() || (!hasVisibleStage() && !hasVitalStage()))
      production.attemptClose();
  }

  private boolean hasVitalStage()
  {
    for(Stage stage : stages)
    {
      if(stage.isVital())
        return true;
    }
    return false;
  }

  private boolean hasVisibleStage()
  {
    for(Stage stage : stages)
    {
      if(stage.isVisible())
        return true;
    }
    return false;
  }

  private static class ActivateStageAction implements EventAction
  {
    public static ActivateStageAction instance = new ActivateStageAction();
    public void invoke(Event event)
    {
      final Stage stage = (Stage) event.getSubject();
      stage.getTheater().activeStage = stage;
    }
  }

  private static class StageClosedAction implements EventAction
  {
    public static StageClosedAction instance = new StageClosedAction();
    public void invoke(Event event)
    {
      final Stage stage = (Stage) event.getSubject();
      stage.getTheater().remove(stage);
    }
  }

  private static class DeactivateStageAction implements EventAction
  {
    public static DeactivateStageAction instance = new DeactivateStageAction();
    public void invoke(Event event)
    {
      final Stage stage = (Stage) event.getSubject();
      final Theater theater = stage.getTheater();
      if(theater.activeStage == stage)
        theater.activeStage = null;
      theater.checkForEmptyTheater();
    }
  }
}

