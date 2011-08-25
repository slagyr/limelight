//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

import java.awt.*;

public class SimpleFontStyleValueTest extends TestCase
{
  private SimpleFontStyleValue plain;
  private SimpleFontStyleValue bold;
  private SimpleFontStyleValue italic;
  private SimpleFontStyleValue boldItalic ;

  public void setUp() throws Exception
  {
    plain = new SimpleFontStyleValue("plain");
    bold = new SimpleFontStyleValue("bold");
    italic = new SimpleFontStyleValue("italic");
    boldItalic = new SimpleFontStyleValue("bold italic");
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (plain instanceof StyleValue));

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
    assertEquals(true, bold.equals(new SimpleFontStyleValue("bold")));
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
