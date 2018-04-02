package exnihilocreatio.util;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class EntityInfo {
    @Getter
    private Class <? extends Entity > entityClass;

    private Random rand = new Random();

    public EntityInfo(String entityName){
        LogUtil.log(Level.DEBUG, "Creating EntityInfo");
        entityClass = EntityList.getClassFromName(entityName);
    }

    /**
     * Attempts to spawn entity located  within `range` of `pos`
     * @param pos
     * @param range
     * @param worldIn
     * @return
     */
    public boolean spawnEntityNear(BlockPos pos, int range, World worldIn){
        if(!worldIn.isRemote){
            Entity entity = EntityList.newEntity(entityClass, worldIn);
            double dx = (rand.nextDouble()*2 - 1)*range;
            double dy = (rand.nextDouble()*2 - 1)*range;
            double dz = (rand.nextDouble()*2 - 1)*range;
            entity.setPosition(pos.getX()+dx, pos.getY()+dy, pos.getZ()+dy);
            worldIn.spawnEntity(entity);
            return true;
        }

        return false;
    }
}
