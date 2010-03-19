__ :name => "sandbox"
__install "header.rb"
arena do
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
    styled_text :text => "The <quick>quick</quick> <brown>brown</brown> <fox>fox</fox> <jumped>jumped</jumped> <over>over</over> the <lazy>lazy</lazy> <dog>dog</dog>."
  end
end