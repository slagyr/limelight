#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__install "header.rb"
arena do

  controls do
    main_switch :players => "check_box", :checked => true
    label :text => "Text:"
    text_input :players => "text_box", :id => "text_input", :text => "This is a test"
    populate_button :players => "button", :text => "Populate", :id => "populate_button"
  end

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Solo Text"
    section_body do
      text :styles => "big", :id => :solo
    end
  end
  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
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
    section_switch do
      switch :players => "check_box", :checked => true
    end
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

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Text Boxes"
    section_body do
      line do
        label :text => "text box 1:"
        text :id => :text_box1, :players => "text_box"
      end
      line do
        label :text => "text box 2:"
        text :id => :text_box2, :players => "text_box"
      end
    end
  end

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Text Areas"
    section_body do
      line do
        label :text => "text area 1:"
        text :id => :text_area1, :players => "text_area"
      end
    end
  end

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Drop Down"
    section_body do
      line do
        label :text => "drop down 1:"
        text :id => :drop_down1, :players => "drop_down", :choices => %w{red orange yellow green blue indigo violet}
      end
    end
  end

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Button"
    section_body do
      line do
        label :text => "button 1:"
        text :id => :button1, :players => "button"
      end
    end
  end

  section do
    section_switch do
      switch :players => "check_box", :checked => true
    end
    section_label :text => "Check Box"
    section_body do
      line do
        label :text => "check box 1:"
        text :id => :check_box1, :players => "check_box"
      end
    end
  end
end