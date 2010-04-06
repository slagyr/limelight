//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.win32;

import junit.framework.TestCase;
import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.util.StringUtil;
import limelight.os.MockRuntimeExecution;

public class Win32OSTest extends TestCase
{
  private Win32OS os;

  public void setUp() throws Exception
  {
    os = new Win32OS();
  }
  
  public void testDataRootDir() throws Exception
  {
    assertEquals(System.getProperty("user.home") + "/Application Data/Limelight", os.dataRoot());
  }

  public void testConfigureSystemProperties() throws Exception
  {
    System.setProperty("jruby.shell", "blah");
    System.setProperty("jruby.script", "blah");

    os.configureSystemProperties();

    assertEquals("cmd.exe", System.getProperty("jruby.shell"));
    assertEquals("jruby.bat org.jruby.Main", System.getProperty("jruby.script"));
  }

  public void testOpenProduction() throws Exception
  {
    MockStudio studio = new MockStudio();
    Context.instance().studio = studio;

    os.openProduction("blah");

    assertEquals("blah", studio.openedProduction);
  }

  public void testOpenURL() throws Exception
  {
    MockRuntimeExecution mockSystemExecution = new MockRuntimeExecution();
    os.setRuntime(mockSystemExecution);

    os.launch("http://www.google.com");

    assertEquals("cmd.exe /C start http://www.google.com", StringUtil.join(" ", mockSystemExecution.command));
  }
}
