package limelight.ui.model;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: dennyabraham
 * Date: Dec 10, 2008
 * Time: 4:18:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StyledTextTest extends TestCase
{
    private StyledText styledText;
    private StyledText styledText1;
    private StyledText styledText2;

    public void testHasText() throws Exception
    {
        styledText = new StyledText("Hello there");
        assertEquals(styledText.getText(), "Hello there");
    }

    public void testHasOtherText() throws Exception
    {
        styledText = new StyledText("Goodbye there");
        assertEquals(styledText.getText(), "Goodbye there");
    }

    public void testHasDefaultStyle() throws Exception
    {
        styledText = new StyledText("Hello there");
        assertEquals(styledText.getStyle(), "default");
    }

    public void testHasStyle() throws Exception
    {
        styledText = new StyledText("Some text", "Some_style");
        assertEquals(styledText.getStyle(), "Some_style");
    }

    public void testHasOtherStyle() throws Exception
    {
        styledText = new StyledText("Some text", "Other_style");
        assertEquals(styledText.getStyle(), "Other_style");
    }

    public void testHasUsefulEquals() throws Exception
    {
        styledText1 = new StyledText("Hello");
        styledText2 = new StyledText("Hello");

        assertTrue(styledText1.equals(styledText2));
    }

    public void testIfDifferentTextThenNotEquals() throws Exception
    {
        styledText1 = new StyledText("hi");
        styledText2 = new StyledText("bye");

        assertFalse(styledText1.equals(styledText2));
    }


    public void testIfDifferentStyleThenNotEquals() throws Exception
    {
        styledText1 = new StyledText("hi", "a");
        styledText2 = new StyledText("hi", "b");

        assertFalse(styledText1.equals(styledText2));
    }

    public void testEqualsWithStyles() throws Exception
    {
        styledText1 = new StyledText("hi","a");
        styledText2 = new StyledText("hi","a");

        assertTrue(styledText1.equals(styledText2));
    }

    public void testEqualUsesStringEqualsMethod() throws Exception
    {
        String text1 = new String("abcd");
        String text2 = new String("abcd");
        styledText1 = new StyledText(text1);
        styledText2 = new StyledText(text2);

        assertTrue(styledText1.equals(styledText2));
    }

    public void testChangeText() throws Exception
    {
        styledText = new StyledText("hi","a");
        styledText.setText("bye");

        assertEquals(styledText.getText(), "bye");
    }

    public void testChangeStyle() throws Exception
    {
        styledText = new StyledText("hi","a");
        styledText.setStyle("bye");

        assertEquals(styledText.getStyle(), "bye");
    }

    public void testRemoveUnusedTags() throws Exception
    {
        styledText = new StyledText("a<b>bc","a");

        assertTrue(styledText.equals(new StyledText("abc","a")));
    }

    public void testRemoveMultipleUnusedTags() throws Exception
    {
        styledText = new StyledText("<q>a</b>b</c>c<d>","default");

        System.out.println("!!! " + styledText.getText());
        assertTrue(styledText.equals(new StyledText("abc","default")));
    }
}
