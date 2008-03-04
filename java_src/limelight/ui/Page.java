package limelight.ui;

import limelight.PageLoader;

public interface Page extends Prop
{
  PageLoader getLoader();
}

//public class Page extends Prop
//{
//	private Hashtable<String, FlatStyle> styles;
//	private Book book;
//
//	public Page()
//	{
//		super();
//		setPage(this);
//		styles = new Hashtable<String, FlatStyle>();
//	}
//
//	public Hashtable<String, FlatStyle> getStyles()
//	{
//		return styles;
//	}
//
//	public void setBook(Book book)
//	{
//		this.book = book;
//	}
//
//	public Book getBook()
//	{
//		return book;
//	}
//
//
//
//}


