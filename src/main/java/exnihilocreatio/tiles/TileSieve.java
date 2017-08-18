package exnihilocreatio.tiles;

import com.google.common.base.Objects;
import exnihilocreatio.ModEnchantments;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.config.Config;
import exnihilocreatio.registries.SieveRegistry;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.Util;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileSieve extends BaseTileEntity {

    private static Random rand = new Random();
    @Getter
    private BlockInfo currentStack;
    @Getter
    private byte progress = 0;
    @Getter
    private ItemStack meshStack = ItemStack.EMPTY;
    @Getter
    private BlockSieve.MeshType meshType = BlockSieve.MeshType.NONE;

    private long lastSieveAction = 0;
    private UUID lastPlayer;

    public TileAutoSifter autoSifter = null;

    /**
     * Sets the mesh type in the sieve.
     *
     * @param newMesh
     * @return true if setting is successful.
     */
    public boolean setMesh(ItemStack newMesh) {
        return setMesh(newMesh, false);
    }

    public boolean setMesh(ItemStack newMesh, boolean simulate) {
        if (progress != 0 || currentStack != null)
            return false;

        if (meshStack.isEmpty()) {
            if (!simulate) {
                meshStack = newMesh.copy();
                meshType = BlockSieve.MeshType.getMeshTypeByID(newMesh.getMetadata());

                this.markDirtyClient();
            }
            return true;
        }

        if (!meshStack.isEmpty() && newMesh.isEmpty()) {
            //Removing
            if (!simulate) {
                meshStack = ItemStack.EMPTY;
                meshType = BlockSieve.MeshType.NONE;

                this.markDirtyClient();
            }
            return true;
        }

        return false;

    }

    public boolean addBlock(ItemStack stack) {
        if (currentStack == null && SieveRegistry.canBeSifted(stack)) {
            if (meshStack.isEmpty())
                return false;
            int meshLevel = meshStack.getItemDamage();
            for (Siftable siftable : SieveRegistry.getDrops(stack)) {
                if (siftable.getMeshLevel() == meshLevel) {
                    currentStack = new BlockInfo(stack);
                    markDirtyClient();
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doSieving(EntityPlayer player, boolean automatedSieving) {
        if (!world.isRemote) {

            if (currentStack == null) {
                return false;
            }

            // 4 ticks is the same period of holding down right click
            if (getWorld().getTotalWorldTime() - lastSieveAction < 4 && !automatedSieving) {
                return false;
            }

            // Really good chance that they're using a macro
            if (!automatedSieving && player != null && getWorld().getTotalWorldTime() - lastSieveAction == 0 && lastPlayer.equals(player.getUniqueID())) {
                if (Config.setFireToMacroUsers) {
                    player.setFire(1);
                }

                player.sendMessage(new TextComponentString("Bad").setStyle(new Style().setColor(TextFormatting.RED).setBold(true)));
            }

            lastSieveAction = getWorld().getTotalWorldTime();
            if (player != null) {
                lastPlayer = player.getUniqueID();
            }

            int efficiency = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.EFFICIENCY, meshStack);
            efficiency += EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, meshStack);

            int fortune = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FORTUNE, meshStack);
            fortune += EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, meshStack);
            if (player != null) {
                fortune += player.getLuck();
            }

            int luckOfTheSea = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LUCK_OF_THE_SEA, meshStack);
            luckOfTheSea += EnchantmentHelper.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, meshStack);

            if (luckOfTheSea > 0 && player != null) {
                luckOfTheSea += player.getLuck();
            }

            progress += 10 + 5 * efficiency;
            markDirtyClient();

            if (progress >= 100) {
                List<ItemStack> drops = SieveRegistry.getRewardDrops(rand, currentStack.getBlockState(), meshStack.getMetadata(), fortune);

                if (drops == null) {
                    drops = new ArrayList<>();
                }

                // Fancy math to make the average fish dropped ~ luckOfTheSea / 2 fish, which is what it was before

                int fishToDrop = (int) Math.round(rand.nextGaussian() + (luckOfTheSea / 2.0));

                fishToDrop = Math.min(fishToDrop, 0);
                fishToDrop = Math.max(fishToDrop, luckOfTheSea);

                for (int i = 0; i < fishToDrop; i++) {
                /*
                 * Gives fish following chances:
                 *  Normal: 43% (3/7)
                 *  Salmon: 29% (2/7)
                 *  Clown:  14% (1/7)
                 *  Puffer: 14% (1/7)
                 */

                    int fishMeta = 0;

                    switch (rand.nextInt(7)) {
                        case 3:
                        case 4:
                            fishMeta = 1;
                            break;
                        case 5:
                            fishMeta = 2;
                            break;
                        case 6:
                            fishMeta = 3;
                            break;
                        default:
                            break;
                    }

                    drops.add(new ItemStack(Items.FISH, 1, fishMeta));
                }

                drops.forEach(stack -> Util.dropItemInWorld(this, player, stack, 1));

                resetSieve();
                markDirtyClient();
            }
        }
        return true;
    }

    public boolean isSieveSimilar(TileSieve sieve) {
        if (sieve == null)
            return false;
        if (meshStack.isEmpty() || sieve.getMeshStack().isEmpty())
            return false;
        return meshStack.getItemDamage() == sieve.getMeshStack().getItemDamage() &&
                progress == sieve.getProgress() &&
                BlockInfo.areEqual(currentStack, sieve.getCurrentStack());
    }

    public boolean isSieveSimilarToInput(TileSieve sieve) {
        if (meshStack.isEmpty() || sieve.getMeshStack().isEmpty())
            return false;
        return meshStack.getItemDamage() == sieve.getMeshStack().getItemDamage() &&
                progress == sieve.getProgress() &&
                sieve.getCurrentStack() == null;
    }

    private void resetSieve() {
        progress = 0;
        currentStack = null;
        markDirtyClient();
    }

    public void validateAutoSieve(){
        if (autoSifter == null || autoSifter.isInvalid() || !(world.getTileEntity(autoSifter.getPos()) instanceof TileAutoSifter || autoSifter.toSift == null)) {
            autoSifter = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture() {
        if (currentStack != null) {
            return Util.getTextureFromBlockState(currentStack.getBlockState());
        }
        return null;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (currentStack != null) {
            NBTTagCompound stackTag = currentStack.writeToNBT(new NBTTagCompound());
            tag.setTag("stack", stackTag);
        }

        if (!meshStack.isEmpty()) {
            NBTTagCompound meshTag = meshStack.writeToNBT(new NBTTagCompound());
            tag.setTag("mesh", meshTag);
        }

        tag.setByte("progress", progress);

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("stack"))
            currentStack = BlockInfo.readFromNBT(tag.getCompoundTag("stack"));
        else
            currentStack = null;

        if (tag.hasKey("mesh")){
            meshStack = new ItemStack(tag.getCompoundTag("mesh"));
            meshType = BlockSieve.MeshType.getMeshTypeByID(meshStack.getMetadata());
        }
        else {
            meshStack = ItemStack.EMPTY;
            meshType = BlockSieve.MeshType.NONE;
        }

        progress = tag.getByte("progress");
        super.readFromNBT(tag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pos.getX(), pos.getY(), pos.getZ());
    }
}