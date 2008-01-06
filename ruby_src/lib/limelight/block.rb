require 'limelight/java_util'

module Limelight
  class Block
    
    include Java::limelight.Block
  
    attr_reader :panel, :style
    attr_accessor :page, :class_name, :id, :parent
    getters :panel, :style, :page, :class_name, :text
    setters :text
    
    def initialize(hash = {})
      @panel = Java::limelight.Panel.new(self) unless @panel
      @children = [] unless @children
      populate(hash)
      @style = Java::limelight.StackableStyle.new unless @style
    end
    
    def populate(hash)
      hash.each_pair do |key, value|
        setter_sym = "#{key.to_s}=".to_sym
        self.send(setter_sym, value) if self.respond_to?(setter_sym)
      end
    end
    
    def add(child)
      @children << child
      @panel.add(child.panel)
      child.parent = self
      child.page = @page
    end
     
    def <<(child)
      add(child)
      return self
    end
    
    def load_style
      if @class_name
        new_style = @page.styles[@class_name];
        @style.add_to_bottom(new_style) if new_style
        @hover_style = page.styles["#{@class_name}.hover"];
      end
      @children.each { |child| child.load_style }
    end
    
    def add_controller(controller_module)
      extend controller_module unless self.is_a?(controller_module)
    end
    
    def update
      @panel.doLayout
      @panel.repaint
    end
    
    def update_now
      @panel.doLayout()
      @panel.paintImmediately(0, 0, @panel.width, @panel.height)
    end     
    
    def find(id)
      return self if @id == id
      @children.each do |child|
        result = child.find(id)
        return result if result
      end
      return nil
    end
    
    def find_by_class(class_name, results = [])
      results << self if @class_name == class_name
      @children.each { |child| child.find_by_class(class_name, results) }
      return results
    end
    
    def text=(value)
      @panel.text_accessor.text = value
    end
    
    def text
      return panel.text_accessor.text
    end
    
    def book
      return page.book
    end
    
    def to_s
      return "Block[id: #{@id}, class_name: #{@class_name}]"
    end
    
    def inspect
      return self.to_s
    end
    
    # GUI Events ##########################################
    
    def hover_on
      return nil if @hover_style.nil?
      @panel.setCursor(java.awt.Cursor.new(java.awt.Cursor::HAND_CURSOR))
      style.push(@hover_style)
      update
    end
    
    def hover_off
      return nil if @hover_style.nil?
      @panel.setCursor(java.awt.Cursor.new(java.awt.Cursor::DEFAULT_CURSOR))
      @style.pop
      update
    end
    
    def ignore_event(event)
    end
    
    alias :mouse_clicked :ignore_event
    alias :mouse_entered :ignore_event
    alias :mouse_exited :ignore_event
    alias :mouse_pressed :ignore_event
    alias :mouse_released :ignore_event
    alias :mouse_dragged :ignore_event
    alias :mouse_moved :ignore_event
    alias :key_typed :ignore_event
    alias :key_pressed :ignore_event
    alias :key_released :ignore_event
    alias :focus_gained :ignore_event
    alias :focus_lost :ignore_event
    alias :state_changed :ignore_event
    alias :button_pressed :ignore_event
    alias :item_state_changed :ignore_event
    
  end
end