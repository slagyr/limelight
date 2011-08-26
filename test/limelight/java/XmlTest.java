//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.LimelightException;
import limelight.io.FakeFileSystem;
import limelight.styles.RichStyle;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class XmlTest
{
  private FakeFileSystem fs;

  @Before
  public void setUp() throws Exception
  {
    fs = FakeFileSystem.installed();
  }

  @Test
  public void loadStylesWithAnExtension() throws Exception
  {
    fs.createTextFile("/styles.xml", "<styles><one width='100'/><two extends='one' height='200'/></styles>");
    final HashMap<String, RichStyle> styles = new HashMap<String, RichStyle>();
    Xml.toStyles("/styles.xml", styles, new HashMap<String, RichStyle>());

    assertEquals(2, styles.size());
    final RichStyle one = styles.get("one");
    final RichStyle two = styles.get("two");
    assertEquals("100", two.getWidth());
    assertEquals("200", two.getHeight());
    assertEquals(true, two.hasExtension(one));
  }

  private void checkMultipleExtensions()
  {
    final HashMap<String, RichStyle> styles = new HashMap<String, RichStyle>();
    Xml.toStyles("/styles.xml", styles, new HashMap<String, RichStyle>());

    assertEquals(3, styles.size());
    final RichStyle one = styles.get("one");
    final RichStyle two = styles.get("two");
    final RichStyle three = styles.get("three");
    assertEquals("100", three.getWidth());
    assertEquals("200", three.getHeight());
    assertEquals(true, three.hasExtension(one));
    assertEquals(true, three.hasExtension(two));
  }

  @Test
  public void loadStylesWithMultipleExtensionsSeparatedWithSpace() throws Exception
  {
    fs.createTextFile("/styles.xml", "<styles><one width='100'/><two height='200'/><three extends='one two'/></styles>");
    checkMultipleExtensions();
  }

  @Test
  public void loadStylesWithMultipleExtensionsSeparatedWithComma() throws Exception
  {
    fs.createTextFile("/styles.xml", "<styles><one width='100'/><two height='200'/><three extends='one,two'/></styles>");
    checkMultipleExtensions();
  }

  @Test
  public void loadStyleWithMissingExtension() throws Exception
  {
    fs.createTextFile("/styles.xml", "<styles><two extends='missing' height='200'/></styles>");

    try
    {
      Xml.toStyles("/styles.xml", new HashMap<String, RichStyle>(), new HashMap<String, RichStyle>());
      fail("should have thrown exception");
    }
    catch(LimelightException e)
    {
      assertEquals("Can't extend missing style: 'missing'", e.getMessage());
    }
  }

  @Test
  public void loadStyleWithExtensionFromExtendableMap() throws Exception
  {
    fs.createTextFile("/extensions.xml", "<styles><one width='100'/></styles>");
    final Map<String, RichStyle> extensions = Xml.toStyles("/extensions.xml", new HashMap<String, RichStyle>(), new HashMap<String, RichStyle>());
    fs.createTextFile("/styles.xml", "<styles><two extends='one' height='200'/></styles>");
    final Map<String, RichStyle> styles = Xml.toStyles("/styles.xml", new HashMap<String, RichStyle>(), extensions);

    final RichStyle one = extensions.get("one");
    final RichStyle two = styles.get("two");
    assertEquals("100", two.getWidth());
    assertEquals("200", two.getHeight());
    assertEquals(true, two.hasExtension(one));

  }
}
