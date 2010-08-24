package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class KeyEventTest
{
  private Panel panel;
  private KeyEvent event;

  private static class TestableKeyEvent extends KeyEvent
  {
    public TestableKeyEvent(Panel panel, int modifiers, int keyCode, int location)
    {
      super(panel, modifiers, keyCode, location);
    }
  }

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new TestableKeyEvent(panel, 123, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT);
  }

  @Test
  public void canGetKeyCodeAndLocation() throws Exception
  {
    assertEquals(KeyEvent.KEY_ENTER, event.getKeyCode());
    assertEquals(KeyEvent.LOCATION_LEFT, event.getKeyLocation());
  }
}
