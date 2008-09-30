package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

import java.awt.*;

public class SimpleFontStyleAttributeTest extends TestCase
{
  private SimpleFontStyleAttribute plain;
  private SimpleFontStyleAttribute bold;
  private SimpleFontStyleAttribute italic;
  private SimpleFontStyleAttribute boldItalic ;

  public void setUp() throws Exception
  {
    plain = new SimpleFontStyleAttribute("plain");
    bold = new SimpleFontStyleAttribute("bold");
    italic = new SimpleFontStyleAttribute("italic");
    boldItalic = new SimpleFontStyleAttribute("bold italic");
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
    assertEquals(true, bold.equals(new SimpleFontStyleAttribute("bold")));
    assertEquals(false, bold.equals(plain));
    assertEquals(false, bold.equals(null));
  }
  
  public void testStyleAsInt() throws Exception
  {
    assertEquals(Font.BOLD | Font.ITALIC, boldItalic.toInt());
    assertEquals(Font.BOLD, bold.toInt());
    assertEquals(Font.ITALIC, italic.toInt());
    assertEquals(0, plain.toInt());
  }
}
