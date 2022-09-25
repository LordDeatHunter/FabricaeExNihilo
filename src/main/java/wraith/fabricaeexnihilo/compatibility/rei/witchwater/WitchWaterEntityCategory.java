package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterEntityCategory implements DisplayCategory<WitchWaterEntityDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int ARROW_IN_U = 0;
    public static final int ARROW_IN_V = 16 * 5;
    public static final int ARROW_OUT_U = 0;
    public static final int ARROW_OUT_V = 16 * 4;
    public static final int HEIGHT = 3 * 18 + 6 * 4;
    public static final int WIDTH = 8 * 18 + 6 * 2;

    // Entity rendering voodoo
    // https://github.com/theorbtwo/RoughlyEnoughResources/blob/1f1c028fa20ebdd34df044e6f160e53cc8a1acf7/common/src/main/java/uk/me/desert_island/rer/rei_stuff/EntityLootCategory.java#L50
    private void createEntityWidget(DrawableHelper helper, MatrixStack matrices, int mouseX, int mouseY, float delta, Entity entity, Rectangle bounds) {
        float f = (float) Math.atan((bounds.getCenterX() - mouseX) / 40.0F);
        float g = (float) Math.atan((bounds.getCenterY() - mouseY) / 40.0F);
        float size = 32;
        if (Math.max(entity.getWidth(), entity.getHeight()) > 1.0) {
            size /= Math.max(entity.getWidth(), entity.getHeight());
        }

        matrices.push();
        matrices.translate(bounds.getCenterX(), bounds.getCenterY() + 20, 1050.0);
        matrices.scale(1, 1, -1);
        matrices.translate(0.0D, 0.0D, 1000.0D);
        matrices.scale(size, size, size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
        quaternion.hamiltonProduct(quaternion2);
        matrices.multiply(quaternion);
        float i = entity.getYaw();
        float j = entity.getPitch();
        float h = 0, k = 0, l = 0;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-g * 20.0F);

        if (entity instanceof LivingEntity living) {
            h = living.bodyYaw;
            k = living.prevHeadYaw;
            l = living.headYaw;
            living.bodyYaw = 180.0F + f * 20.0F;
            living.headYaw = entity.getYaw();
            living.prevHeadYaw = entity.getYaw();
        }

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrices, immediate, 15728880);
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.setYaw(i);
        entity.setPitch(j);

        if (entity instanceof LivingEntity living) {
            living.bodyYaw = h;
            living.prevHeadYaw = k;
            living.headYaw = l;
        }

        matrices.pop();
    }

    @Override
    public CategoryIdentifier<? extends WitchWaterEntityDisplay> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(WitchWaterEntityDisplay display) {
        return WIDTH;
    }

    public Renderer getIcon() {
        return EntryStacks.of(WitchWaterFluid.BUCKET);
    }

    @Override
    public Text getTitle() {
        return Text.translatable("fabricaeexnihilo.rei.category.witch_water.entity");
    }

    @Override
    public List<Widget> setupDisplay(WitchWaterEntityDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();

        // TODO: make separate displays for each entity type
        Entity target = display.target.stream().map(e -> e.create(MinecraftClient.getInstance().world)).filter(Objects::nonNull).findFirst().orElse(null);
        Entity result = display.result.create(MinecraftClient.getInstance().world);

        if (target == null || result == null) {
            FabricaeExNihilo.LOGGER.warn("Unable to create REI display entity");
            return widgets;
        }

        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle targetBounds = new Rectangle(bounds.getMinX(), bounds.getCenterY() - 27, 54, 54);
        Rectangle resultBounds = new Rectangle(bounds.getMaxX() - 55, bounds.getCenterY() - 27, 54, 54);
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + 60, bounds.getMinY() + 20, 16, 16, ARROW, ARROW_IN_U, ARROW_IN_V));
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + 80, bounds.getMinY() + 20, 16, 16, ARROW, ARROW_OUT_U, ARROW_OUT_V));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 70, bounds.getMinY() + 42)).entries(EntryIngredients.of(WitchWaterFluid.BUCKET)));
        List<Text> lines = new ArrayList<>();
        lines.add(target.getDisplayName());
        VillagerProfession profession = display.profession;
        if (profession != null) {
            lines.add(Text.of("-> " + profession));
        }
        widgets.add(Widgets.createTooltip(targetBounds, lines));
        widgets.add(Widgets.createTooltip(resultBounds, result.getDisplayName()));
        widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> createEntityWidget(helper, matrices, mouseX, mouseY, delta, target, targetBounds)));
        widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> createEntityWidget(helper, matrices, mouseX, mouseY, delta, result, resultBounds)));

        return widgets;
    }

}