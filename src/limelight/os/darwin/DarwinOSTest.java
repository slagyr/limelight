//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import junit.framework.TestCase;
import limelight.Context;
import limelight.util.StringUtil;
import limelight.os.MockRuntimeExecution;
import limelight.ui.api.MockStudio;

public class DarwinOSTest extends TestCase
{
  private DarwinOS os;

  public void setUp() throws Exception
  {
    os = new DarwinOS();
  }

  public void testDataRootDir() throws Exception
  {
    assertEquals(System.getProperty("user.home") + "/Library/Application Support/Limelight", os.dataRoot());
  }

  public void testConfigureSystemProperties() throws Exception
  {
    System.setProperty("jruby.shell", "blah");
    System.setProperty("jruby.script", "blah");

    os.configureSystemProperties();

    assertEquals("/bin/sh", System.getProperty("jruby.shell"));
    assertEquals("jruby", System.getProperty("jruby.script"));
  }

  public void testAppIsStartingWhenRunningAsApp() throws Exception
  {
    System.setProperty("limelight.as.app", "true");

    os.appIsStarting();

    assertEquals(true, os.isRunningAsApp());
  }

  public void testAppIsStartingWhenNotRunningAsApp() throws Exception
  {
    System.setProperty("limelight.as.app", "false");

    os.appIsStarting();

    assertEquals(false, os.isRunningAsApp());
  }

  public void testOpenProductionWhenNotRunningAsApp() throws Exception
  {
    LimelightApplicationAdapter.startupsReceived = 0;
    System.setProperty("limelight.as.app", "false");
    MockStudio studio = new MockStudio();
    Context.instance().studio = studio;

    os.openProduction("blah");

    assertEquals("blah", studio.openedProduction);
    assertEquals(0, LimelightApplicationAdapter.startupsReceived);
  }

  public void testOpenProductionWhenRunningAsApp() throws Exception
  {
    LimelightApplicationAdapter.startupsReceived = 0;
    System.setProperty("limelight.as.app", "true");
    MockStudio studio = new MockStudio();
    Context.instance().studio = studio;

    os.openProduction("blah");

    assertEquals("blah", studio.openedProduction);
    assertEquals(1, LimelightApplicationAdapter.startupsReceived);
  }

  public void testRegistersApplicationAdapter() throws Exception
  {
    assertNotNull(os.getApplicationAdapter());
    assertEquals(true, os.getApplicationAdapter().isRegistered());
  }

  public void testOpenURL() throws Exception
  {
    MockRuntimeExecution mockSystemExecution = new MockRuntimeExecution();
    os.setRuntime(mockSystemExecution);

    os.launch("http://www.google.com");

    assertEquals("open http://www.google.com", StringUtil.join(" ", mockSystemExecution.command));
  }

}
