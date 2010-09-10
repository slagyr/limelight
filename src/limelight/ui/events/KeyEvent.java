package limelight.ui.events;

import limelight.ui.Panel;

public class KeyEvent extends ModifiableEvent
{
  public static final int KEY_UNKNOWN = 0;
  public static final int KEY_ENTER = '\n';
  public static final int KEY_BACK_SPACE = '\b';
  public static final int KEY_TAB = '\t';
  public static final int KEY_CANCEL = 0x03;
  public static final int KEY_CLEAR = 0x0C;
  public static final int KEY_SHIFT = 0x10;
  public static final int KEY_CONTROL = 0x11;
  public static final int KEY_ALT = 0x12;
  public static final int KEY_PAUSE = 0x13;
  public static final int KEY_CAPS_LOCK = 0x14;
  public static final int KEY_ESCAPE = 0x1B;
  public static final int KEY_SPACE = 0x20;
  public static final int KEY_PAGE_UP = 0x21;
  public static final int KEY_PAGE_DOWN = 0x22;
  public static final int KEY_END = 0x23;
  public static final int KEY_HOME = 0x24;
  public static final int KEY_LEFT = 0x25;
  public static final int KEY_UP = 0x26;
  public static final int KEY_RIGHT = 0x27;
  public static final int KEY_DOWN = 0x28;
  public static final int KEY_COMMA = 0x2C;
  public static final int KEY_MINUS = 0x2D;
  public static final int KEY_PERIOD = 0x2E;
  public static final int KEY_SLASH = 0x2F;
  public static final int KEY_0 = 0x30;
  public static final int KEY_1 = 0x31;
  public static final int KEY_2 = 0x32;
  public static final int KEY_3 = 0x33;
  public static final int KEY_4 = 0x34;
  public static final int KEY_5 = 0x35;
  public static final int KEY_6 = 0x36;
  public static final int KEY_7 = 0x37;
  public static final int KEY_8 = 0x38;
  public static final int KEY_9 = 0x39;
  public static final int KEY_SEMICOLON = 0x3B;
  public static final int KEY_EQUALS = 0x3D;
  public static final int KEY_A = 0x41;
  public static final int KEY_B = 0x42;
  public static final int KEY_C = 0x43;
  public static final int KEY_D = 0x44;
  public static final int KEY_E = 0x45;
  public static final int KEY_F = 0x46;
  public static final int KEY_G = 0x47;
  public static final int KEY_H = 0x48;
  public static final int KEY_I = 0x49;
  public static final int KEY_J = 0x4A;
  public static final int KEY_K = 0x4B;
  public static final int KEY_L = 0x4C;
  public static final int KEY_M = 0x4D;
  public static final int KEY_N = 0x4E;
  public static final int KEY_O = 0x4F;
  public static final int KEY_P = 0x50;
  public static final int KEY_Q = 0x51;
  public static final int KEY_R = 0x52;
  public static final int KEY_S = 0x53;
  public static final int KEY_T = 0x54;
  public static final int KEY_U = 0x55;
  public static final int KEY_V = 0x56;
  public static final int KEY_W = 0x57;
  public static final int KEY_X = 0x58;
  public static final int KEY_Y = 0x59;
  public static final int KEY_Z = 0x5A;
  public static final int KEY_OPEN_BRACKET = 0x5B;
  public static final int KEY_BACK_SLASH = 0x5C;
  public static final int KEY_CLOSE_BRACKET = 0x5D;
  public static final int KEY_NUMPAD0 = 0x60;
  public static final int KEY_NUMPAD1 = 0x61;
  public static final int KEY_NUMPAD2 = 0x62;
  public static final int KEY_NUMPAD3 = 0x63;
  public static final int KEY_NUMPAD4 = 0x64;
  public static final int KEY_NUMPAD5 = 0x65;
  public static final int KEY_NUMPAD6 = 0x66;
  public static final int KEY_NUMPAD7 = 0x67;
  public static final int KEY_NUMPAD8 = 0x68;
  public static final int KEY_NUMPAD9 = 0x69;
  public static final int KEY_MULTIPLY = 0x6A;
  public static final int KEY_ADD = 0x6B;
  public static final int KEY_SEPARATOR = 0x6C;
  public static final int KEY_SUBTRACT = 0x6D;
  public static final int KEY_DECIMAL = 0x6E;
  public static final int KEY_DIVIDE = 0x6F;
  public static final int KEY_DELETE = 0x7F;
  public static final int KEY_NUM_LOCK = 0x90;
  public static final int KEY_SCROLL_LOCK = 0x91;
  public static final int KEY_F1 = 0x70;
  public static final int KEY_F2 = 0x71;
  public static final int KEY_F3 = 0x72;
  public static final int KEY_F4 = 0x73;
  public static final int KEY_F5 = 0x74;
  public static final int KEY_F6 = 0x75;
  public static final int KEY_F7 = 0x76;
  public static final int KEY_F8 = 0x77;
  public static final int KEY_F9 = 0x78;
  public static final int KEY_F10 = 0x79;
  public static final int KEY_F11 = 0x7A;
  public static final int KEY_F12 = 0x7B;
  public static final int KEY_F13 = 0xF000;
  public static final int KEY_F14 = 0xF001;
  public static final int KEY_F15 = 0xF002;
  public static final int KEY_F16 = 0xF003;
  public static final int KEY_F17 = 0xF004;
  public static final int KEY_F18 = 0xF005;
  public static final int KEY_F19 = 0xF006;
  public static final int KEY_F20 = 0xF007;
  public static final int KEY_F21 = 0xF008;
  public static final int KEY_F22 = 0xF009;
  public static final int KEY_F23 = 0xF00A;
  public static final int KEY_F24 = 0xF00B;
  public static final int KEY_PRINTSCREEN = 0x9A;
  public static final int KEY_INSERT = 0x9B;
  public static final int KEY_HELP = 0x9C;
  public static final int KEY_META = 0x9D;
  public static final int KEY_BACK_QUOTE = 0xC0;
  public static final int KEY_QUOTE = 0xDE;
  public static final int KEY_KP_UP = 0xE0;
  public static final int KEY_KP_DOWN = 0xE1;
  public static final int KEY_KP_LEFT = 0xE2;
  public static final int KEY_KP_RIGHT = 0xE3;
  public static final int KEY_DEAD_GRAVE = 0x80;
  public static final int KEY_DEAD_ACUTE = 0x81;
  public static final int KEY_DEAD_CIRCUMFLEX = 0x82;
  public static final int KEY_DEAD_TILDE = 0x83;
  public static final int KEY_DEAD_MACRON = 0x84;
  public static final int KEY_DEAD_BREVE = 0x85;
  public static final int KEY_DEAD_ABOVEDOT = 0x86;
  public static final int KEY_DEAD_DIAERESIS = 0x87;
  public static final int KEY_DEAD_ABOVERING = 0x88;
  public static final int KEY_DEAD_DOUBLEACUTE = 0x89;
  public static final int KEY_DEAD_CARON = 0x8a;
  public static final int KEY_DEAD_CEDILLA = 0x8b;
  public static final int KEY_DEAD_OGONEK = 0x8c;
  public static final int KEY_DEAD_IOTA = 0x8d;
  public static final int KEY_DEAD_VOICED_SOUND = 0x8e;
  public static final int KEY_DEAD_SEMIVOICED_SOUND = 0x8f;
  public static final int KEY_AMPERSAND = 0x96;
  public static final int KEY_ASTERISK = 0x97;
  public static final int KEY_QUOTEDBL = 0x98;
  public static final int KEY_LESS = 0x99;
  public static final int KEY_GREATER = 0xa0;
  public static final int KEY_BRACELEFT = 0xa1;
  public static final int KEY_BRACERIGHT = 0xa2;
  public static final int KEY_AT = 0x0200;
  public static final int KEY_COLON = 0x0201;
  public static final int KEY_CIRCUMFLEX = 0x0202;
  public static final int KEY_DOLLAR = 0x0203;
  public static final int KEY_EURO_SIGN = 0x0204;
  public static final int KEY_EXCLAMATION_MARK = 0x0205;
  public static final int KEY_INVERTED_EXCLAMATION_MARK = 0x0206;
  public static final int KEY_LEFT_PARENTHESIS = 0x0207;
  public static final int KEY_NUMBER_SIGN = 0x0208;
  public static final int KEY_PLUS = 0x0209;
  public static final int KEY_RIGHT_PARENTHESIS = 0x020A;
  public static final int KEY_UNDERSCORE = 0x020B;
  public static final int KEY_WINDOWS = 0x020C;
  public static final int KEY_CONTEXT_MENU = 0x020D;
  public static final int KEY_FINAL = 0x0018;
  public static final int KEY_CONVERT = 0x001C;
  public static final int KEY_NONCONVERT = 0x001D;
  public static final int KEY_ACCEPT = 0x001E;
  public static final int KEY_MODECHANGE = 0x001F;
  public static final int KEY_KANA = 0x0015;
  public static final int KEY_KANJI = 0x0019;
  public static final int KEY_ALPHANUMERIC = 0x00F0;
  public static final int KEY_KATAKANA = 0x00F1;
  public static final int KEY_HIRAGANA = 0x00F2;
  public static final int KEY_FULL_WIDTH = 0x00F3;
  public static final int KEY_HALF_WIDTH = 0x00F4;
  public static final int KEY_ROMAN_CHARACTERS = 0x00F5;
  public static final int KEY_ALL_CANDIDATES = 0x0100;
  public static final int KEY_PREVIOUS_CANDIDATE = 0x0101;
  public static final int KEY_CODE_INPUT = 0x0102;
  public static final int KEY_JAPANESE_KATAKANA = 0x0103;
  public static final int KEY_JAPANESE_HIRAGANA = 0x0104;
  public static final int KEY_JAPANESE_ROMAN = 0x0105;
  public static final int KEY_KANA_LOCK = 0x0106;
  public static final int KEY_INPUT_METHOD_ON_OFF = 0x0107;
  public static final int KEY_CUT = 0xFFD1;
  public static final int KEY_COPY = 0xFFCD;
  public static final int KEY_PASTE = 0xFFCF;
  public static final int KEY_UNDO = 0xFFCB;
  public static final int KEY_AGAIN = 0xFFC9;
  public static final int KEY_FIND = 0xFFD0;
  public static final int KEY_PROPS = 0xFFCA;
  public static final int KEY_STOP = 0xFFC8;
  public static final int KEY_COMPOSE = 0xFF20;
  public static final int KEY_ALT_GRAPH = 0xFF7E;
  public static final int KEY_BEGIN = 0xFF58;

  public static final int LOCATION_UNKNOWN = 0;
  public static final int LOCATION_STANDARD = 1;
  public static final int LOCATION_LEFT = 2;
  public static final int LOCATION_RIGHT = 3;
  public static final int LOCATION_NUMPAD = 4;

  private int keyCode;
  private int keyLocation;

  public KeyEvent(Panel panel, int modifiers, int keyCode, int keyLocation)
  {
    super(panel, modifiers);
    this.keyCode = keyCode;
    this.keyLocation = keyLocation;
  }

  public int getKeyCode()
  {
    return keyCode;
  }

  public int getKeyLocation()
  {
    return keyLocation;
  }

  @Override
  public String toString()
  {
    return super.toString() + " keyCode=" + keyCode + "/0x" + Integer.toHexString(keyCode) + " location=" + keyLocation;
  }
}
