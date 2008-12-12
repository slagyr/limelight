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
    private String styleName;

    public Boolean equals(StyledText other)
    {
        return (this.text.equals(other.getText()) && this.styleName.equals(other.getStyleName()));
    }

    public StyledText(String text)
    {
        this.text = text;
        this.styleName = "default";
    }

    public StyledText(String text, String style)
    {
        this.text = text;
        this.styleName = style;
    }

    public String getText()
    {
        return text;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String style)
    {
        this.styleName = style;
    }

    public void setText(String text)
    {
        this.text = text;
    }
    
}
