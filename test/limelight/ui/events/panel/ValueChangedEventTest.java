//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
