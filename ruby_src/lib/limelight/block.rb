require 'limelight/limelight_java'

module Limelight
  class Block
    
    include Java::limelight.Block
  
    attr_accessor :onclick
    attr_reader :panel, :style
    attr_accessor :page, :name, :text
    getters :panel, :style, :page, :name, :text
    setters :page, :name, :text
    
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



# package limelight;
# 
# import java.util.*;
# import java.awt.*;
# 
# public class Block
# {
#   private Panel panel;
# 
#   private String text;
#   private StackableStyle style;
#   private Page page;
#   private String name;
#   private LinkedList<Block> children;
#   private Style hoverStyle;
# 
#   public Block()
#   {
#     panel = new Panel(this);
#     children = new LinkedList<Block>();
#     style = new StackableStyle();
#     panel.addMouseListener(new BlockMouseListener(this));
#   }
# 
#   public void add(Block child)
#   {
#     children.add(child);
#     panel.add(child.getPanel());
#     child.setPage(page);
#   }
# 
#   public Panel getPanel()
#   {
#     return panel;
#   }
# 
#   public Style getStyle()
#   {
#     return style;
#   }
# 
#   public Page getPage()
#   {
#     return page;
#   }
# 
#   public void setPage(Page page)
#   {
#     this.page = page;
#   }
# 
#   public Book getBook()
#   {
#     return page.getBook();
#   }
# 
#   public void setName(String name)
#   {
#     this.name = name;
#   }
# 
#   public String getName()
#   {
#     return name;
#   }
# 
#   public String getText()
#   {
#     return text;
#   }
# 
#   public void setText(String value)
#   {
#     text = value;
#   }
# 
#   public void setX(int x)
#   {
#     panel.setLocation(x, panel.getLocation().y);
#   }
# 
#   public void setY(int y)
#   {
#     panel.setLocation(panel.getLocation().x, y);
#   }
# 
#   public void loadStyle()
#   {
#     if(name != null)
#     {
#       Style newStyle = page.getStyles().get(name);
#       if(newStyle != null)
#         style.addToBottom(newStyle);
#       hoverStyle = page.getStyles().get(name + ".hover");
#     }
#     for(Block block : children)
#       block.loadStyle();
#   }
# 
#   public void hoverOn()
#   {
#     if(hoverStyle != null)
#     {
#       panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
#       style.push(hoverStyle);
#       panel.repaint();
#     }
#   }
# 
#   public void hoverOff()
#   {
#     if(hoverStyle != null)
#     {
#       panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
#       style.pop();
#       panel.repaint();
#     }
#   }
# 
#   public void mouseClicked()
#   {
#   }
# 
#   public void mouseEntered()
#   {
#   }
# 
#   public void mouseExited()
#   {
#   }
# 
#   public void update()
#   {
#     panel.doLayout();
#     panel.repaint();
#   }
# 
#   public void updateNow()
#   {   
#     panel.doLayout();
#     panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
#   }
# 
#   public Block find(String name)
#   {
#     for (Iterator<Block> blockIterator = children.iterator(); blockIterator.hasNext();)
#     {
#       Block block = blockIterator.next();
#       if(name.equals(block.getName()))
#         return block;
#     }
#     return null;
#   }
# }
