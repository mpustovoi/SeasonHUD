package club.iananderson.seasonhud.client.gui.screens;

import static club.iananderson.seasonhud.client.SeasonHUDClient.mc;

import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.client.gui.components.buttons.DefaultColorButton;
import club.iananderson.seasonhud.client.gui.components.buttons.MenuButton;
import club.iananderson.seasonhud.client.gui.components.buttons.MenuButton.MenuButtons;
import club.iananderson.seasonhud.client.gui.components.sliders.BlueSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.GreenSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.RedSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.RgbSlider;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.SeasonList;
import club.iananderson.seasonhud.platform.Services;
import club.iananderson.seasonhud.util.Rgb;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ColorScreen extends Screen {

  public static final int WIDGET_PADDING = 6;
  private static final int MENU_PADDING_FULL = 25;
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 20;
  private static final Component TITLE = Component.translatable("menu.seasonhud.color.title");
  private static final Component ENABLE_SEASON_NAME_COLOR = Component.translatable(
      "menu.seasonhud.color.button.enableSeasonNameColor");
  private static final ColorScreen instance = new ColorScreen(SeasonHUDScreen.getInstance());
  public static MenuButton doneButton;
  private final Screen lastScreen;
  private final List<ColorEditBox> seasonBoxes = new ArrayList<>();
  private final List<AbstractWidget> widgets = new ArrayList<>();
  private MenuButton cancelButton;
  private CycleButton<Boolean> seasonNameColorButton;
  private int x;
  private int y;

  public ColorScreen(Screen screen) {
    super(TITLE);
    this.lastScreen = screen;
    this.widgets.toArray().clone();
  }

  public static void open(Screen screen) {
    mc.setScreen(new ColorScreen(screen));
  }

  private static EnumSet<SeasonList> seasonListSet() {
    EnumSet<SeasonList> set = SeasonList.seasons.clone();

    if (!Config.showTropicalSeason.get() || !Services.PLATFORM.getPlatformName().equals("Forge")) {
      set.remove(SeasonList.DRY);
      set.remove(SeasonList.WET);
    }

    return set;
  }

  public boolean isPauseScreen() {
    return true;
  }

  public int getWidth() {
    return mc.getWindow().getGuiScaledWidth();
  }

  public int getHeight() {
    return mc.getWindow().getGuiScaledHeight();
  }

  @Override
  public void tick() {
    seasonBoxes.forEach(EditBox::tick);
    super.tick();
  }

  private void onDone() {
    seasonBoxes.forEach(seasonBoxes -> {
      if (Integer.parseInt(seasonBoxes.getValue()) != seasonBoxes.getColor()) {
        seasonBoxes.save();
      }
    });

    mc.setScreen(this.lastScreen);
  }

  private void onCancel() {
    mc.setScreen(this.lastScreen);
  }

  public int getBoxWidth() {
    int widgetCount = seasonListSet().size();
    int widgetTotalSize = ((80 + WIDGET_PADDING) * widgetCount);
    int scaledWidth = this.getWidth();

    int boxWidth;
    if (scaledWidth < widgetTotalSize) {
      boxWidth = 60;
    } else {
      boxWidth = 80;
    }

    return boxWidth;
  }

  private List<AbstractWidget> seasonWidget(int x, int y, SeasonList season) {
    ColorEditBox colorBox;
    RedSlider redSlider;
    GreenSlider greenSlider;
    BlueSlider blueSlider;
    List<AbstractWidget> seasonWidgetList = new ArrayList<>();

    colorBox = new ColorEditBox(this.font, x, y, getBoxWidth(), BUTTON_HEIGHT, season);
    seasonBoxes.add(colorBox);
    seasonWidgetList.add(colorBox);
    y += colorBox.getHeight() + WIDGET_PADDING;

    x -= 1;
    y += BUTTON_HEIGHT + RgbSlider.SLIDER_PADDING;

    redSlider = new RedSlider(x, y, colorBox);
    seasonWidgetList.add(redSlider);
    y += redSlider.getHeight() + RgbSlider.SLIDER_PADDING;

    greenSlider = new GreenSlider(x, y, colorBox);
    seasonWidgetList.add(greenSlider);
    y += greenSlider.getHeight() + RgbSlider.SLIDER_PADDING;

    blueSlider = new BlueSlider(x, y, colorBox);
    seasonWidgetList.add(blueSlider);

    y -= (greenSlider.getHeight() + redSlider.getHeight() + RgbSlider.SLIDER_PADDING + BUTTON_HEIGHT
        + RgbSlider.SLIDER_PADDING);
    seasonWidgetList.add(new DefaultColorButton(x, y, season, colorBox, button -> {
      int defaultColorInt = season.getDefaultColor();

      if (colorBox.getNewColor() != defaultColorInt) {
        redSlider.setSliderValue(defaultColorInt);
        greenSlider.setSliderValue(defaultColorInt);
        blueSlider.setSliderValue(defaultColorInt);
        colorBox.setValue(String.valueOf(defaultColorInt));

        Rgb.setRgb(season, defaultColorInt);
      }
    }));

    return seasonWidgetList;
  }

  @Override
  public void init() {
    this.widgets.clear();
    int scaledWidth = this.getWidth();

    int leftButtonX = (getWidth() / 2) - (BUTTON_WIDTH + WIDGET_PADDING);
    int rightButtonX = (getWidth() / 2) + WIDGET_PADDING;
    int widgetWidth = getBoxWidth() + WIDGET_PADDING;
    int totalWidgetWidth = (seasonListSet().size() * widgetWidth) - WIDGET_PADDING;

    this.x = scaledWidth / 2 - totalWidgetWidth / 2;
    this.y = MENU_PADDING_FULL + BUTTON_HEIGHT + WIDGET_PADDING + BUTTON_HEIGHT;
    seasonListSet().forEach(season -> {
      this.widgets.addAll(seasonWidget(this.x, this.y, season));
      this.x += widgetWidth;
    });

    //Buttons
    seasonNameColorButton = CycleButton.onOffBuilder(Config.enableSeasonNameColor.get())
                                       .create(leftButtonX, MENU_PADDING_FULL, BUTTON_WIDTH, BUTTON_HEIGHT,
                                           ENABLE_SEASON_NAME_COLOR,
                                           (b, enableColor) -> Config.setEnableSeasonNameColor(enableColor));
    this.widgets.add(seasonNameColorButton);

    doneButton = new MenuButton(leftButtonX, (getHeight() - BUTTON_HEIGHT - WIDGET_PADDING), MenuButtons.DONE,
        press -> this.onDone());
    this.widgets.add(doneButton);

    cancelButton = new MenuButton(rightButtonX, (getHeight() - BUTTON_HEIGHT - WIDGET_PADDING), MenuButtons.CANCEL,
        press -> this.onCancel());
    this.widgets.add(cancelButton);
    this.widgets.forEach(this::addRenderableWidget);
  }

  @Override
  public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(graphics);
    graphics.drawCenteredString(font, TITLE, getWidth() / 2, WIDGET_PADDING, 16777215);
    super.render(graphics, mouseX, mouseY, partialTicks);
  }
}