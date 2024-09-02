package leppa.planarartifice.tiles;

import leppa.planarartifice.main.CommonProxy;
import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.network.PacketRequestUpdateTeleporter;
import leppa.planarartifice.network.PacketUpdateTeleporter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.client.fx.FXDispatcher;

import javax.annotation.Nullable;

public class TileTeleporter extends TileEntity implements ITickable{
	
	Ticket chunkTicket;
	
	public ItemStackHandler crystal = new ItemStackHandler(1){
		@Override
		protected void onContentsChanged(int slot){
			if(!world.isRemote){
				lastChangeTime = world.getTotalWorldTime();
				CommonProxy.network.sendToAllAround(new PacketUpdateTeleporter(TileTeleporter.this),
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(),
								pos.getZ(), 64));
			}
		}
	};
	public long lastChangeTime;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		compound.setTag("inventory", crystal.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		crystal.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)crystal
				: super.getCapability(capability, facing);
	}
	
	@Override
	public void onLoad(){
		if(world.isRemote){
			CommonProxy.network.sendToServer(new PacketRequestUpdateTeleporter(this));
		}
	}
	
	public Aspect getAspect(){
		if(!crystal.getStackInSlot(0)
				.isEmpty()){ return ((ItemGenericEssentiaContainer)crystal.getStackInSlot(0).getItem())
						.getAspects(crystal.getStackInSlot(0)).getAspects()[0]; }
		return null;
	}
	
	@Override
	public void update(){
		if (chunkTicket == null) chunkTicket = ForgeChunkManager.requestTicket(PlanarArtifice.instance, world, Type.NORMAL);
		ForgeChunkManager.forceChunk(chunkTicket, new ChunkPos(pos));
		if (world.isRemote && getAspect() != null) {
			int c = getAspect().getColor();
			float r = ((c & 0xFF0000) >> 16) / 255.0F;
			float g = ((c & 0x00FF00) >> 8) / 255.0F;
			float b = (c & 0x0000FF) / 255.0F;
			for (int i = 0; i < 5; i++) {
				float theta = world.rand.nextFloat() * ((float)Math.PI * 2);
				FXDispatcher.INSTANCE.drawSimpleSparkle(world.rand, pos.getX() + 0.5 + MathHelper.cos(theta), pos.getY() + 0.5, pos.getZ() + 0.5 + MathHelper.sin(theta), 0.0D, 0.0025D, 0.0D, 0.4F + (float)this.getWorld().rand.nextGaussian() * 0.1F, r, g, b, world.rand.nextInt(5), 1.0F, 0, 8);
			}
		}
	}
}