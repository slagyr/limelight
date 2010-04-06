//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.win32;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.awt.*;

public interface GDI32 extends W32API
{

  GDI32 INSTANCE = (GDI32) Native.loadLibrary("gdi32", GDI32.class, DEFAULT_OPTIONS);

  class RECT extends Structure
  {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public Rectangle toRectangle()
    {
      return new Rectangle(left, top, right - left, bottom - top);
    }

    public String toString()
    {
      return "[(" + left + "," + top + ")(" + right + "," + bottom + ")]";
    }
  }

  int RDH_RECTANGLES = 1;

  class RGNDATAHEADER extends Structure
  {
    public int dwSize = size();
    public int iType = RDH_RECTANGLES; // required
    public int nCount;
    public int nRgnSize;
    public RECT rcBound;
  }

  class RGNDATA extends Structure
  {
    public RGNDATAHEADER rdh;
    public byte[] Buffer;

    public RGNDATA(int bufferSize)
    {
      Buffer = new byte[bufferSize];
      allocateMemory();
    }
  }

  public HRGN ExtCreateRegion(Pointer lpXform, int nCount, RGNDATA lpRgnData);

  int RGN_AND = 1;
  int RGN_OR = 2;
  int RGN_XOR = 3;
  int RGN_DIFF = 4;
  int RGN_COPY = 5;

  int ERROR = 0;
  int NULLREGION = 1;
  int SIMPLEREGION = 2;
  int COMPLEXREGION = 3;

  int CombineRgn(HRGN hrgnDest, HRGN hrgnSrc1, HRGN hrgnSrc2, int fnCombineMode);

  HRGN CreateRectRgn(int nLeftRect, int nTopRect,
                     int nRightRect, int nBottomRect);

  HRGN CreateRoundRectRgn(int nLeftRect, int nTopRect,
                          int nRightRect, int nBottomRect,
                          int nWidthEllipse,
                          int nHeightEllipse);

  int ALTERNATE = 1;
  int WINDING = 2;

  HRGN CreatePolyPolygonRgn(User32.POINT[] lppt, int[] lpPolyCounts, int nCount, int fnPolyFillMode);

  boolean SetRectRgn(HRGN hrgn, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);

  int SetPixel(HDC hDC, int x, int y, int crColor);

  HDC CreateCompatibleDC(HDC hDC);

  boolean DeleteDC(HDC hDC);

  int BI_RGB = 0;
  int BI_RLE8 = 1;
  int BI_RLE4 = 2;
  int BI_BITFIELDS = 3;
  int BI_JPEG = 4;
  int BI_PNG = 5;

  class BITMAPINFOHEADER extends Structure
  {
    public int biSize = size();
    public int biWidth;
    public int biHeight;
    public short biPlanes;
    public short biBitCount;
    public int biCompression;
    public int biSizeImage;
    public int biXPelsPerMeter;
    public int biYPelsPerMeter;
    public int biClrUsed;
    public int biClrImportant;
  }

  class RGBQUAD extends Structure
  {
    public byte rgbBlue;
    public byte rgbGreen;
    public byte rgbRed;
    public byte rgbReserved = 0;
  }

  class BITMAPINFO extends Structure
  {
    public BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
    //RGBQUAD:
    //byte rgbBlue;
    //byte rgbGreen;
    //byte rgbRed;
    //byte rgbReserved = 0;
    int[] bmiColors = new int[1];

    public BITMAPINFO()
    {
      this(1);
    }

    public BITMAPINFO(int size)
    {
      bmiColors = new int[size];
      allocateMemory();
    }
  }

  int DIB_RGB_COLORS = 0;
  int DIB_PAL_COLORS = 1;

  HBITMAP CreateDIBitmap(HDC hDC, BITMAPINFOHEADER lpbmih, int fdwInit,
                         Pointer lpbInit, BITMAPINFO lpbmi, int fuUsage);

  HBITMAP CreateDIBSection(HDC hDC, BITMAPINFO pbmi, int iUsage,
                           PointerByReference ppvBits, Pointer hSection,
                           int dwOffset);

  HBITMAP CreateCompatibleBitmap(HDC hDC, int width, int height);

  HANDLE SelectObject(HDC hDC, HANDLE hGDIObj);

  boolean DeleteObject(HANDLE p);
}
