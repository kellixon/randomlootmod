package com.mic.randomloot.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mic.randomloot.RandomLoot;
import com.mic.randomloot.items.AxeItem;
import com.mic.randomloot.items.BowItem;
import com.mic.randomloot.items.CaseItem;
import com.mic.randomloot.items.ClickItem;
import com.mic.randomloot.items.PickaxeItem;
import com.mic.randomloot.items.ShovelItem;
import com.mic.randomloot.items.SwordItem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static final ItemFields ITEM_FIELDS = new ItemFields();
	
	public static final CaseItem BASIC_CASE = new CaseItem("basic_case", 1);
	public static final CaseItem GOLDEN_CASE = new CaseItem("golden_case", 2);
	public static final CaseItem TITAN_CASE = new CaseItem("titan_case", 3);

	public static final Item RL_SWORD = new SwordItem(ToolMaterial.DIAMOND, 20);
	public static final Item RL_PICKAXE = new PickaxeItem(ToolMaterial.DIAMOND, 12);
	public static final Item RL_SHOVEL = new ShovelItem(ToolMaterial.DIAMOND, 6);
	public static final Item RL_AXE = new AxeItem(ToolMaterial.DIAMOND, 6);
	
	public static final Item RL_BOW = new BowItem(6);
	
//	@SideOnly(Side.CLIENT)
//	public static void registerModels()
//	{
//		System.out.println("Registering Models");
//		
//		for (final Item item : ITEMS)
//		{
//			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//			System.out.println(item.getRegistryName());
//			
//			//			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandomLoot.MODID + ":" + item.getUnlocalizedName(),  "inventory"));
//		}
//	}
	
	@Mod.EventBusSubscriber(modid = RandomLoot.MODID)
	public static class ItemRegistry
	{
		public static final Set<Item> ITEM_SET = new HashSet<Item>();
		@SubscribeEvent
		public static void newRegistry(final RegistryEvent.NewRegistry event)
		{
			;
		}
		@SubscribeEvent
		public static void register(final RegistryEvent.Register<Item> event)
		{
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			for (final Item item : ITEMS)
			{
				registry.register(item);
				ITEM_SET.add(item);
			}
			
		}
	}
}
