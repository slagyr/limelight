package limelight.os.win32;

import junit.framework.TestCase;
import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.os.darwin.StartupListener;
import limelight.os.IRuntime;

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
    MockRuntime mockRuntime = new MockRuntime();
    os.setRuntime(mockRuntime);

    os.openURL("http://www.google.com");

    String[] cmd = new String[4];
	  cmd[0] = "cmd.exe";
	  cmd[1] = "/C";
	  cmd[2] = "start";
	  cmd[3] = "http://www.google.com";

    assertTrue(mockRuntime.receivedExecWith(cmd));
  }
}
