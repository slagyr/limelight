package limelight.ui;

import limelight.ui.events.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class EventActionMulticasterTest
{
  private RecordingAction action1;
  private RecordingAction action2;
  private ArrayList<EventAction> invokations = new ArrayList<EventAction>();
  private RecordingAction action3;
  private RecordingAction action4;
  private RecordingAction action5;

  private class RecordingAction implements EventAction
  {
    private int id;

    public RecordingAction(int id)
    {
      this.id = id;
    }

    public void invoke(Event event)
    {
      invokations.add(this);
    }

    @Override
    public String toString()
    {
      return "RecodingAction-" + id;
    }
  }

  @Before
  public void setUp() throws Exception
  {
    action1 = new RecordingAction(1);
    action2 = new RecordingAction(2);
    action3 = new RecordingAction(3);
    action4 = new RecordingAction(4);
    action5 = new RecordingAction(5);
  }

  @Test
  public void addingToNullEvent() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, null);
    assertSame(action1, action);
  }

  @Test
  public void addingActionToNull() throws Exception
  {
    EventAction action = EventActionMulticaster.add(null, action1);
    assertSame(action1, action);
  }

  @Test
  public void addingTwoActions() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);

    assertEquals(EventActionMulticaster.class, action.getClass());
    EventActionMulticaster multicaster = (EventActionMulticaster) action;
    assertEquals(action1, multicaster.getFirst());
    assertEquals(action2, multicaster.getSecond());
  }

  @Test
  public void actionsAreInvokedInOrder() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);

    action.invoke(null);

    assertSame(action1, invokations.get(0));
    assertSame(action2, invokations.get(1));
  }

  @Test
  public void chainingMulticasters() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    action = EventActionMulticaster.add(action, action3);
    action = EventActionMulticaster.add(action, action4);
    action = EventActionMulticaster.add(action, action5);

    action.invoke(null);

    assertEquals(action1, invokations.get(0));
    assertEquals(action2, invokations.get(1));
    assertEquals(action3, invokations.get(2));
    assertEquals(action4, invokations.get(3));
    assertEquals(action5, invokations.get(4));
  }

  @Test
  public void removingOnlyAction() throws Exception
  {
    EventAction action = EventActionMulticaster.remove(action1, action1);

    assertEquals(null, action);
  }

  @Test
  public void removingNull() throws Exception
  {
    EventAction action = EventActionMulticaster.remove(action1, null);

    assertSame(action1, action);
  }

  @Test
  public void removingFromNull() throws Exception
  {
    EventAction action = EventActionMulticaster.remove(null, action1);

    assertEquals(null, action);
  }

  @Test
  public void removingFirstOfTwo() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    EventAction result = EventActionMulticaster.remove(action, action1);

    assertSame(result, action2);
  }

  @Test
  public void removingSecondOfTwo() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    EventAction result = EventActionMulticaster.remove(action, action2);

    assertSame(result, action1);
  }

  @Test
  public void removingNeitherOfTwo() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    EventAction result = EventActionMulticaster.remove(action, action3);

    assertSame(action, result);
  }

  @Test
  public void removingFirstOfThree() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    action = EventActionMulticaster.add(action, action3);

    EventAction result = EventActionMulticaster.remove(action, action1);

    assertEquals(EventActionMulticaster.class, result.getClass());
    EventActionMulticaster multicaster = (EventActionMulticaster) result;
    assertSame(action2, multicaster.getFirst());
    assertSame(action3, multicaster.getSecond());
  }

  @Test
  public void removingMiddleOfThree() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    action = EventActionMulticaster.add(action, action3);

    EventAction result = EventActionMulticaster.remove(action, action2);

    assertEquals(EventActionMulticaster.class, result.getClass());
    EventActionMulticaster multicaster = (EventActionMulticaster) result;
    assertSame(action1, multicaster.getFirst());
    assertSame(action3, multicaster.getSecond());
  }

  @Test
  public void removingLastOfThree() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    action = EventActionMulticaster.add(action, action3);

    EventAction result = EventActionMulticaster.remove(action, action3);

    assertEquals(EventActionMulticaster.class, result.getClass());
    EventActionMulticaster multicaster = (EventActionMulticaster) result;
    assertSame(action1, multicaster.getFirst());
    assertSame(action2, multicaster.getSecond());
  }

  @Test
  public void collectingFromNull() throws Exception
  {
    List<EventAction> actions = EventActionMulticaster.collect(null);

    assertEquals(0, actions.size());
  }

  @Test
  public void collectingOneAction() throws Exception
  {
    List<EventAction> actions = EventActionMulticaster.collect(action1);

    assertEquals(1, actions.size());
    assertSame(action1, actions.get(0));
  }

  @Test
  public void collectingTwoActions() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    List<EventAction> actions = EventActionMulticaster.collect(action);

    assertEquals(2, actions.size());
    assertSame(action1, actions.get(0));
    assertSame(action2, actions.get(1));
  }

  @Test
  public void collectingLotsOfActions() throws Exception
  {
    EventAction action = EventActionMulticaster.add(action1, action2);
    action = EventActionMulticaster.add(action, action3);
    action = EventActionMulticaster.add(action, action4);
    action = EventActionMulticaster.add(action, action5);

    List<EventAction> actions = EventActionMulticaster.collect(action);

    assertEquals(5, actions.size());
    assertSame(action1, actions.get(0));
    assertSame(action2, actions.get(1));
    assertSame(action3, actions.get(2));
    assertSame(action4, actions.get(3));
    assertSame(action5, actions.get(4));
  }
}


