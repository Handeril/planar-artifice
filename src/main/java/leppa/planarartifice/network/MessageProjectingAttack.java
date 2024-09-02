package leppa.planarartifice.network;

import io.netty.buffer.ByteBuf;
import leppa.planarartifice.enchantment.EnumInfusionEnchantmentII;
import leppa.planarartifice.main.PlanarArtifice;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageProjectingAttack implements IMessage {
	private int entityId;

	public MessageProjectingAttack() {

	}

	public MessageProjectingAttack(int parEntityId) {
		entityId = parEntityId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = ByteBufUtils.readVarInt(buf, 4);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, entityId, 4);
	}

	public static class Handler implements IMessageHandler<MessageProjectingAttack, IMessage> {
		@Override
		public IMessage onMessage(final MessageProjectingAttack message, MessageContext ctx) {
			final EntityPlayer thePlayer = PlanarArtifice.proxy.getPlayerEntityFromContext(ctx);
			thePlayer.getServer().addScheduledTask(() -> {
				Entity theEntity = thePlayer.world.getEntityByID(message.entityId);
				// Need to ensure that hackers can't cause trick kills,
				// so double check weapon type and reach
				if(getProjectingLevel(thePlayer.getHeldItemMainhand()) > 0) {
					int projectingLevel = getProjectingLevel(thePlayer.getHeldItemMainhand()) + 5;
					double distanceSq = thePlayer.getDistanceSq(theEntity);
					double reachSq = projectingLevel ^ 2;
					if(distanceSq < reachSq)
						thePlayer.attackTargetEntityWithCurrentItem(theEntity);

				}
			});
			return null;
		}
	}

	public static int getProjectingLevel(ItemStack stack) {
		return EnumInfusionEnchantmentII.getInfusionEnchantmentLevel(stack, EnumInfusionEnchantmentII.PROJECTING);
	}
}
