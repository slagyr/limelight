__ :name => "sandbox"
__install "header.rb"
arena :vertical_scrollbar => :on, :height => :greedy do
  text_section do
    section_title :text => "Inherits Containing Prop's Styles"
    styled_text :text_color => :dark_red, :text => "All the text here, <big_n_bold>even this text,</big_n_bold> should be red because the parent is red."
  end
  text_section do
    section_title :text => "Simple Tags"
    styled_text :text => "The <quick>quick</quick> <brown>brown</brown> <fox>fox</fox> <jumped>jumped</jumped> <over>over</over> the <lazy>lazy</lazy> <dog>dog</dog>."
  end
  text_section do
    section_title :text => "Nested Styles"
    styled_text :text => "Start with default style, <courier>(set the font face to courier, <bolded>(add some boldness, <green>(and a touch of green)</green>)</bolded>).</courier>"
    styled_text :text => "When you syle <big_n_blue>big, blue text, and nest <green>some green, the green should be big,</green> but not blue</big_n_blue>."
  end
  text_section do
    section_title :text => "Escaping Characters"
    styled_text :text => "The &lt;quick&gt;quick&lt;/quick&gt; &lt;brown&gt;brown&lt;/brown&gt; &lt;fox&gt;fox&lt;/fox&gt; &lt;jumped&gt;jumped&lt;/jumped&gt; &lt;over&gt;over&lt;/over&gt; the &lt;lazy&gt;lazy&lt;/lazy&gt; &lt;dog&gt;dog&lt;/dog&gt;."
    styled_text :text => "The above text should have angle brackets.  And here's an ampersand sign: &amp;"
  end
  text_section do
    section_title :text => "Inline Style Attributes"
    styled_text :text => "You can inline styles! <blah text_color=purple>Purple text</blah>. <blah font_size=30>Big text</blah>. <blah font_face='courier new'>Courier text</blah>."
  end
  text_section do
    section_title :text => "Non-Text Style Attributes"
    styled_text :text => "Here is some text <background_color>with a blue background</background_color>. Tada!"
    styled_text :text => "Here is some text <bordered>with a rounded grey border</bordered>. Tada!"
    styled_text :text => "Here is some text <padded>that has lots of padding</padded>. Tada!"
    styled_text :text => "Here is some text <background_image>with an image background</background_image>. Tada!"
  end
end                                                                                                                                                                            