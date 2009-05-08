package limelight.os.win32;

import junit.framework.TestCase;
import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.os.darwin.StartupListener;

public class Win32OSTest extends TestCase
{
  private Win32OS os;

  public void setUp() throws Exception
  {
    os = new Win32OS();
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
}
