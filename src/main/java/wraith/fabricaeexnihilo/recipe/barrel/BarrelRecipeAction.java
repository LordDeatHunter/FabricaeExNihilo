package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public sealed interface BarrelRecipeAction {
    default boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
        return true;
    }

    void apply(ServerWorld world, BarrelBlockEntity barrel);

    default void toPacket(PacketByteBuf buf) {
        buf.writeByte(getId());
        writePacket(buf);
    }

    default JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", getName());
        writeJson(json);
        return json;
    }

    void writePacket(PacketByteBuf buf);

    void writeJson(JsonObject json);

    byte getId();

    String getName();

    static BarrelRecipeAction fromJson(JsonObject json) {
        var type = JsonHelper.getString(json, "type");
        return switch (type) {
            case SpawnEntity.NAME -> new SpawnEntity(json);
            case StoreItem.NAME -> new StoreItem(json);
            case StoreFluid.NAME -> new StoreFluid(json);
            case ConsumeFluid.NAME -> new ConsumeFluid(json);
            case ConvertBlock.NAME -> new ConvertBlock(json);
            case DropItem.NAME -> new DropItem(json);
            case FillCompost.NAME -> new FillCompost(json);
            default -> throw new JsonParseException("Unknown action type: " + type);
        };
    }

    static BarrelRecipeAction fromPacket(PacketByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case SpawnEntity.ID -> new SpawnEntity(CodecUtils.fromPacket(EntityStack.CODEC, buf));
            case StoreItem.ID -> new StoreItem(buf.readItemStack());
            case StoreFluid.ID -> new StoreFluid(FluidVariant.fromPacket(buf), buf.readVarLong());
            case ConsumeFluid.ID -> new ConsumeFluid(FluidIngredient.fromPacket(buf), buf.readVarLong());
            case ConvertBlock.ID -> new ConvertBlock(BlockIngredient.fromPacket(buf), CodecUtils.fromPacket(BlockState.CODEC, buf));
            case DropItem.ID -> new DropItem(buf.readItemStack());
            case FillCompost.ID -> new FillCompost(buf);
            default -> throw new JsonParseException("Unknown action type id: " + type);
        };
    }

    record SpawnEntity(EntityStack entities) implements BarrelRecipeAction {
        private static final String NAME = "spawn_entity";
        private static final byte ID = 0;

        public SpawnEntity(JsonObject json) {
            this(CodecUtils.fromJson(EntityStack.CODEC, json.get("entities")));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = barrel.getPos().up();
            for (int i = 0; i < entities.getSize(); i++) {
                var entity = entities.getEntity(world, pos);
                if (entity == null) continue;
                world.spawnEntity(entity);
            }
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            CodecUtils.toPacket(EntityStack.CODEC, entities, buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("entities", CodecUtils.toJson(EntityStack.CODEC, entities));
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record StoreItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "store_item";
        private static final byte ID = 1;

        public StoreItem(JsonObject json) {
            this(CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("stack")));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setItem(stack);
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            buf.writeItemStack(stack);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("stack", CodecUtils.toJson(CodecUtils.ITEM_STACK, stack));
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record StoreFluid(FluidVariant fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "store_fluid";
        private static final byte ID = 2;

        public StoreFluid(JsonObject json) {
            this(CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid")), JsonHelper.getLong(json, "amount", FluidConstants.BUCKET));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setFluid(fluid, amount);
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            fluid.toPacket(buf);
            buf.writeVarLong(amount);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("fluid", CodecUtils.toJson(CodecUtils.FLUID_VARIANT, fluid));
            json.addProperty("amount", amount);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record ConsumeFluid(FluidIngredient fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "consume_fluid";
        private static final byte ID = 3;

        public ConsumeFluid(JsonObject json) {
            this(FluidIngredient.fromJson(json.get("fluid")), JsonHelper.getLong(json, "amount", FluidConstants.BUCKET));
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            return fluid.test(barrel.getFluid().getFluid()) && barrel.getFluidAmount() >= amount;
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setFluid(barrel.getFluid(), barrel.getFluidAmount() - amount);
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            fluid.toPacket(buf);
            buf.writeVarLong(amount);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("fluid", fluid.toJson());
            json.addProperty("amount", amount);
        }


        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record ConvertBlock(BlockIngredient filter, BlockState result) implements BarrelRecipeAction {
        private static final String NAME = "convert_block";
        private static final byte ID = 4;

        public ConvertBlock(JsonObject json) {
            this(BlockIngredient.fromJson(json.get("filter")), CodecUtils.fromJson(BlockState.CODEC, json.get("result")));
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            var world = Objects.requireNonNull(barrel.getWorld(), "world is null");
            var pos = barrel.getPos();
            var radius = FabricaeExNihilo.CONFIG.modules.barrels.leaking.radius;

            return BlockPos.stream(pos.add(-radius, 0, -radius), pos.add(radius, -2, radius))
                    .map(world::getBlockState)
                    .anyMatch(filter);
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = barrel.getPos();
            var radius = FabricaeExNihilo.CONFIG.modules.barrels.leaking.radius;

            var positions = BlockPos.stream(pos.add(-radius, 0, -radius), pos.add(radius, -2, radius))
                    .filter(candidate -> filter.test(world.getBlockState(candidate)))
                    .toList();
            var chosen = positions.get(world.random.nextInt(positions.size()));
            world.setBlockState(chosen, result, Block.NOTIFY_LISTENERS);
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            filter.toPacket(buf);
            CodecUtils.toPacket(BlockState.CODEC, result, buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("filter", filter.toJson());
            json.add("result", CodecUtils.toJson(BlockState.CODEC, result));
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record DropItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "drop_item";
        private static final byte ID = 5;

        public DropItem(JsonObject json) {
            this(CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("stack")));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = Vec3d.ofBottomCenter(barrel.getPos().up());
            ItemScatterer.spawn(world, pos.x, pos.y, pos.z, stack.copy());
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            buf.writeItemStack(stack);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("stack", CodecUtils.toJson(CodecUtils.ITEM_STACK, stack));
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record FillCompost(ItemStack result, float increment) implements BarrelRecipeAction {
        private static final String NAME = "fill_compost";
        private static final byte ID = 6;

        public FillCompost(JsonObject json) {
            this(CodecUtils.fromJson(CodecUtils.ITEM_STACK, JsonHelper.getElement(json, "result")), JsonHelper.getFloat(json, "increment"));
        }

        public FillCompost(PacketByteBuf buf) {
            this(buf.readItemStack(), buf.readFloat());
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            return barrel.getState() == BarrelState.EMPTY || (barrel.getState() == BarrelState.COMPOST && ItemStack.areEqual(barrel.getItem(), result));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.fillCompost(result, increment);
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            buf.writeItemStack(result);
            buf.writeFloat(increment);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, result));
            json.addProperty("increment", increment);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }
}
