package leppa.planarartifice.tiles;

import leppa.planarartifice.blocks.BlockStarvingChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.common.tiles.devices.TileHungryChest;

import java.util.List;
import java.util.function.Predicate;

public class TileStarvingChest extends TileHungryChest {
    public int upgrades = 0;

    private void setUpgrades() {
        upgrades = getBlockMetadata() + 1;
    }

    // partially stole from openblocks -p
    @Override
    public void update() {
        super.update();

        if (upgrades < 4 && upgrades >= 0)
            setUpgrades();

        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-upgrades, 0, -upgrades), pos.add(1 + upgrades, 1 + upgrades, 1 + upgrades)), filterEntity::test);
        for (Entity e : entities) {
            double x = (0.5 + pos.getX()) - e.posX;
            double y = (0.5 + pos.getY()) - e.posY;
            double z = (0.5 + pos.getZ()) - e.posZ;
            double dist = Math.sqrt(x * x + y * y + z * z);
            if ((e.motionX * x) < 0) e.motionX = 0;
            if ((e.motionY * y) < 0) e.motionY = 0;
            if ((e.motionZ * z) < 0) e.motionZ = 0;
            e.motionX += 0.05 * x / dist;
            e.motionY += 0.05 * y / dist;
            e.motionZ += 0.05 * z / dist;
        }
    }

    private final Predicate<Entity> filterEntity = entity -> {
        if (!entity.isDead && entity instanceof EntityItem)
            return ThaumcraftInvHelper.hasRoomForSome(world, pos, EnumFacing.UP, ((EntityItem) entity).getItem());
        return false;
    };

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockStarvingChest) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tags) {
        upgrades = tags.getInteger("upgrades");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        tags.setInteger("upgrades", upgrades);
        return tags;
    }
}
