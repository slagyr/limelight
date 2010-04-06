#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  module DSL

    class AnonymousActionListener #:nodoc:
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

    # A class used to build menu bars using a DSL.
    #
    #  MenuBar.build(self) do
    #    menu("File") do
    #      item("Open", :open_chosen_scene)
    #      item("Refresh", :reload)
    #    end
    #  end
    #
    # This example created one menu named 'File' with two menu items: 'Open' and 'Refresh'. The seconds parameter of the
    # menu items is the symbol of a method on the context that will be invoked when the menu item is selected.
    #
    class MenuBar

      def self.build(context, &prop)
        builder = self.new(context)
        builder.instance_eval(&prop)
        return builder.menu_bar
      end

      attr_reader :menu_bar

      # This builder must be provided with a context.  All menu item actions will be invoked on the context.
      #
      def initialize(context)
        @context = context
        @menu_bar = javax.swing.JMenuBar.new
      end

      # Creates a new menu with the provided name
      #
      def menu(name)
        @menu = javax.swing.JMenu.new(name)
        @menu_bar.add(@menu)
        yield
      end

      # Created a new menu item with the provided name.  The symbols paramter is the name of a method on the context
      # that will be invoked when the item is selected.
      #
      def item(name, symbol)
        menu_item = javax.swing.JMenuItem.new(name)
        @menu.add(menu_item)
        menu_item.addActionListener(AnonymousActionListener.new(@context, symbol))
      end

    end
    
  end
end