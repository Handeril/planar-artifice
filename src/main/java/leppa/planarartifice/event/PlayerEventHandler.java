package leppa.planarartifice.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;

import javax.annotation.Nonnull;
import java.util.ListIterator;

@EventBusSubscriber(modid = PlanarArtifice.MODID)
public class PlayerEventHandler {

	private static final String KEEP_INVENTORY = "keepInventory";

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerDeath(PlayerDropsEvent e) {
		if(e.getEntityPlayer() == null || e.getEntityPlayer() instanceof FakePlayer || e.isCanceled())
			return;

		EntityPlayer player = e.getEntityPlayer();
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

		if(player.world.getGameRules().getBoolean(KEEP_INVENTORY))
			return;

		if(hasPlayerMirroredAmulet(player) == -1)
			return;

		baubles.setStackInSlot(hasPlayerMirroredAmulet(e.getEntityPlayer()), ItemStack.EMPTY);

		ListIterator<EntityItem> iter = e.getDrops().listIterator();
		while(iter.hasNext()) {
			EntityItem ei = iter.next();
			ItemStack item = ei.getItem();
			if(addToPlayerInventory(player, item))
				iter.remove();
		}

		for(int i = 0; i != baubles.getSlots(); i++) {
			if(addToPlayerInventory(player, baubles.getStackInSlot(i)))
				baubles.setStackInSlot(i, ItemStack.EMPTY);
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerClone(PlayerEvent.Clone evt) {
		if(!evt.isWasDeath() || evt.isCanceled()) {
			return;
		}
		if(evt.getOriginal() == null || evt.getEntityPlayer() == null || evt.getEntityPlayer() instanceof FakePlayer) {
			return;
		}
		if(evt.getEntityPlayer().world.getGameRules().getBoolean(KEEP_INVENTORY)) {
			return;
		}
		if(evt.getOriginal() == evt.getEntityPlayer() || evt.getOriginal().inventory == evt.getEntityPlayer().inventory
				|| (evt.getOriginal().inventory.armorInventory == evt.getEntityPlayer().inventory.armorInventory && evt
						.getOriginal().inventory.mainInventory == evt.getEntityPlayer().inventory.mainInventory)) {
			return;
		}

		for(int i = 0; i < evt.getOriginal().inventory.armorInventory.size(); i++) {
			ItemStack item = evt.getOriginal().inventory.armorInventory.get(i);
			if(addToPlayerInventory(evt.getEntityPlayer(), item)) {
				evt.getOriginal().inventory.armorInventory.set(i, ItemStack.EMPTY);
			}

		}
		for(int i = 0; i < evt.getOriginal().inventory.mainInventory.size(); i++) {
			ItemStack item = evt.getOriginal().inventory.mainInventory.get(i);
			if(addToPlayerInventory(evt.getEntityPlayer(), item)) {
				evt.getOriginal().inventory.mainInventory.set(i, ItemStack.EMPTY);
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerCloneLast(PlayerEvent.Clone e) {
		if(!e.isWasDeath() || e.isCanceled()) { return; }
		if(e.getOriginal() == null || e.getEntityPlayer() == null || e.getEntityPlayer() instanceof FakePlayer) { return; }
		if(e.getEntityPlayer().world.getGameRules().getBoolean(KEEP_INVENTORY)) { return; }
		if(e.getOriginal() == e.getEntityPlayer() || e.getOriginal().inventory == e.getEntityPlayer().inventory
				|| (e.getOriginal().inventory.armorInventory == e.getEntityPlayer().inventory.armorInventory
						&& e.getOriginal().inventory.mainInventory == e.getEntityPlayer().inventory.mainInventory)) { return; }
		for(int i = 0; i < e.getOriginal().inventory.armorInventory.size(); i++) {
			ItemStack item = e.getOriginal().inventory.armorInventory.get(i);
			if(addToPlayerInventory(e.getEntityPlayer(), item) || tryToSpawnItemInWorld(e.getOriginal(), item)) {
				e.getOriginal().inventory.armorInventory.set(i, ItemStack.EMPTY);
			}
		}
		for(int i = 0; i < e.getOriginal().inventory.mainInventory.size(); i++) {
			ItemStack item = e.getOriginal().inventory.mainInventory.get(i);
			if(addToPlayerInventory(e.getEntityPlayer(), item) || tryToSpawnItemInWorld(e.getOriginal(), item)) {
				e.getOriginal().inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
		}

	}

	private static boolean addToPlayerInventory(EntityPlayer entityPlayer, ItemStack item) {
		if(item == null || entityPlayer == null)  return false;
		InventoryPlayer inv = entityPlayer.inventory;

		if(item.getItem() instanceof ItemArmor) {
			ItemArmor arm = (ItemArmor) item.getItem();
			int index = arm.armorType.getIndex();
			if(inv.armorInventory.get(index).isEmpty()) {
				inv.armorInventory.set(index, item);
				return true;
			}
		}
		for(int i = 0; i < inv.mainInventory.size(); i++) {
			if(inv.mainInventory.get(i).isEmpty()) {
				inv.mainInventory.set(i, item.copy());
				return true;
			}
		}
		return false;
	}

	private static boolean tryToSpawnItemInWorld(EntityPlayer entityPlayer, @Nonnull ItemStack item) {
		if (entityPlayer != null) {
			EntityItem entityitem = new EntityItem(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5,
					entityPlayer.posZ, item);
			entityitem.setPickupDelay(40);
			entityitem.lifespan *= 5;
			entityitem.motionX = 0;
			entityitem.motionZ = 0;
			entityPlayer.world.spawnEntity(entityitem);
			return true;
		}
		return false;
	}

	public static int hasPlayerMirroredAmulet(EntityPlayer player) {
		return BaublesApi.isBaubleEquipped(player, PAItems.mirrored_amulet);
	}

	@SubscribeEvent
	public static void pickupXP(PlayerPickupXpEvent event) {
		if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote && BaublesApi.isBaubleEquipped(event.getEntityPlayer(), PAItems.mirromirous_headband) >= 0 && event.getOrb().getXpValue() > 1) {
			int d = event.getOrb().xpValue / 2;
			EntityXPOrb orb = event.getOrb();
			orb.xpValue -= d;
			float r = event.getEntityPlayer().getRNG().nextFloat();
			String[] s;
			String cat;
			if ((double)r < 0.1D * (double)d) {
				s = ResearchCategories.researchCategories.keySet().toArray(new String[0]);
				cat = s[event.getEntityPlayer().getRNG().nextInt(s.length)];
				ThaumcraftApi.internalMethods.addKnowledge(event.getEntityPlayer(), IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory(cat), 2);
			} else if ((double)r < 0.4D * (double)d) {
				s = ResearchCategories.researchCategories.keySet().toArray(new String[0]);
				cat = s[event.getEntityPlayer().getRNG().nextInt(s.length)];
				ThaumcraftApi.internalMethods.addKnowledge(event.getEntityPlayer(), IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory(cat), 2);
			}
		}
	}

}
