package limelight.ui.model.text.masking;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordMaskTest
{
  @Test
  public void hasInstance() throws Exception
  {
    assertNotNull(PasswordMask.instance);
    assertEquals(PasswordMask.class, PasswordMask.instance.getClass());
  }

  @Test
  public void masksToDots() throws Exception
  {
    assertEquals("", PasswordMask.instance.mask(""));
    assertEquals("\u2022", PasswordMask.instance.mask("a"));
    assertEquals("\u2022\u2022\u2022", PasswordMask.instance.mask("abc"));
  }
}
