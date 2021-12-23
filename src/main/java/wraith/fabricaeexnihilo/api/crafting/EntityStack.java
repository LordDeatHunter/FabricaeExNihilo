package wraith.fabricaeexnihilo.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class EntityStack {
    public static final Codec<EntityStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ENTITY_TYPE.getCodec()
                    .fieldOf("type")
                    .forGetter(EntityStack::getType),
            Codec.INT
                    .fieldOf("size")
                    .forGetter(EntityStack::getSize),
            NbtCompound.CODEC
                    .fieldOf("data")
                    .forGetter(EntityStack::getData)
    ).apply(instance, EntityStack::new));
    

    private EntityType<?> type;
    private int size;
    private NbtCompound data;

    public EntityStack(Identifier identifier, int size, NbtCompound data) {
        this(Registry.ENTITY_TYPE.get(identifier), size, data);
    }

    public EntityStack(String identifier, int size, NbtCompound data) {
        this(new Identifier(identifier), size, data);
    }

    public EntityStack(NbtCompound nbt) {
        this(nbt.getString("type"), nbt.getInt("size"), nbt.getCompound("data"));
    }

    public EntityStack(EntityType<?> entityType, int size, NbtCompound data) {
        this.type = entityType;
        this.size = size;
        this.data = data;
    }

    public EntityStack(EntityType<?> entityType, int size) {
        this(entityType, size, new NbtCompound());
    }

    public EntityStack(EntityType<?> entityType, NbtCompound data) {
        this(entityType, 1, data);
    }

    public EntityStack(EntityType<?> entityType) {
        this(entityType, 1, new NbtCompound());
    }

    public boolean isEmpty() {
        return this == EMPTY || size == 0;
    }

    public EntityType<?> getCategory() {
        return type;
    }

    public EntityType<?> getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public NbtCompound getData() {
        return data;
    }

    public void setType(EntityType<?> type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setData(NbtCompound data) {
        this.data = data;
    }

    public Entity getEntity(ServerWorld world, BlockPos pos, SpawnReason spawnType) {
        return type.create(world, data, null, null, pos, spawnType, true, true);
    }

    public Entity getEntity(ServerWorld world, BlockPos pos) {
        return getEntity(world, pos, SpawnReason.SPAWNER);
    }

    public EntityStack copy() {
        return new EntityStack(this.type, this.size, this.data.copy());
    }

    public static final EntityStack EMPTY = new EntityStack(EntityType.PIG, 0);

}