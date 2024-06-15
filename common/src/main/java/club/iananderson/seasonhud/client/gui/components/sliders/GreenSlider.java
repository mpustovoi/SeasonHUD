package club.iananderson.seasonhud.client.gui.components.sliders;

import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.util.Rgb;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

public class GreenSlider extends RgbSlider {

  public GreenSlider(int x, int y, ColorEditBox seasonBox) {
    super(x, y, seasonBox);
    this.width = seasonBox.getWidth() + 2;
    this.seasonBox = seasonBox;
    this.maxValue = 255;
    this.initial = Rgb.getGreen(this.season);
    this.g = Rgb.rgbColor(Integer.parseInt(seasonBox.getValue())).getGreen();
    this.value = snapToNearest(this.g);
    this.textColor = ChatFormatting.GREEN;
    this.updateMessage();
  }

  @Override
  public void setSliderValue(int newValue) {
    int oldValue = (int) this.value;
    this.value = snapToNearest(Rgb.rgbColor(newValue).getGreen());
    if (!Mth.equal(oldValue, this.value)) {
      this.g = Rgb.rgbColor(newValue).getGreen();
    }

    this.updateMessage();
  }

  @Override
  protected void applyValue() {
    this.r = Rgb.getRed(this.season);
    this.b = Rgb.getBlue(this.season);
    this.rgb = Rgb.rgbInt(this.r, this.getValueInt(), this.b);

    Rgb.setRgb(this.season, this.rgb);
    this.seasonBox.setValue(String.valueOf(this.rgb));
  }
}