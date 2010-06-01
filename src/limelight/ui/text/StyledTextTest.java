//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.styles.RichStyle;
import limelight.styles.StyleAttribute;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleValue;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StyledTextTest extends Assert
{
  private StyledText styledText;
  private Map<String, RichStyle> styles;
  private RichStyle style1;
  private RichStyle style2;
  private RichStyle defaultStyle;

  @Test
  public void shouldHaveBasicGetters() throws Exception
  {
    LinkedList<String> styles = new LinkedList<String>();
    styledText = new StyledText("Hello there", styles);
    assertEquals("Hello there", styledText.getText());
    assertEquals(styles, styledText.getStyleNames());
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
    styles = new HashMap<String, RichStyle>();
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
  public void shouldNotBuildStylesMoreThanOnce() throws Exception
  {
    makeSampleStyles();
    StyleObserver observer = new MockStyleObserver();

    styledText = new StyledText("Some Text", "fizz", "bang");
    styledText.setupStyles(styles, defaultStyle, observer);
    styledText.setupStyles(styles, defaultStyle, observer);

    RichStyle style = styledText.getStyle();
    assertEquals(3, style.getExtentions().size());
    assertEquals(1, style.getObservers().size());
  }

  @Test
  public void shouldBuildFont() throws Exception
  {
    styledText = new StyledText("Blah");
    styledText.setupStyles(new HashMap<String, RichStyle>(), new RichStyle(), new MockStyleObserver());
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
    public void styleChanged(StyleAttribute attribute, StyleValue value)
    {
    }
  }
}
