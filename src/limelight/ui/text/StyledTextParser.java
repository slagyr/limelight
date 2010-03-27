package limelight.ui.text;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyledTextParser
{

  public static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)>(.*?)</(\\1)>", Pattern.DOTALL);

  public LinkedList<StyledText> parse(String text)
  {
    return parse(text, new LinkedList<String>(), new LinkedList<StyledText>());
  }

  private LinkedList<StyledText> parse(String text, LinkedList<String> styles, LinkedList<StyledText> list)
  {
    Matcher matcher = TAG_REGEX.matcher(text);

    int index = 0;
    while(matcher.find())
    {
      String prefix = escape(text.substring(index, matcher.start()));
      String styleName = matcher.group(1);
      String styledText = matcher.group(2);

      if(prefix.length() > 0)
        list.add(new StyledText(prefix, styles));

      LinkedList<String> newStyles = new LinkedList<String>(styles);
      newStyles.addFirst(styleName);
      parse(styledText, newStyles, list);

      index = matcher.end();
    }

    String trailingText = escape(text.substring(index));
    if(trailingText.length() > 0)
      list.add(new StyledText(trailingText, styles));

    return list;
  }

  private String escape(String text)
  {
    return text.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
  }
}
