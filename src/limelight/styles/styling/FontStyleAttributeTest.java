package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class FontStyleAttributeTest extends TestCase
{
  private FontStyleAttribute plain;
  private FontStyleAttribute bold;
  private FontStyleAttribute italic;
  private FontStyleAttribute boldItalic ;

  public void setUp() throws Exception
  {
    plain = new FontStyleAttribute("plain");
    bold = new FontStyleAttribute("bold");
    italic = new FontStyleAttribute("italic");
    boldItalic = new FontStyleAttribute("bold italic");
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (plain instanceof StyleAttribute));

    assertEquals(false, plain.isBold());
    assertEquals(false, plain.isItalic());

    assertEquals(true, bold.isBold());
    assertEquals(false, bold.isItalic());

    assertEquals(false, italic.isBold());
    assertEquals(true, italic.isItalic());

    assertEquals(true, boldItalic.isBold());
    assertEquals(true, boldItalic.isItalic());
  }

  public void testToString() throws Exception
  {
    assertEquals("plain", plain.toString());
    assertEquals("bold", bold.toString());
    assertEquals("italic", italic.toString());
    assertEquals("bold italic", boldItalic.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, bold.equals(bold));
    assertEquals(true, bold.equals(new FontStyleAttribute("bold")));
    assertEquals(false, bold.equals(plain));
    assertEquals(false, bold.equals(null));
  }

}
