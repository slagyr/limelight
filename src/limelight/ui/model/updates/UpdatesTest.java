//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.updates;

import junit.framework.TestCase;

public class UpdatesTest extends TestCase
{
  public void testStaticUpdates() throws Exception
  {
    assertEquals(LayoutAndPaintUpdate.class, Updates.layoutAndPaintUpdate.getClass());
    assertEquals(ScrollChangedUpdate.class, Updates.scrollChangedUpdate.getClass());
    assertEquals(PaintUpdate.class, Updates.paintUpdate.getClass());
    assertEquals(ShallowPaintUpdate.class, Updates.shallowPaintUpdate.getClass());
  }

  public void testSeverities() throws Exception
  {
    assertEquals(true, Updates.layoutAndPaintUpdate.isMoreSevereThan(Updates.scrollChangedUpdate));
    assertEquals(true, Updates.scrollChangedUpdate.isMoreSevereThan(Updates.paintUpdate));
    assertEquals(true, Updates.paintUpdate.isMoreSevereThan(Updates.shallowPaintUpdate));
  }
}
