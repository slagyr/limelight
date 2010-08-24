package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CharTypedEventTest
{
  private Panel panel;
  private CharTypedEvent event;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new CharTypedEvent(panel, 123, 'a');
  }
  
  @Test
  public void canGetAttributes() throws Exception
  {
    assertEquals(123, event.getModifiers());
    assertEquals('a', event.getChar());
  }
}
