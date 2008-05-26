//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;

public class MainTest extends TestCase
{
  private Main main;

  public void setUp() throws Exception
  {
    main = new Main();
    Context.removeInstance();
  }
  
  public void testTempFileIsAddedToContext() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().tempDirectory);
  }
}
