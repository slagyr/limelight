package limelight.util;

import limelight.LimelightException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class OptionsTest
{
  public static class OptionsSubject
  {
    private boolean b;
    private int i;
    private double d;
    private String s;

    public void setB(boolean b)
    {
      this.b = b;
    }

    public void setI(int i)
    {
      this.i = i;
    }

    public void setD(double d)
    {
      this.d = d;
    }

    public void setS(String s)
    {
      this.s = s;
    }
  }

  private OptionsSubject subject;

  @Before
  public void setUp() throws Exception
  {
    subject = new OptionsSubject();
  }

  @Test
  public void canUseSetters() throws Exception
  {
    Options.apply(subject, Util.toMap("b", true, "i", 42, "d", 3.14, "s", "blah"));

    assertEquals(true, subject.b);
    assertEquals(42, subject.i);
    assertEquals(3.14, subject.d, 0.01);
    assertEquals("blah", subject.s);
  }

  @Test
  public void doesntCrashWhenOptionsCantBeMapped() throws Exception
  {
    final Map<String, Object> options = Util.toMap("foo", "bar");
    try
    {
      Options.apply(subject, options);
      assertEquals("bar", options.get("foo"));
    }
    catch(Exception e)
    {
      fail("Should not throw exception");
    }
  }

  @Test
  public void usedValuesAreRemovedFromTheMap() throws Exception
  {
    final Map<String, Object> options = Util.toMap("b", true, "i", 42, "d", 3.14, "s", "blah");
    Options.apply(subject, options);

    assertEquals(0, options.size());
  }

  @Test
  public void incompatibleValuesDoesntCauseCrash() throws Exception
  {
    final Map<String, Object> options = Util.toMap("b", 1);

    Options.apply(subject, options);

    assertEquals(1, options.size());
  }

}
