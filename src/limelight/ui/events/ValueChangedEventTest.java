package limelight.ui.events;

import limelight.ui.MockPanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValueChangedEventTest
{
  private MockPanel panel;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
  }

  @Test
  public void isNotInheritable() throws Exception
  {
    assertEquals(false, new ValueChangedEvent(panel).isInheritable());
  }


}
