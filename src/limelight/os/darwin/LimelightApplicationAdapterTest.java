//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import junit.framework.TestCase;
import limelight.Context;
import limelight.MockContext;

public class LimelightApplicationAdapterTest extends TestCase
{
  private LimelightApplicationAdapter adapter;

  public void setUp() throws Exception
  {
    adapter = new LimelightApplicationAdapter();
  }

  public void testHandleQuit() throws Exception
  {
    MockContext context = MockContext.stub();

    adapter.handleQuit(null);

    assertEquals(true, context.shutdownAttempted);
  }
}
