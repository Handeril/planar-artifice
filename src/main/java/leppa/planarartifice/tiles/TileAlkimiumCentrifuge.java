package leppa.planarartifice.tiles;

import leppa.planarartifice.main.PAConfig;
import leppa.planarartifice.util.Aspects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.essentia.TileCentrifuge;

import javax.annotation.Nonnull;

public class TileAlkimiumCentrifuge extends TileCentrifuge {
    int count = 0;
    int process = 0;
    float rotationSpeed = 0.0F;
    boolean success = true;
    int pp = 0;

    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        if (this.aspectIn == null && !aspect.isPrimal()) {
            this.aspectIn = aspect;
            this.process = 40;
            this.markDirty();
            this.world.markAndNotifyBlock(this.getPos(), this.world.getChunk(this.getPos()), this.world.getBlockState(this.getPos()), this.world.getBlockState(this.getPos()), 3);
            return 1;
        } else return 0;
    }

    public void update() {
        if (!this.world.isRemote) {
            if (!this.gettingPower()) {
                if (this.aspectOut == null && this.aspectIn == null && ++this.count % 5 == 0) this.drawEssentia();
                if (this.process > 0) --this.process;
                if (this.aspectOut == null && this.aspectIn != null && this.process % 20 == 0) this.processEssentia(this.process / 20);
            }
        } else {
            if (this.aspectIn != null && !this.gettingPower() && this.rotationSpeed < 20.0F) this.rotationSpeed += 2.0F;
            else if ((this.aspectIn == null || this.gettingPower()) && this.rotationSpeed > 0.0F) this.rotationSpeed -= 0.5F;
            int pr = (int)this.rotation;
            this.rotation += this.rotationSpeed;
            if (this.rotation % 180.0F <= 20.0F && pr % 180 >= 160 && this.rotationSpeed > 0.0F)
                this.world.playSound((double)this.getPos().getX() + 0.5D, (double)this.getPos().getY() + 0.5D, (double)this.getPos().getZ() + 0.5D, SoundsTC.pump, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    void processEssentia(int number) {
        final BlockPos pos = this.getPos();
        final IBlockState state = this.world.getBlockState(pos);
        if (success) {
            Aspect[] comps = this.aspectIn.getComponents();
            this.aspectOut = comps[number];
            this.markDirty();
            this.world.markAndNotifyBlock(pos, this.world.getChunk(pos), state, state, 3);
        } else {
            this.aspectOut = null;
            AuraHelper.polluteAura(this.world, pos, pp / 2.0F, true);
        }
        if (number == 0) this.aspectIn = null;
    }

    void drawEssentia() {
        TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.world, this.getPos(), EnumFacing.DOWN);
        if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(EnumFacing.UP)) return;
            Aspect ta = null;
            if (ic.getEssentiaAmount(EnumFacing.UP) > 0 && ic.getSuctionAmount(EnumFacing.UP) < this.getSuctionAmount(EnumFacing.DOWN) && this.getSuctionAmount(EnumFacing.DOWN) >= ic.getMinimumSuction())
                ta = ic.getEssentiaType(EnumFacing.UP);
            if (ta != null && !ta.isPrimal() && ic.getSuctionAmount(EnumFacing.UP) < this.getSuctionAmount(EnumFacing.DOWN) && ic.takeEssentia(ta, 1, EnumFacing.UP) == 1) {
                final BlockPos pos = this.getPos();
                final IBlockState state = this.world.getBlockState(pos);
                this.aspectIn = ta;
                success = (this.world.rand.nextFloat() > PAConfig.balance.centrifugeFluxRate);
                if (!success) pp = AspectHelper.reduceToPrimals(new Aspects(ta, 1)).aspects.values().parallelStream().reduce(0, Integer::sum);
                this.process = 40;
                this.markDirty();
                this.world.markAndNotifyBlock(pos, this.world.getChunk(pos), state, state, 3);
            }
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (this.aspectIn != null) compound.setString("aspectIn", this.aspectIn.getTag());
        if (this.aspectOut != null) compound.setString("aspectOut", this.aspectOut.getTag());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.aspectIn = Aspect.getAspect(compound.getString("aspectIn"));
        this.aspectOut = Aspect.getAspect(compound.getString("aspectOut"));
        super.readFromNBT(compound);
    }
}
