package club.iananderson.seasonhud.util;

import club.iananderson.seasonhud.impl.seasons.Seasons;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Rgb {
  private Rgb() {
  }

  public static Map<String, Integer> seasonMap(int rgb) {
    Map<String, Integer> rgbMap = new HashMap<>();

    rgbMap.put("r", rgbColor(rgb).getRed());
    rgbMap.put("g", rgbColor(rgb).getGreen());
    rgbMap.put("b", rgbColor(rgb).getBlue());
    rgbMap.put("rgb", rgb);

    return rgbMap;
  }

  public static Map<String, Integer> defaultSeasonMap(Seasons season) {
    Map<String, Integer> defaultRgbMap = new HashMap<>();

    int rgb = season.getDefaultColor();

    defaultRgbMap.put("r", rgbColor(rgb).getRed());
    defaultRgbMap.put("g", rgbColor(rgb).getGreen());
    defaultRgbMap.put("b", rgbColor(rgb).getBlue());
    defaultRgbMap.put("rgb", rgb);

    return defaultRgbMap;
  }

  public static int rgbInt(int r, int g, int b) {
    return (256 * 256 * r) + (256 * g) + b;
  }

  public static Color rgbColor(int rgb) {
    return new Color(rgb);
  }

  public static void setRgb(Seasons season, int rgb) {
    season.getRgbMap().put("r", rgbColor(rgb).getRed());
    season.getRgbMap().put("g", rgbColor(rgb).getGreen());
    season.getRgbMap().put("b", rgbColor(rgb).getBlue());
    season.getRgbMap().put("rgb", rgb);
  }

  public static void setRgb(Seasons season, int r, int g, int b) {
    season.getRgbMap().put("r", r);
    season.getRgbMap().put("g", g);
    season.getRgbMap().put("b", b);
    season.getRgbMap().put("rgb", rgbInt(r, g, b));
  }

  public static int getRgb(Seasons season) {
    return season.getRgbMap().get("rgb");
  }

  public static int getRed(Seasons season) {
    return season.getRgbMap().get("r");
  }

  public static int getGreen(Seasons season) {
    return season.getRgbMap().get("g");
  }

  public static int getBlue(Seasons season) {
    return season.getRgbMap().get("b");
  }
}
