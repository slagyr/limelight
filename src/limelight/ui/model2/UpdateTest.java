package limelight.ui.model2;

import junit.framework.TestCase;
import limelight.ui.Panel;

public class UpdateTest extends TestCase
{
  public void testSeverity() throws Exception
  {
    Update update = new TestableUpdate(6);

    assertEquals(6, update.getSeverity());
  }
  
  public void testMoreSevereThan() throws Exception
  {
    Update update1 = new TestableUpdate(1);
    Update update2 = new TestableUpdate(2);

    assertEquals(true, update2.isMoreSevereThan(update1));
    assertEquals(false, update1.isMoreSevereThan(update2));
  }

  private class TestableUpdate extends Update
  {
    protected TestableUpdate(int severity)
    {
      super(severity);
    }

    public void performUpdate(Panel panel)
    {
    }
  }
}
