package com.mic.randomloot.util.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.mic.randomloot.RandomLoot;
import com.mic.randomloot.init.ModItems;
import com.mic.randomloot.items.CaseItem;
import com.mic.randomloot.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = RandomLoot.MODID)
public class RegistryHandler {
	
	static World world;
	
	
//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public static void onItemRegister(RegistryEvent.Register<Item> event) {
//		
//		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
//	}

	private static Random random = new Random();

	// @SubscribeEvent
	// public static void onBlockRegister(RegistryEvent.Register<Block> event) {
	// event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
	// TileEntityHandler.registerTileEntities();
	// }

	public static ArrayList<CaseItem> getList() {
		ArrayList<CaseItem> list = new ArrayList<CaseItem>();
		list.add(ModItems.BASIC_CASE);
		list.add(ModItems.GOLDEN_CASE);
		list.add(ModItems.TITAN_CASE);
		Collections.shuffle(list, random);
		return list;
	}

	// Thanks to Lootbags for helping me out here, some of this is taken from
	// lootbags sorry if you thought this was my code
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {

		if (!(event.getSource() instanceof EntityDamageSource)
				|| !(((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityPlayer)
				|| !(((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityPlayer))
			return;

		for (CaseItem i : getList()) {
			// Get the drop roll for the drop resolution
			int chance = random.nextInt(1000);
			// Get the weight
			int weight;
			if (!event.getEntityLiving().isNonBoss())
				weight = i.getBossDropWeight();
			else if (event.getEntityLiving() instanceof EntityMob || event.getEntityLiving() instanceof IMob)
				weight = i.getMonsterDropWeight();
			else if (event.getEntityLiving() instanceof EntityPlayer)
				weight = i.getPlayerDropWeight();
			else if (event.getEntityLiving() instanceof EntityAnimal || event.getEntityLiving() instanceof IAnimals)
				weight = i.getPassiveDropWeight();
			else {
				weight = i.getMonsterDropWeight();
			}

			// ChatComponentText("Command Sender Name: " +
			// event.entityLiving.getCommandSenderName() + ": EventList Name: "
			// + EntityList.getEntityString(event.entityLiving)));

			if (chance <= weight && weight > 0) {

				event.getEntityLiving().entityDropItem(i.getItem(), random.nextInt(2) + 1);
			}
		}
	}

//	@SubscribeEvent
//	public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
//		if (!event.getWorld().isRemote) {
//			Item breaker = null;
//			if (event.getHarvester() != null && event.getHarvester().inventory.getCurrentItem() != null
//					&& event.getHarvester().inventory.getCurrentItem().getItem() != null) {
//				breaker = event.getHarvester().inventory.getCurrentItem().getItem();
//			}
//			if (event.getDrops() != null && event.getDrops().size() != 0) {
//				for (int x = event.getDrops().size() - 1; x >= 0; x--) {
//					if (FurnaceRecipes.instance().getSmeltingResult(event.getDrops().get(x).copy()) != null) {
//						ItemStack thisItem = FurnaceRecipes.instance()
//								.getSmeltingResult(event.getDrops().get(x).copy());
//						if ((breaker == ModItems.RL_PICKAXE) && breaker != null) {
//							System.out.println("It's the right tool! Congrats");
//							NBTTagCompound compound = (event.getHarvester().inventory.getCurrentItem().hasTagCompound())
//									? event.getHarvester().inventory.getCurrentItem().getTagCompound()
//									: new NBTTagCompound();
//							int t1 = compound.getInteger("T1");
//							int t2 = compound.getInteger("T2");
//							int t3 = compound.getInteger("T3");
//							if (t1 == 8 || t2 == 8 || t3 == 8) {
//								System.out.println("Oh and it's auto smelt too!");
//								if (thisItem != null) {
//									event.getDrops().remove(x);
//									event.getDrops().add(thisItem);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	}

	@SubscribeEvent
	public void onHarvest(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();

		if (player != null && !player.world.isRemote) {
			List<Item> tools = new ArrayList<Item>();
			 tools.add(ModItems.RL_AXE);
			tools.add(ModItems.RL_PICKAXE);
			 tools.add(ModItems.RL_SHOVEL);

			ItemStack item = player.getHeldItemMainhand();

			if (tools.contains(item.getItem())) {
				List<ItemStack> smelted = new ArrayList<ItemStack>();

				for (ItemStack drop : event.getDrops()) {
					ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(drop);

					if (smeltResult != null && smeltResult != ItemStack.EMPTY)
						smelted.add(smeltResult.copy());
					else
						smelted.add(drop.copy());
				}

				NBTTagCompound compound = (item.hasTagCompound())
						? item.getTagCompound() : new NBTTagCompound();
				int t1 = compound.getInteger("T1");
				int t2 = compound.getInteger("T2");
				int t3 = compound.getInteger("T3");
				if (t1 == 8 || t2 == 8 || t3 == 8) {

					event.getDrops().clear();
					event.getDrops().addAll(smelted);

				}

			}
		}
	}

	@SubscribeEvent
	public static void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event) {
		// event.getRegistry().registerAll(ModEnchantments.Enchantments.toArray(new
		// Enchantment[0]));
	}

	// @SubscribeEvent
	// public void playerTickEvent(PlayerTickEvent event, EntityPlayer player)
	// // CHOOSE
	// // THE
	// // EVENT
	// // THAT
	// // BEST
	// // SUITS
	// // YOUR
	// // NEEDS
	// {
	//
	// }

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		final LootPool main = event.getTable().getPool("main");

		if (main == null)
			return;

		
		if (event.getName().toString().contains("chest")) {
			addEntry(main, ModItems.BASIC_CASE, 8);
			addEntry(main, ModItems.GOLDEN_CASE, 4);
			addEntry(main, ModItems.TITAN_CASE, 2);
		}
	}

	private void addEntry(LootPool lootPool, Item item, int weight) {
		lootPool.addEntry(new LootEntryItem(item, weight, 0, new LootFunction[0], new LootCondition[0],
				RandomLoot.MODID + ":" + item.getUnlocalizedName().substring(5)));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ModItems.ITEMS) {
			RandomLoot.proxy.registerItemRenderer(item, 0, "inventory");
		}
		// for (Block block : ModBlocks.BLOCKS) {
		// if (block instanceof IHasModel) {
		// ((IHasModel) block).registerModels();
		// }
		// }
	}

	public static void preInitRegistries(FMLPreInitializationEvent event) {
		ConfigHandler.registerConfig(event);

	}

	public static void initRegistries() {
		NetworkHandler net = new NetworkHandler();
		// NetworkRegistry.INSTANCE.registerGuiHandler(Biocraft.instance, new
		// GuiHandler());

	}
}
