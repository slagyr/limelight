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
    Map<String, Style> styles = new HashMap<String, Style>();
    Style style1 = new RichStyle();
    Style style2 = new RichStyle();
    RichStyle defaultStyle = new RichStyle();
    styles.put("fizz", style1);
    styles.put("bang", style2);
    StyleObserver observer = new MockStyleObserver();

    styledText = new StyledText("Some Text", "fizz", "bang");
    styledText.applyStyles(styles, defaultStyle, observer);

    RichStyle style = styledText.getStyle();
    assertSame(style1, style.getExtentions().get(0));
    assertSame(style2, style.getExtentions().get(1));
    assertSame(defaultStyle, style.getExtentions().get(2));
    assertEquals(true, style.hasObserver(observer));
  }

  @Test
  public void shouldBuildFont() throws Exception
  {
    styledText = new StyledText("Blah");
    styledText.applyStyles(new HashMap<String, Style>(), new RichStyle(), new MockStyleObserver());
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
