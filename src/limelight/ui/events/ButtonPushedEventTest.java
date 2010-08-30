package limelight.ui.events;

import limelight.ui.MockPanel;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ButtonPushedEventTest
{
  @Test
  public void isNotInheritable() throws Exception
  {
    final ButtonPushedEvent event = new ButtonPushedEvent(new MockPanel());
    assertEquals(false, event.isInheritable());
  }
}
