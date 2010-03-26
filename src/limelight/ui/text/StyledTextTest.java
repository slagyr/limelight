//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.
package limelight.ui.text;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.styles.StyleDescriptor;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleAttribute;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StyledTextTest extends Assert
{
  private StyledText styledText;
  private Map<String, Style> styles;
  private Style style1;
  private Style style2;
  private RichStyle defaultStyle;

  @Test
  public void shouldHaveBasicGetters() throws Exception
  {
    LinkedList<String> styles = new LinkedList<String>();
    styledText = new StyledText("Hello there", styles);
    assertEquals("Hello there", styledText.getText());
    assertEquals(styles, styledText.getStyles());
  }

  @Test
  public void shouldBuildStyles() throws Exception
  {
    makeSampleStyles();
    StyleObserver observer = new MockStyleObserver();

    styledText = new StyledText("Some Text", "fizz", "bang");
    styledText.setupStyles(styles, defaultStyle, observer);

    RichStyle style = styledText.getStyle();
    assertSame(style1, style.getExtention(0));
    assertSame(style2, style.getExtention(1));
    assertSame(defaultStyle, style.getExtention(2));
    assertEquals(true, style.hasObserver(observer));
  }

  private void makeSampleStyles()
  {
    styles = new HashMap<String, Style>();
    style1 = new RichStyle();
    style2 = new RichStyle();
    styles.put("fizz", style1);
    styles.put("bang", style2);
    defaultStyle = new RichStyle();
  }
  
  @Test
  public void shouldTeardownStyles() throws Exception
  {
    makeSampleStyles();
    styledText = new StyledText("Some Text", "fizz", "bang");
    styledText.setupStyles(styles, defaultStyle, new MockStyleObserver());

    styledText.teardownStyles();
    
    RichStyle style = styledText.getStyle();
    assertEquals(false, style1.hasObserver(style));
    assertEquals(false, style2.hasObserver(style));
    assertEquals(false, defaultStyle.hasObserver(style));
  }

  @Test
  public void shouldBuildFont() throws Exception
  {
    styledText = new StyledText("Blah");
    styledText.setupStyles(new HashMap<String, Style>(), new RichStyle(), new MockStyleObserver());
    styledText.getStyle().setFontFace("Courier");
    styledText.getStyle().setFontStyle("plain");
    styledText.getStyle().setFontSize(12);
    
    Font font = styledText.getFont();

    assertEquals("Courier", font.getFontName());
    assertEquals(12, font.getSize());
    assertEquals(true, font.isPlain());
  }

  private class MockStyleObserver implements StyleObserver
  {
    public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
    {
    }
  }
}
