package limelight.ui.events.panel;

import limelight.ui.MockPanel;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ButtonPushedEventTest
{
  @Test
  public void isNotInheritable() throws Exception
  {
    final ButtonPushedEvent event = new ButtonPushedEvent();
    assertEquals(false, event.isInheritable());
  }
}
