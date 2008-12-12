package limelight.ui.model;

import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: dennyabraham
 * Date: Dec 10, 2008
 * Time: 4:57:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class StyledTextParser {

    private static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)>(.*)</(\\1)>");

    public List<StyledText> parse(String text)
    {
        List<StyledText> styledTextList = new LinkedList<StyledText>();

        Matcher matcher = TAG_REGEX.matcher(text);

        int index = 0;
        while(matcher.find())
        {
            String prefix = text.substring(index, matcher.start());
            String styleName = matcher.group(1);
            String styledText = matcher.group(2);

            if(prefix.length() > 0)
                styledTextList.add(new StyledText(prefix));

            Matcher innerMatcher = TAG_REGEX.matcher(styledText);
            if(innerMatcher.find())
            {
                StyledTextParser innerParser = new StyledTextParser();
                List<StyledText> innerMatches = innerParser.parse(styledText);
                
                Integer last = innerMatches.size() - 1;
                StyledText firstInnerMatch = innerMatches.get(0);
                StyledText lastInnerMatch = innerMatches.get(last);
                firstInnerMatch.setStyleName(styleName);
                lastInnerMatch.setStyleName(styleName);

                styledTextList.addAll(innerMatches);
                index = matcher.end();
            } else {
                index = matcher.end();
                styledTextList.add(new StyledText(styledText, styleName));
            }
        }
        String trailingText = text.substring(index);
        if(trailingText.length() > 0)
            styledTextList.add(new StyledText(trailingText));

        return styledTextList;
    }
}
