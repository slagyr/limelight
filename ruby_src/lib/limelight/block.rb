require 'limelight/java_util'

module Limelight
  class Block
    
    class << self

      def event(*syms)       
        @events ||= []
        syms.each do |sym|
          @events << sym unless @events.include?(sym)
          define_method(sym) { |event| } # do nothing by default
        end
      end

      def events
        return @events
      end

    end
    
    include Java::limelight.ui.Block
  
    attr_reader :panel, :style, :children
    attr_accessor :page, :class_name, :id, :parent, :players
    getters :panel, :style, :page, :class_name, :text
    setters :text
    
    def initialize(hash = {})
      @panel = Java::limelight.ui.Panel.new(self) unless @panel
      @children = [] unless @children
      @style = Java::limelight.ui.StackableStyle.new unless @style
      populate(hash)
    end
    
    def populate(hash)
      hash.each_pair do |key, value|
        setter_sym = "#{key.to_s}=".to_sym        
        if self.respond_to?(setter_sym)
          self.send(setter_sym, value) 
        elsif self.style.respond_to?(setter_sym)
          self.style.send(setter_sym, value)
        elsif is_event_setter(key)
          define_event(key, value)
        end
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
    
    event :mouse_clicked, :mouse_entered, :mouse_exited, :mouse_pressed, :mouse_released, :mouse_dragged, :mouse_moved
    event :key_typed, :key_pressed, :key_released
    event :focus_gained, :focus_lost
    event :state_changed, :button_pressed
    event :item_state_changed
   
    private ###############################################
    
    def is_event_setter(symbol)
      string_value = symbol.to_s
      return string_value[0..2] == "on_" and self.events.include?(string_value[3..-1].to_sym)
    end
    
    def define_event(symbol, value)
      event_name = symbol.to_s[3..-1]
      self.instance_eval "def #{event_name}(event); #{value}; end"
    end
    
  end
end