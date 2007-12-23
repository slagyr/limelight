package limelight;

import junit.framework.TestCase;


public class StyleTest extends TestCase
{
  private FlatStyle style;

  public void setUp() throws Exception
  {
    style = new FlatStyle();
  }
  
  public void testEmptyChecksum() throws Exception
  {
    assertEquals(0, style.checksum());
  }

  public void testChecksumChangesWhenStylesChange() throws Exception
  {
    style.setWidth("123");
    int checksum = style.checksum();

    assertTrue(checksum != 0);

    style.setHeight("321");
    int newChecksum = style.checksum();

    assertTrue(newChecksum != checksum);
  }

  public void testChecksumIsDifferentWithSwappedValues() throws Exception
  {
    style.setWidth("123");
    style.setHeight("321");
    int checksum = style.checksum();

    style.setWidth("321");
    style.setHeight("123");
    assertTrue(style.checksum() != checksum);
  }
  
  public void testChecksumsForTwoStylesWithSameValues() throws Exception
  {
    style.setWidth("123");
    style.setHeight("321");

    FlatStyle other = new FlatStyle();
    other.setWidth("123");
    other.setHeight("321");

    assertEquals(style.checksum(), other.checksum());
  }

  public void testChecksumDoesntIncludeTransparency() throws Exception
  {
    style.setWidth("123");
    style.setHeight("321");
    int checksum = style.checksum();

    style.setTransparency("50");
    assertEquals(checksum, style.checksum());

    style.setTransparency("100");
    assertEquals(checksum, style.checksum());
  }
}
