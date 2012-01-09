#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__install "header.rb"
arena do

  controls do
    label :text => "Text:"
    text_input :players => "text_box", :id => "text_input", :text => "This is a test"
    populate_button :players => "button", :text => "Populate", :id => "populate_button"
  end

  section do
    section_label :text => "Solo text"
    section_body do
      text :styles => "big", :id => :solo
    end
  end
  section do
    section_label :text => "Inlined Labeled Text"
    section_body do
      label :text => "label 1:"
      text :id => :inline1

      label :text => "label 2:"
      text :id => :inline2

      label :text => "label 3:"
      text :id => :inline3
    end
  end

  section do
    section_label :text => "Lined Labeled Text"
    section_body do
      line do
        label :text => "label 1:"
        text :id => :lined1
      end

      line do
        label :text => "label 2:"
        text :id => :lined2
      end

      line do
        label :text => "label 3:"
        text :id => :lined3
      end
    end
  end
end