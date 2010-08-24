package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class EventTest
{
  private Event event;
  private MockPanel panel;

  private static class TestableEvent extends Event
  {
    public TestableEvent(Panel source)
    {
      super(source);
    }
  }

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new TestableEvent(panel);
  }
  
  @Test
  public void rememberThePanel() throws Exception
  {
    assertSame(panel, event.getSource());
  }
  
  @Test
  public void isConsumable() throws Exception
  {
    assertEquals(false, event.isConsumed());

    event.consume();

    assertEquals(true, event.isConsumed());
  }
  
  @Test
  public void isNotInheritableByDefault() throws Exception
  {
    assertEquals(false, event.isInheritable());
  }

}
