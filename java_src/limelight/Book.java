package limelight;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Book
{
	private JFrame frame;
  private File directory;

  public Book()
	{
		frame = new JFrame();

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);
    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    JMenuItem open = new JMenuItem("Open");
    fileMenu.add(open);
    open.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        JFileChooser chooser = new JFileChooser();
        if(directory != null)
          chooser.setCurrentDirectory(directory);
        chooser.setFileFilter(new FileFilter() {
          public boolean accept(File file)
          {
            return file.getName().endsWith("llm");  
          }

          public String getDescription()
          {
            return "Limelight Markup File";
          }
        });
        int returnVal = chooser.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          load2(chooser.getSelectedFile().getAbsolutePath());
          directory = chooser.getSelectedFile().getAbsoluteFile().getParentFile();
        }
      }
    });
    JMenuItem refresh = new JMenuItem("Refresh");
    refresh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        reload();
      }
    });
    fileMenu.add(refresh);

  }

	public void open()
	{
		open(new DefaultPage());
	}

	public void open(Page page)
	{
		frame.setLayout(null);
		frame.setSize(900, 900);
		frame.setLocation(200, 25);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loadPage(page);
		frame.setVisible(true);
		frame.repaint();
	}

	public void close()
	{
		frame.setVisible(false);
		frame.dispose();
	}

  public void load2(String filename)
  {
System.out.println("filename = " + filename);
    // to be overidden
  }

  public void reload()
  {
System.out.println("reload");
    // to be overidden
  }

  public void loadPage(Page page)
	{
		frame.getContentPane().removeAll();
		frame.add(page.getPanel());
		page.setBook(this);
		page.getPanel().setSize(frame.getSize());
	}

	public JFrame getFrame()
	{
		return frame;
	}
}
