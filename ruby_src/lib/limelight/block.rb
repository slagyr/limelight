require 'limelight/java_util'

module Limelight
  class Block
    
    include Java::limelight.Block
  
    attr_accessor :onclick
    attr_reader :panel, :style
    attr_accessor :page, :name, :text
    getters :panel, :style, :page, :name, :text
    setters :name, :text
    
    def initialize
      @panel = Java::limelight.Panel.new(self)
      @children = []
      @style = Java::limelight.StackableStyle.new
      panel.addMouseListener(Java::limelight.BlockMouseListener.new(self))
    end
    
    def add(child)
      @children << child
      @panel.add(child.panel)
      child.page = @page
    end
    
    def load_style
      if name
        new_style = @page.styles[name];
        @style.add_to_bottom(new_style) if new_style
        @hover_style = page.styles["#{name}.hover"];
      end
      @children.each { |child| child.load_style }
    end
    
    def add_controller(controller_module)
      extend controller_module     
    end
    
    def hoverOn
      return nil if @hover_style.nil?
      @panel.setCursor(java.awt.Cursor.new(java.awt.Cursor::HAND_CURSOR))
      style.push(@hover_style)
      update
    end
    
    def hoverOff
      return nil if @hover_style.nil?
      @panel.setCursor(java.awt.Cursor.new(java.awt.Cursor::DEFAULT_CURSOR))
      @style.pop
      update
    end
  
    def mouseClicked()
      eval(@onclick) if @onclick
    end
  
    def mouseEntered()
    end
  
    def mouseExited()
    end
    
    def update
      @panel.doLayout
      @panel.repaint
    end
    
    def update_now
      @panel.doLayout()
      @panel.paintImmediately(0, 0, @panel.width, @panel.height)
    end     
    
    def find(name)
      return @children.find { |child| child.name == name }
    end
    
    def book
      return page.book
    end
      
  end
end