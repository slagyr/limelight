package limelight.styles;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class StylesTest
{
  private HashMap<String, RichStyle> oldStyles;
  private HashMap<String,RichStyle> newStyles;
  private HashMap<String, RichStyle> mergedStyles;

  @Before
  public void setUp() throws Exception
  {
    oldStyles = new HashMap<String, RichStyle>();
    newStyles = new HashMap<String, RichStyle>();
  }

  @Test
  public void simpleMerging() throws Exception
  {
    oldStyles.put("oldOne", new RichStyle());
    newStyles.put("newOne", new RichStyle());

    mergedStyles = Styles.merge(newStyles, oldStyles);

    assertEquals(true, mergedStyles.containsKey("oldOne"));
    assertEquals(true, mergedStyles.containsKey("newOne"));
  }

  @Test
  public void stylesWithSameNameGetExtended() throws Exception
  {
    final RichStyle style1 = new RichStyle();
    final RichStyle style2 = new RichStyle();
    oldStyles.put("sameName", style1);
    newStyles.put("sameName", style2);

    mergedStyles = Styles.merge(newStyles, oldStyles);

    assertSame(style2, mergedStyles.get("sameName"));
    assertEquals(true, style2.hasExtension(style1));
  }

  @Test
  public void stylesWithSameNameDontDuplicateExtensions() throws Exception
  {
    final RichStyle style1 = new RichStyle();
    final RichStyle style2 = new RichStyle();
    style2.addExtension(style1);
    oldStyles.put("sameName", style1);
    newStyles.put("sameName", style2);

    mergedStyles = Styles.merge(newStyles, oldStyles);

    assertSame(style2, mergedStyles.get("sameName"));
    assertEquals(true, style2.hasExtension(style1));
    assertEquals(1, style2.getExtentions().size());
  }
}
