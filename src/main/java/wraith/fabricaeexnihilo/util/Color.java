package wraith.fabricaeexnihilo.util;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class Color {

    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int color) {
        this(color, true);
    }

    public Color(int color, boolean ignoreAlpha) {
        this(
                (color >> 16 & 255) / 255F,
                (color >> 8 & 255) / 255F,
                (color & 255) / 255F,
                ignoreAlpha ? 1.0f : (color >> 24 & 255) / 255F
        );
    }

    public Color(String hex) {
        this(hexToInt(hex), hex.length() < 8);
    }

    public int toInt() {
        return toIntIgnoreAlpha() | ((int) (a * 255) << 24);
    }

    public int toIntAlphaOne() {
        return toIntIgnoreAlpha() | -16777216;
    }

    public int toIntIgnoreAlpha() {
        return (((int) (r * 255)) << 16) | (((int) (g * 255)) << 8) | ((int) (b * 255));
    }

    public String toHex() {
        return Strings.padStart(Integer.toHexString(toInt()), 8, '0');
    }

    public String toHexNoAlpha() {
        return Strings.padStart(Integer.toHexString(toIntIgnoreAlpha()), 6, '0');
    }

    public int withAlpha() {
        return new Color(r, g, b, a).toInt();
    }

    public int withAlpha(float a) {
        return new Color(r, g, b, a).toInt();
    }

    public int withAlpha(double a) {
        return new Color(r, g, b, (float) a).toInt();
    }

    public static Color fromJson(JsonElement json) {
        if (!(json instanceof JsonPrimitive primitive) || !(primitive.isString() || primitive.isNumber())) {
            throw new JsonParseException("Expected string or number but found " + json + "!");
        }
    
        if (primitive.isString()) {
            return new Color(primitive.getAsString());
        } else {
            return new Color(json.getAsInt());
        }
    }
    
    public static int hexToInt(String hex) {
        var rgb = hex.length() < 8 ? hex : hex.substring(2);
        var a = hex.length() < 8 ? "FF" : hex.substring(0, 2);

        return (Integer.parseInt(a, 16) << 24) | Integer.parseInt(rgb, 16);
    }

    public static Color average(Color left, Color right, float leftWeight) {
        var rightWeight = 1F - leftWeight;
        var r = (float) Math.sqrt(left.r * left.r * leftWeight + right.r * right.r * rightWeight);
        var g = (float) Math.sqrt(left.g * left.g * leftWeight + right.g * right.g * rightWeight);
        var b = (float) Math.sqrt(left.b * left.b * leftWeight + right.b * right.b * rightWeight);
        var a = left.a * leftWeight + right.a * rightWeight;
        return new Color(r, g, b, a);
    }

    public static Color average(Color left, Color right, double leftWeight) {
        return average(left, right, (float)leftWeight);
    }

    public static Color average(Color left, Color right) {
        return average(left, right, 0.5F);
    }

    public static final Color DARK_RED = new Color("AA0000");
    public static final Color RED = new Color("FF5555");
    public static final Color GOLDEN = new Color("FFAA00");
    public static final Color YELLOW = new Color("FFFF55");
    public static final Color DARK_GREEN = new Color("00AA00");
    public static final Color GREEN = new Color("55FF55");
    public static final Color AQUA = new Color("55FFFF");
    public static final Color DARK_AQUA = new Color("00AAAA");
    public static final Color DARK_BLUE = new Color("0000AA");
    public static final Color BLUE = new Color("5555FF");
    public static final Color LIGHT_PURPLE = new Color("FF55FF");
    public static final Color DARK_PURPLE = new Color("AA00AA");
    public static final Color WHITE = new Color("FFFFFF");
    public static final Color GRAY = new Color("AAAAAA");
    public static final Color DARK_GRAY = new Color("555555");
    public static final Color BLACK = new Color("000000");
    public static final Color ORANGE = new Color("FFA500");


    public static final Color IRON = new Color("BF8040");
    public static final Color GOLD = new Color("FFFF00");

    public static final Color ALUMINUM = new Color("BFBFBF");
    public static final Color ARDITE = new Color("FF751A");
    public static final Color BERYLLIUM = new Color("E5ECDD");
    public static final Color BORON = new Color("939393");
    public static final Color COBALT = new Color("3333FF");
    public static final Color COPPER = new Color("FF9933");
    public static final Color LEAD = new Color("330066");
    public static final Color LITHIUM = new Color("EDEDED");
    public static final Color MAGNESIUM = new Color("F8DEF8");
    public static final Color NICKEL = new Color("FFFFCC");
    public static final Color SILVER = new Color("F2F2F2");
    public static final Color TIN = new Color("E6FFF2");
    public static final Color TITANIUM = new Color("BD87CA");
    public static final Color THORIUM = new Color("333333");
    public static final Color TUNGSTEN = new Color("1C1C1C");
    public static final Color URANIUM = new Color("4E5B43");
    public static final Color ZINC = new Color("A59C74");
    public static final Color ZIRCONIUM = new Color("E6E8C7");
}