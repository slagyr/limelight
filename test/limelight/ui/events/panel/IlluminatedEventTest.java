package limelight.ui.events.panel;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IlluminatedEventTest
{
  private Panel panel;
  private IlluminatedEvent event;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new IlluminatedEvent(panel);
  }

  @Test
  public void panel() throws Exception
  {
    assertEquals(panel, event.getRecipient());
  }
}
