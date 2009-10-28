//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import org.jruby.Ruby;

public class RuntimeFactoryTest extends TestCase
{
  private RuntimeFactory runtimes;
  private String rubysrc = "" +
      "begin\n" +
      " class Production; def name; return 'blah'; end; end;" +
      " Java::java.lang.Thread.currentThread.handle = Production.new\n" +
//      " sleep(5)\n" +
      "rescue Exception => e\n" +
      " puts e\n" +
      "end";

  public void setUp() throws Exception
  {
    MockContext.stub();
    runtimes = new RuntimeFactory();
  }

  public void testSpawningNewRuntime() throws Exception
  {
    RuntimeFactory.BirthCertificate result = runtimes.spawn(rubysrc);

    assertEquals("blah", result.production.getName());
    Ruby ruby = runtimes.get(result);
    assertNotNull(ruby);
  }

  public void testTerminateRuntime() throws Exception
  {
    RuntimeFactory.BirthCertificate handle = runtimes.spawn(rubysrc);

    runtimes.terminate(handle);

    assertEquals(null, runtimes.get(handle));
  }

  public void testShouldRaiseRubyErrors() throws Exception
  {
    try
    {
      runtimes.spawn("raise 'blah'");
      fail("should throw exception");
    }
    catch(Exception e)
    {
//      e.printStackTrace();
      assertEquals(e.toString(), true, e.toString().contains("blah"));
    }
  }
}
