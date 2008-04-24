module Limelight
  
  class AnonymousActionListener
    include java.awt.event.ActionListener
    
    def initialize(context, symbol)
      @context = context
      @symbol = symbol
    end
    
    def actionPerformed(e)
      method = @context.method(@symbol)
      method.call
    end
  end
  
  class MenuBar
    
    def self.build(context, &prop)
      builder = self.new(context)
      builder.instance_eval(&prop)
      return builder.menu_bar
    end
    
    attr_reader :menu_bar
    
    def initialize(context)
      @context = context
      @menu_bar = javax.swing.JMenuBar.new
    end
    
    def menu(name)
      @menu = javax.swing.JMenu.new(name)
      @menu_bar.add(@menu)
      yield
    end
    
    def item(name, symbol)
      menu_item = javax.swing.JMenuItem.new(name)
      @menu.add(menu_item)
      menu_item.addActionListener(AnonymousActionListener.new(@context, symbol))
    end
    
  end
  
end