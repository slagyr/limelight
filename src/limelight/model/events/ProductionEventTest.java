package limelight.model.events;

import limelight.model.FakeProduction;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ProductionEventTest
{
  private FakeProduction production;
  private TestableProductionEvent event;
  private MockEventAction action;

  public static class TestableProductionEvent extends ProductionEvent
  {
  }

  @Before
  public void setUp() throws Exception
  {
    production = new FakeProduction();
    event = new TestableProductionEvent();
    action = new MockEventAction();
  }
  
  @Test
  public void dispatching() throws Exception
  {
    production.getEventHandler().add(TestableProductionEvent.class, action);

    event.dispatch(production);

    assertEquals(true, action.invoked);
    assertEquals(production, event.getSubject());
    assertEquals(production, event.getProduction());
  }
}
