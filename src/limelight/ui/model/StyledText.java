package limelight.ui.model;


/**
 * Created by IntelliJ IDEA.
 * User: dennyabraham
 * Date: Dec 10, 2008
 * Time: 4:26:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class StyledText
{
    private String text;
    private String style;

    public Boolean equals(StyledText other)
    {
        return (this.text.equals(other.getText()) && this.style.equals(other.getStyle()));
    }

    public StyledText(String text)
    {
        setText(text);
        setStyle("default");
    }

    public StyledText(String text, String style)
    {
        setText(text);
        setStyle(style);
    }

    public String getText()
    {
        return text;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public void setText(String text)
    {
        this.text = text.replaceAll("<(|/)\\w+>","");
    }

}
