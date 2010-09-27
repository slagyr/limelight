package limelight.ui.events.panel;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValueChangedEventTest
{
  @Test
  public void isNotInheritable() throws Exception
  {
    assertEquals(false, new ValueChangedEvent().isInheritable());
  }


}
