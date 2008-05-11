package limelight;

import junit.framework.TestCase;
import limelight.io.TempDirectory;

public class ContextTest extends TestCase
{
  public void testTempDirectory() throws Exception
  {
    TempDirectory directory = new TempDirectory();
    Context.instance().tempDirectory = directory;
    assertEquals(directory, Context.instance().tempDirectory);
  }
}
