package taintedmagic.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class RecipeRegistry
{
	public static void init ()
	{
		initCrafting();
		initSmelting();
		initCrucible();
		initInfusion();
		initArcane();
	}

	/**
	 * Vanilla crafting table recipes
	 */
	public static void initCrafting ()
	{
		GameRegistry.addShapedRecipe(new ItemStack(
				Item.getItemFromBlock(BlockRegistry.BlockShadowmetal)), new Object[]{ "AAA", "AAA", "AAA", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial) });

		GameRegistry.addShapedRecipe(new ItemStack(ItemRegistry.ItemMaterial, 1, 0), new Object[]{ "AAA", "AAA", "AAA", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 9) });

		ResearchRegistry.recipes.put("ItemShadowmetalPick", GameRegistry.addShapedRecipe(new ItemStack(ItemRegistry.ItemShadowmetalPick), new Object[]{ "AAA", " B ", " B ", Character.valueOf('A'),
				new ItemStack(ItemRegistry.ItemMaterial), Character.valueOf('B'), new ItemStack(Items.stick) }));

		ResearchRegistry.recipes.put("ItemShadowmetalAxe", CraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.ItemShadowmetalAxe), new Object[]{ "AA", "BA", "B ",
				Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial), Character.valueOf('B'), new ItemStack(Items.stick) }));

		ResearchRegistry.recipes.put("ItemShadowmetalSpade", CraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.ItemShadowmetalSpade), new Object[]{ "A", "B", "B",
				Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial), Character.valueOf('B'), new ItemStack(Items.stick) }));

		ResearchRegistry.recipes.put("ItemShadowmetalHoe", CraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.ItemShadowmetalHoe), new Object[]{ "AA", "B ", "B ",
				Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial), Character.valueOf('B'), new ItemStack(Items.stick) }));

		ResearchRegistry.recipes.put("ItemShadowmetalSword", GameRegistry.addShapedRecipe(new ItemStack(ItemRegistry.ItemShadowmetalSword), new Object[]{ "A", "A", "B", Character.valueOf('A'),
				new ItemStack(ItemRegistry.ItemMaterial), Character.valueOf('B'), new ItemStack(Items.stick) }));

		GameRegistry.addRecipe(new ItemStack(ItemRegistry.ItemMaterial, 9), new Object[]{ "A", Character.valueOf('A'), new ItemStack(BlockRegistry.BlockShadowmetal) });

		GameRegistry.addRecipe(new ItemStack(ItemRegistry.ItemMaterial, 9, 9), new Object[]{ "A", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 0) });

		ResearchRegistry.recipes.put("BlockWarpwoodPlanks", CraftingManager.getInstance()
				.addRecipe(new ItemStack(BlockRegistry.BlockWarpwoodPlanks, 4), new Object[]{ "A", Character.valueOf('A'), new ItemStack(BlockRegistry.BlockWarpwoodLog) }));
	}

	/**
	 * Furnace recipes
	 */
	public static void initSmelting ()
	{
		GameRegistry.addSmelting(BlockRegistry.BlockShadowOre, new ItemStack(ItemRegistry.ItemMaterial), 2.0F);
	}

	/**
	 * Arcane Infusion recipes
	 */
	public static void initInfusion ()
	{
		ResearchRegistry.recipes.put("ItemFocusShockwave", ThaumcraftApi
				.addInfusionCraftingRecipe("FOCUSSHOCKWAVE", new ItemStack(ItemRegistry.ItemFocusShockwave), 6, new AspectList().add(Aspect.AIR, 35).add(Aspect.MOTION, 42).add(Aspect.ENERGY, 42)
						.add(Aspect.MAGIC, 16), new ItemStack(ConfigItems.itemFocusShock), new ItemStack[]{ new ItemStack(Blocks.tnt), new ItemStack(Items.gunpowder),
								new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Blocks.tnt), new ItemStack(Items.gunpowder), new ItemStack(ConfigItems.itemShard, 1, 0) }));

		ResearchRegistry.recipes.put("ItemPrimordialEdge", ThaumcraftApi.addInfusionCraftingRecipe("PRIMALBLADE", new ItemStack(ItemRegistry.ItemPrimordialEdge), 8, new AspectList()
				.add(Aspect.WEAPON, 65).add(Aspect.ELDRITCH, 46).add(Aspect.DARKNESS, 16).add(Aspect.METAL, 60)
				.add(Aspect.VOID, 56), ItemApi.getItem("itemSwordVoid", 0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 5), new ItemStack(ConfigItems.itemShard, 1, 0),
						new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ItemRegistry.ItemMaterial, 1, 10),
						new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(ConfigItems.itemShard, 1, 5) }));

		ResearchRegistry.recipes.put("ItemFocusMageMace", ThaumcraftApi.addInfusionCraftingRecipe("MACEFOCUS", new ItemStack(ItemRegistry.ItemFocusMageMace), 3, new AspectList().add(Aspect.WEAPON, 32)
				.add(Aspect.METAL, 8).add(Aspect.ENTROPY, 18)
				.add(Aspect.MAGIC, 26), new ItemStack(Blocks.iron_block), new ItemStack[]{ new ItemStack(Items.gold_ingot), new ItemStack(Items.quartz), ItemApi.getItem("itemShard", 5),
						new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemSwordThaumium), new ItemStack(Items.quartz), ItemApi.getItem("itemShard", 5), new ItemStack(Items.quartz) }));

		ResearchRegistry.recipes.put("ItemSwordCrimson", ThaumcraftApi.addInfusionCraftingRecipe("CRIMSONBLADE", new ItemStack(ConfigItems.itemSwordCrimson), 7, new AspectList().add(Aspect.WEAPON, 32)
				.add(Aspect.METAL, 16).add(Aspect.ENTROPY, 8).add(Aspect.ELDRITCH, 18)
				.add(Aspect.VOID, 32), ItemApi.getItem("itemSwordVoid", 0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7),
						new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7),
						new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7) }));

		ResearchRegistry.recipes.put("ItemVoidwalkerSash", ThaumcraftApi.addInfusionCraftingRecipe("VOIDSASH", new ItemStack(ItemRegistry.ItemVoidwalkerSash, 1), 7, new AspectList()
				.add(Aspect.VOID, 56).add(Aspect.MAGIC, 40).add(Aspect.ARMOR, 66).add(Aspect.TRAVEL, 16)
				.add(Aspect.FLIGHT, 10), ItemApi.getItem("itemGirdleRunic", 0), new ItemStack[]{ ItemApi.getItem("itemEldritchObject", 3), ItemApi.getItem("itemResource", 16),
						new ItemStack(ItemRegistry.ItemMaterial), ItemApi.getItem("itemResource", 16), new ItemStack(ItemRegistry.ItemMaterial, 1, 1), ItemApi.getItem("itemResource", 16),
						new ItemStack(ItemRegistry.ItemMaterial), ItemApi.getItem("itemResource", 16), }));

		ResearchRegistry.recipes.put("ItemMaterial:5", ThaumcraftApi.addInfusionCraftingRecipe("CREATIONSHARD", new ItemStack(ItemRegistry.ItemMaterial, 1, 5), 8, TaintedMagicHelper.getPrimals(46)
				.add(Aspect.AURA, 35).add(Aspect.VOID, 22).add(Aspect.MAGIC, 56).add(Aspect.CRYSTAL, 38), new ItemStack(ConfigItems.itemShard, 1,
						6), new ItemStack[]{ new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 1),
								new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Items.nether_star), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 4),
								new ItemStack(ConfigItems.itemShard, 1, 5) }));

		ResearchRegistry.recipes
				.put("BlockWarpwoodSapling", ThaumcraftApi.addInfusionCraftingRecipe("WARPTREE", new ItemStack(BlockRegistry.BlockWarpwoodSapling), 3, new AspectList().add(Aspect.ELDRITCH, 8)
						.add(Aspect.TREE, 8).add(Aspect.DARKNESS, 8), new ItemStack(Blocks.sapling, 1, Short.MAX_VALUE), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 3),
								ItemApi.getItem("itemZombieBrain", 0), new ItemStack(ItemRegistry.ItemMaterial, 1, 3), ItemApi.getItem("itemZombieBrain", 0) }));

		ResearchRegistry.recipes.put("ItemWandRod:0", ThaumcraftApi.addInfusionCraftingRecipe("ROD_warpwood", new ItemStack(ItemRegistry.ItemWandRod), 8, new AspectList().add(Aspect.ELDRITCH, 65)
				.add(Aspect.DARKNESS, 30).add(Aspect.TREE, 12).add(Aspect.MAGIC, 45)
				.add(Aspect.AURA, 35), new ItemStack(BlockRegistry.BlockWarpwoodLog), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 10), new ItemStack(ItemRegistry.ItemMaterial),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 3), ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemZombieBrain", 0), new ItemStack(ItemRegistry.ItemMaterial),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 3), ItemApi.getItem("itemResource", 16) }));

		ResearchRegistry.recipes.put("ItemVoidFortressHelmet:goggles", ThaumcraftApi
				.addInfusionCraftingRecipe("HELMGOGGLES", new Object[]{ "goggles", new NBTTagByte((byte) 1) }, 5, new AspectList().add(Aspect.SENSES, 32).add(Aspect.AURA, 16)
						.add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemVoidFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.slime_ball), ItemApi.getItem("itemGoggles", 0) }));

		ResearchRegistry.recipes.put("ItemVoidFortressHelmet:mask0", ThaumcraftApi
				.addInfusionCraftingRecipe("MASKGRINNINGDEVIL", new Object[]{ "mask", new NBTTagInt(0) }, 8, new AspectList().add(Aspect.MIND, 64).add(Aspect.HEAL, 64)
						.add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemVoidFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 0), new ItemStack(Items.iron_ingot),
								new ItemStack(Items.leather), ItemApi.getBlock("blockCustomPlant", 2), ItemApi.getItem("itemZombieBrain", 0), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemVoidFortressHelmet:mask1", ThaumcraftApi.addInfusionCraftingRecipe("MASKANGRYGHOST", new Object[]{ "mask", new NBTTagInt(1) }, 8, new AspectList()
				.add(Aspect.ENTROPY, 64).add(Aspect.DEATH, 64).add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemVoidFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 15),
						new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.poisonous_potato), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemVoidFortressHelmet:mask2", ThaumcraftApi.addInfusionCraftingRecipe("MASKSIPPINGFIEND", new Object[]{ "mask", new NBTTagInt(2) }, 8, new AspectList()
				.add(Aspect.UNDEAD, 64).add(Aspect.LIFE, 64).add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemVoidFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 1),
						new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.ghast_tear), new ItemStack(Items.milk_bucket), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemVoidFortressHelmet", ThaumcraftApi.addInfusionCraftingRecipe("VOIDFORTRESS", new ItemStack(ItemRegistry.ItemVoidFortressHelmet), 6, new AspectList()
				.add(Aspect.METAL, 24).add(Aspect.ARMOR, 16).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 16), ItemApi.getItem("itemHelmetVoid", 0), new ItemStack[]{
						ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald) }));

		ResearchRegistry.recipes.put("ItemVoidFortressChestplate", ThaumcraftApi
				.addInfusionCraftingRecipe("VOIDFORTRESS", new ItemStack(ItemRegistry.ItemVoidFortressChestplate), 6, new AspectList().add(Aspect.METAL, 24).add(Aspect.ARMOR, 24).add(Aspect.MAGIC, 8)
						.add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 24), ItemApi.getItem("itemChestVoid", 0), new ItemStack[]{ ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16),
								ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather) }));

		ResearchRegistry.recipes.put("ItemVoidFortressLeggings", ThaumcraftApi
				.addInfusionCraftingRecipe("VOIDFORTRESS", new ItemStack(ItemRegistry.ItemVoidFortressLeggings), 6, new AspectList().add(Aspect.METAL, 24).add(Aspect.ARMOR, 20).add(Aspect.MAGIC, 8)
						.add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 20), ItemApi.getItem("itemLegsVoid", 0), new ItemStack[]{ ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16),
								ItemApi.getItem("itemResource", 16), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather) }));

		ResearchRegistry.recipes.put("ItemWarpedGoggles", ThaumcraftApi.addInfusionCraftingRecipe("WARPEDGOGGLES", new ItemStack(ItemRegistry.ItemWarpedGoggles), 3, new AspectList()
				.add(Aspect.ELDRITCH, 35).add(Aspect.DARKNESS, 12).add(Aspect.MAGIC, 16)
				.add(Aspect.ARMOR, 8), ItemApi.getItem("itemGoggles", 0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 9), new ItemStack(ConfigItems.itemWispEssence, 1, 32767) }));

		ResearchRegistry.recipes.put("ItemWandCap:0", ThaumcraftApi.addInfusionCraftingRecipe("CAP_shadowmetal", new ItemStack(ItemRegistry.ItemWandCap), 8, new AspectList().add(Aspect.ELDRITCH, 55)
				.add(Aspect.DARKNESS, 47).add(Aspect.MAGIC, 52).add(Aspect.METAL, 45)
				.add(Aspect.VOID, 55), ItemApi.getItem("itemWandCap", 7), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 3), new ItemStack(ItemRegistry.ItemMaterial),
						ItemApi.getItem("itemResource", 16), new ItemStack(ItemRegistry.ItemMaterial), new ItemStack(ItemRegistry.ItemMaterial, 1, 10), new ItemStack(ItemRegistry.ItemMaterial),
						ItemApi.getItem("itemResource", 16), new ItemStack(ItemRegistry.ItemMaterial) }));

		ResearchRegistry.recipes.put("ItemFocusTaintSwarm", ThaumcraftApi.addInfusionCraftingRecipe("TAINTFOCUS", new ItemStack(ItemRegistry.ItemFocusTaintSwarm), 4, new AspectList()
				.add(Aspect.TAINT, 45).add(Aspect.LIFE, 24).add(Aspect.MOTION, 24).add(Aspect.MAGIC, 16)
				.add(Aspect.DEATH, 37), new ItemStack(ItemRegistry.ItemFocusVisShard), new ItemStack[]{ ItemApi.getItem("itemResource", 11), new ItemStack(ItemRegistry.ItemMaterial, 1, 4),
						ItemApi.getItem("itemBottleTaint", 0), new ItemStack(ItemRegistry.ItemMaterial, 1, 4), ItemApi.getItem("itemResource", 11), new ItemStack(ItemRegistry.ItemMaterial, 1, 4),
						ItemApi.getItem("itemBottleTaint", 0), new ItemStack(ItemRegistry.ItemMaterial, 1, 4) }));

		ResearchRegistry.recipes.put("ItemFocusDarkMatter", ThaumcraftApi.addInfusionCraftingRecipe("ELDRITCHFOCUS", new ItemStack(ItemRegistry.ItemFocusDarkMatter), 6, new AspectList()
				.add(Aspect.ELDRITCH, 64).add(Aspect.DARKNESS, 32).add(Aspect.MAGIC, 32).add(Aspect.DEATH, 32)
				.add(Aspect.VOID, 32), new ItemStack(ConfigItems.itemFocusPortableHole), new ItemStack[]{ new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
						new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ItemRegistry.ItemMaterial, 1, 3), new ItemStack(ConfigItems.itemBucketDeath),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 3), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17),
						new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ItemRegistry.ItemMaterial, 1, 3), new ItemStack(ConfigItems.itemBucketDeath),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 3), new ItemStack(ConfigItems.itemResource, 1, 16) }));

		ResearchRegistry.recipes.put("ItemMaterial:8", ThaumcraftApi
				.addInfusionCraftingRecipe("KNIGHTROBES", new ItemStack(ItemRegistry.ItemMaterial, 9, 8), 2, new AspectList().add(Aspect.HUNGER, 4).add(Aspect.METAL, 8)
						.add(Aspect.MAGIC, 6), new ItemStack(Blocks.iron_block), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7),
								new ItemStack(Items.gold_nugget), new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7), new ItemStack(Items.gold_nugget),
								new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7), new ItemStack(Items.gold_nugget),
								new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7), new ItemStack(Items.gold_nugget) }));

		ResearchRegistry.recipes.put("ItemVoidwalkerBoots", ThaumcraftApi.addInfusionCraftingRecipe("VOIDWALKERBOOTS", new ItemStack(ItemRegistry.ItemVoidwalkerBoots), 8, new AspectList()
				.add(Aspect.DARKNESS, 42).add(Aspect.VOID, 56).add(Aspect.ELDRITCH, 38).add(Aspect.ARMOR, 60)
				.add(Aspect.TRAVEL, 45), ItemApi.getItem("itemBootsTraveller", 0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 11), ItemApi.getItem("itemResource", 16),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 1), new ItemStack(ItemRegistry.ItemMaterial), new ItemStack(ItemRegistry.ItemMaterial, 1, 1), ItemApi.getItem("itemResource", 16),
						ItemApi.getItem("itemEldritchObject", 0), ItemApi.getItem("itemResource", 16), new ItemStack(ItemRegistry.ItemMaterial, 1, 1), new ItemStack(ItemRegistry.ItemMaterial),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 1), ItemApi.getItem("itemResource", 16) }));

		ResearchRegistry.recipes.put("ItemSalis:0", ThaumcraftApi.addInfusionCraftingRecipe("SKYSALT", new ItemStack(ItemRegistry.ItemSalis, 3, 0), 7, new AspectList().add(Aspect.AURA, 10)
				.add(Aspect.WEATHER, 15).add(Aspect.WATER, 15), new ItemStack(ItemRegistry.ItemMaterial, 1,
						12), new ItemStack[]{ new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 0), new ItemStack(ConfigItems.itemNugget, 1, 5) }));

		ResearchRegistry.recipes.put("ItemSalis:1", ThaumcraftApi.addInfusionCraftingRecipe("TIMESALT", new ItemStack(ItemRegistry.ItemSalis, 3, 1), 7, new AspectList().add(Aspect.AURA, 10)
				.add(Aspect.LIGHT, 15).add(Aspect.DARKNESS, 15), new ItemStack(ItemRegistry.ItemMaterial, 1,
						12), new ItemStack[]{ new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1), new ItemStack(Items.gold_nugget) }));

		ResearchRegistry.recipes.put("ItemThaumicDisassembler", ThaumcraftApi.addInfusionCraftingRecipe("THAUMICDISASSEMBLER", new ItemStack(ItemRegistry.ItemThaumicDisassembler), 3, new AspectList()
				.add(Aspect.METAL, 22).add(Aspect.TOOL, 24).add(Aspect.WEAPON, 14).add(Aspect.MECHANISM, 12), new ItemStack(ItemRegistry.ItemMaterial, 1,
						6), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 11), ItemApi.getItem("itemPickVoid", 0), ItemApi.getItem("itemResource", 2),
								ItemApi.getItem("itemShovelVoid", 0), ItemApi.getItem("itemResource", 2), ItemApi.getItem("itemSwordVoid", 0), ItemApi.getItem("itemResource", 2),
								ItemApi.getItem("itemAxeVoid", 0) }));

		ResearchRegistry.recipes.put("ItemShadowFortressHelmet:goggles", ThaumcraftApi
				.addInfusionCraftingRecipe("HELMGOGGLES", new Object[]{ "goggles", new NBTTagByte((byte) 1) }, 5, new AspectList().add(Aspect.SENSES, 32).add(Aspect.AURA, 16)
						.add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemShadowFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.slime_ball), ItemApi.getItem("itemGoggles", 0) }));

		ResearchRegistry.recipes.put("ItemShadowFortressHelmet:mask0", ThaumcraftApi
				.addInfusionCraftingRecipe("MASKGRINNINGDEVIL", new Object[]{ "mask", new NBTTagInt(0) }, 8, new AspectList().add(Aspect.MIND, 64).add(Aspect.HEAL, 64)
						.add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemShadowFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 0), new ItemStack(Items.iron_ingot),
								new ItemStack(Items.leather), ItemApi.getBlock("blockCustomPlant", 2), ItemApi.getItem("itemZombieBrain", 0), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemShadowFortressHelmet:mask1", ThaumcraftApi.addInfusionCraftingRecipe("MASKANGRYGHOST", new Object[]{ "mask", new NBTTagInt(1) }, 8, new AspectList()
				.add(Aspect.ENTROPY, 64).add(Aspect.DEATH, 64).add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemShadowFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 15),
						new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.poisonous_potato), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemShadowFortressHelmet:mask2", ThaumcraftApi.addInfusionCraftingRecipe("MASKSIPPINGFIEND", new Object[]{ "mask", new NBTTagInt(2) }, 8, new AspectList()
				.add(Aspect.UNDEAD, 64).add(Aspect.LIFE, 64).add(Aspect.ARMOR, 16), new ItemStack(ItemRegistry.ItemShadowFortressHelmet, 1, 32767), new ItemStack[]{ new ItemStack(Items.dye, 1, 1),
						new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.ghast_tear), new ItemStack(Items.milk_bucket), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemShadowFortressHelmet", ThaumcraftApi
				.addInfusionCraftingRecipe("SHADOWFORTRESSARMOR", new ItemStack(ItemRegistry.ItemShadowFortressHelmet), 7, new AspectList().add(Aspect.METAL, 28).add(Aspect.ARMOR, 20)
						.add(Aspect.MAGIC, 12).add(Aspect.DARKNESS, 30).add(Aspect.VOID, 22), ItemApi.getItem("itemHelmetVoid", 0), new ItemStack[]{ new ItemStack(BlockRegistry.BlockShadowmetal),
								new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald) }));

		ResearchRegistry.recipes
				.put("ItemShadowFortressChestplate", ThaumcraftApi.addInfusionCraftingRecipe("SHADOWFORTRESSARMOR", new ItemStack(ItemRegistry.ItemShadowFortressChestplate), 7, new AspectList()
						.add(Aspect.METAL, 38).add(Aspect.ARMOR, 26).add(Aspect.MAGIC, 18).add(Aspect.DARKNESS, 34)
						.add(Aspect.VOID, 26), ItemApi.getItem("itemChestVoid", 0), new ItemStack[]{ new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(BlockRegistry.BlockShadowmetal),
								new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather) }));

		ResearchRegistry.recipes.put("ItemShadowFortressLeggings", ThaumcraftApi
				.addInfusionCraftingRecipe("SHADOWFORTRESSARMOR", new ItemStack(ItemRegistry.ItemShadowFortressLeggings), 7, new AspectList().add(Aspect.METAL, 32).add(Aspect.ARMOR, 24)
						.add(Aspect.MAGIC, 16).add(Aspect.DARKNESS, 32).add(Aspect.VOID, 24), ItemApi.getItem("itemLegsVoid", 0), new ItemStack[]{ new ItemStack(BlockRegistry.BlockShadowmetal),
								new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(BlockRegistry.BlockShadowmetal), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather) }));

		ResearchRegistry.recipes.put("ItemKatana:0", ThaumcraftApi
				.addInfusionCraftingRecipe("THAUMIUMKATANA", new ItemStack(ItemRegistry.ItemKatana, 1, 0), 3, new AspectList().add(Aspect.METAL, 42).add(Aspect.WEAPON, 45)
						.add(Aspect.MAGIC, 22), new ItemStack(ConfigItems.itemSwordThaumium), new ItemStack[]{ new ItemStack(Blocks.obsidian), new ItemStack(ConfigItems.itemResource, 1, 2),
								new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald), new ItemStack(Items.iron_ingot), new ItemStack(ConfigItems.itemResource, 1, 2),
								new ItemStack(Blocks.obsidian), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald),
								new ItemStack(Items.iron_ingot), new ItemStack(ConfigItems.itemResource, 1, 2) }));

		ResearchRegistry.recipes.put("ItemKatana:1", ThaumcraftApi.addInfusionCraftingRecipe("VOIDMETALKATANA", new ItemStack(ItemRegistry.ItemKatana, 1, 1), 6, new AspectList().add(Aspect.METAL, 52)
				.add(Aspect.WEAPON, 55).add(Aspect.MAGIC, 32).add(Aspect.VOID, 46)
				.add(Aspect.ELDRITCH, 12), new ItemStack(ConfigItems.itemSwordVoid), new ItemStack[]{ new ItemStack(Blocks.obsidian), new ItemStack(ConfigItems.itemResource, 1, 16),
						new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald), new ItemStack(Items.iron_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Blocks.obsidian),
						new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald), new ItemStack(Items.iron_ingot),
						new ItemStack(ConfigItems.itemResource, 1, 16) }));

		ResearchRegistry.recipes.put("ItemKatana:2", ThaumcraftApi.addInfusionCraftingRecipe("SHADOWMETALKATANA", new ItemStack(ItemRegistry.ItemKatana, 1, 2), 7, new AspectList()
				.add(Aspect.METAL, 62).add(Aspect.WEAPON, 65).add(Aspect.MAGIC, 42).add(Aspect.VOID, 16)
				.add(Aspect.DARKNESS, 46), new ItemStack(ItemRegistry.ItemShadowmetalSword), new ItemStack[]{ new ItemStack(Blocks.obsidian), new ItemStack(ItemRegistry.ItemMaterial, 1, 0),
						new ItemStack(Items.iron_ingot), new ItemStack(Items.emerald), new ItemStack(Items.iron_ingot), new ItemStack(ItemRegistry.ItemMaterial, 1, 0), new ItemStack(Blocks.obsidian),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 0), new ItemStack(Items.iron_ingot), new ItemStack(Items.emerald), new ItemStack(Items.iron_ingot),
						new ItemStack(ItemRegistry.ItemMaterial, 1, 0) }));

		ResearchRegistry.recipes.put("ItemFlyteCharm", ThaumcraftApi.addInfusionCraftingRecipe("FLYTECHARM", new ItemStack(ItemRegistry.ItemFlyteCharm, 1, 0), 7, new AspectList()
				.add(Aspect.FLIGHT, 86).add(Aspect.AURA, 56).add(Aspect.SENSES, 25).add(Aspect.MAGIC, 48).add(Aspect.ENERGY, 65), new ItemStack(ConfigItems.itemPrimalArrow, 1,
						0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemMaterial, 1, 10), new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemShard, 1, 0),
								new ItemStack(Items.iron_ingot), new ItemStack(ItemRegistry.ItemMaterial, 1, 5), new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemShard, 1, 0),
								new ItemStack(Items.iron_ingot) }));
		/**
		 * The ItemKatanaThaumium recipes are for the thaumonomicon
		 */
		ResearchRegistry.recipes.put("ItemKatanaThaumium:inscription0", ThaumcraftApi.addInfusionCraftingRecipe("INSCRIPTIONFIRE", new Object[]{ "inscription", new NBTTagInt(0) }, 8, new AspectList()
				.add(Aspect.FIRE, 64).add(Aspect.LIGHT, 64).add(Aspect.METAL, 16), new ItemStack(ItemRegistry.ItemKatana, 1, 0), new ItemStack[]{ ItemApi.getItem("itemFocusFire", 0),
						new ItemStack(Items.coal), new ItemStack(Blocks.netherrack), new ItemStack(Items.fire_charge), new ItemStack(Items.blaze_powder), ItemApi.getItem("itemResource", 1) }));

		ResearchRegistry.recipes.put("ItemKatanaThaumium:inscription1", ThaumcraftApi.addInfusionCraftingRecipe("INSCRIPTIONTAINT", new Object[]{ "inscription", new NBTTagInt(1) }, 8, new AspectList()
				.add(Aspect.TAINT, 64).add(Aspect.FLESH, 64)
				.add(Aspect.METAL, 16), new ItemStack(ItemRegistry.ItemKatana, 1, 0), new ItemStack[]{ new ItemStack(ItemRegistry.ItemFocusTaintSwarm), new ItemStack(ItemRegistry.ItemMaterial, 1, 4),
						ItemApi.getBlock("blockJar", 1), ItemApi.getBlock("blockCustomPlant", 5), ItemApi.getItem("itemBottleTaint", 0), ItemApi.getItem("itemResource", 11) }));

		ResearchRegistry.recipes.put("ItemKatanaThaumium:inscription2", ThaumcraftApi
				.addInfusionCraftingRecipe("INSCRIPTIONUNDEAD", new Object[]{ "inscription", new NBTTagInt(2) }, 8, new AspectList().add(Aspect.HEAL, 64).add(Aspect.UNDEAD, 64)
						.add(Aspect.METAL, 16), new ItemStack(ItemRegistry.ItemKatana, 1, 0), new ItemStack[]{ new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.bone),
								new ItemStack(Items.rotten_flesh), new ItemStack(Items.fermented_spider_eye), new ItemStack(ConfigItems.itemFocusPech), new ItemStack(ConfigItems.itemBathSalts) }));

		ResearchRegistry.recipes.put("ItemKatana:inscription0", ThaumcraftApi.addInfusionCraftingRecipe("INSCRIPTIONFIRE", new Object[]{ "inscription", new NBTTagInt(0) }, 8, new AspectList()
				.add(Aspect.FIRE, 64).add(Aspect.ENERGY, 64).add(Aspect.MAGIC, 16), new ItemStack(ItemRegistry.ItemKatana, 1, 32767), new ItemStack[]{ ItemApi.getItem("itemFocusFire", 0),
						new ItemStack(Items.coal), new ItemStack(Blocks.netherrack), new ItemStack(Items.fire_charge), new ItemStack(Items.blaze_powder), ItemApi.getItem("itemResource", 1) }));

		ResearchRegistry.recipes.put("ItemKatana:inscription1", ThaumcraftApi.addInfusionCraftingRecipe("INSCRIPTIONTAINT", new Object[]{ "inscription", new NBTTagInt(1) }, 8, new AspectList()
				.add(Aspect.TAINT, 64).add(Aspect.POISON, 64).add(Aspect.MAGIC, 16), new ItemStack(ItemRegistry.ItemKatana, 1,
						32767), new ItemStack[]{ new ItemStack(ItemRegistry.ItemFocusTaintSwarm), new ItemStack(ItemRegistry.ItemMaterial, 1, 4), ItemApi.getBlock("blockJar", 1),
								ItemApi.getBlock("blockCustomPlant", 5), ItemApi.getItem("itemBottleTaint", 0), ItemApi.getItem("itemResource", 11) }));

		ResearchRegistry.recipes.put("ItemKatana:inscription2", ThaumcraftApi
				.addInfusionCraftingRecipe("INSCRIPTIONUNDEAD", new Object[]{ "inscription", new NBTTagInt(2) }, 8, new AspectList().add(Aspect.HEAL, 64).add(Aspect.UNDEAD, 64)
						.add(Aspect.METAL, 16), new ItemStack(ItemRegistry.ItemKatana, 1, 32767), new ItemStack[]{ new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.bone),
								new ItemStack(Items.rotten_flesh), new ItemStack(Items.fermented_spider_eye), new ItemStack(ConfigItems.itemFocusPech), new ItemStack(ConfigItems.itemBathSalts) }));

		ResearchRegistry.recipes.put("ItemVoidmetalGoggles", ThaumcraftApi.addInfusionCraftingRecipe("VOIDGOGGLES", new ItemStack(ItemRegistry.ItemVoidmetalGoggles), 5, new AspectList()
				.add(Aspect.VOID, 40).add(Aspect.SENSES, 35).add(Aspect.ARMOR, 20), new ItemStack(ItemRegistry.ItemWarpedGoggles, 1,
						32767), new ItemStack[]{ new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 16) }));

		ResearchRegistry.recipes.put("ItemLumosRing", ThaumcraftApi
				.addInfusionCraftingRecipe("LUMOSRING", new ItemStack(ItemRegistry.ItemLumosRing), 1, new AspectList().add(Aspect.LIGHT, 35).add(Aspect.SENSES, 25).add(Aspect.AURA, 10), new ItemStack(
						ConfigItems.itemBaubleBlanks, 1, 1), new ItemStack[]{ new ItemStack(ItemRegistry.ItemFocusLumos), new ItemStack(ConfigItems.itemResource, 1, 6),
								new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(Items.gold_ingot) }));
	}

	/**
	 * Arcane worktable recipes
	 */
	public static void initArcane ()
	{
		ResearchRegistry.recipes.put("ItemMaterial:2", ThaumcraftApi
				.addShapelessArcaneCraftingRecipe("CRIMSONROBES", new ItemStack(ItemRegistry.ItemMaterial, 1, 2), new AspectList().add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5), new Object[]{
						new ItemStack(ItemRegistry.ItemMaterial.setContainerItem(ConfigItems.itemEssence), 1, 7), ItemApi.getItem("itemResource", 7), Items.gold_nugget }));

		ResearchRegistry.recipes.put("ItemMaterial:6", ThaumcraftApi.addArcaneCraftingRecipe("THAUMICDISASSEMBLER", new ItemStack(ItemRegistry.ItemMaterial, 1, 6), new AspectList()
				.add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50), new Object[]{ "BAB", "ACA", "BAB", Character.valueOf('A'), ItemApi.getItem("itemResource", 2), Character.valueOf('B'),
						ItemApi.getItem("itemResource", 16), Character.valueOf('C'), new ItemStack(Items.diamond) }));

		ResearchRegistry.recipes.put("ItemHelmetCultistRobe", ThaumcraftApi.addArcaneCraftingRecipe("CRIMSONROBES", ItemApi.getItem("itemHelmetCultistRobe", 0), new AspectList().add(Aspect.FIRE, 5)
				.add(Aspect.ENTROPY, 5), new Object[]{ "AAA", "A A", 'A', new ItemStack(ItemRegistry.ItemMaterial, 1, 2) }));

		ResearchRegistry.recipes.put("ItemChestCultistRobe", ThaumcraftApi
				.addArcaneCraftingRecipe("CRIMSONROBES", ItemApi.getItem("itemChestCultistRobe", 0), new AspectList().add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10), new Object[]{ "A A", "CAC", "ABA",
						'A', new ItemStack(ItemRegistry.ItemMaterial, 1, 2), 'B', new ItemStack(Items.gold_ingot), 'C', new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemLegsCultistRobe", ThaumcraftApi.addArcaneCraftingRecipe("CRIMSONROBES", ItemApi.getItem("itemLegsCultistRobe", 0), new AspectList().add(Aspect.FIRE, 8)
				.add(Aspect.ENTROPY, 8), new Object[]{ "ABA", "ACA", "A A", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 2), Character.valueOf('B'),
						new ItemStack(Items.gold_ingot), Character.valueOf('C'), new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemBootsCultist", ThaumcraftApi.addArcaneCraftingRecipe("CRIMSONROBES", ItemApi.getItem("itemBootsCultist", 0), new AspectList().add(Aspect.FIRE, 5)
				.add(Aspect.ENTROPY, 5), new Object[]{ "A A", "A A", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 2) }));

		ResearchRegistry.recipes.put("ItemWandRod:1", ThaumcraftApi.addArcaneCraftingRecipe("ROD_warpwood_staff", new ItemStack(ItemRegistry.ItemWandRod, 1, 1), new AspectList().add(Aspect.ORDER, 120)
				.add(Aspect.WATER, 120).add(Aspect.EARTH, 120).add(Aspect.AIR, 120).add(Aspect.FIRE, 120).add(Aspect.ENTROPY, 120), new Object[]{ "  A", " B ", "B  ", Character.valueOf('A'),
						new ItemStack(ConfigItems.itemEldritchObject, 1, 3), Character.valueOf('B'), new ItemStack(ItemRegistry.ItemWandRod, 1, 0) }));

		ResearchRegistry.recipes.put("ItemHelmetCultistPlate", ThaumcraftApi
				.addArcaneCraftingRecipe("KNIGHTROBES", ItemApi.getItem("itemHelmetCultistPlate", 0), new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5), new Object[]{
						"ABA", "C C", 'A', new ItemStack(ItemRegistry.ItemMaterial, 1, 8), 'B', new ItemStack(Items.gold_ingot), 'C', new ItemStack(Items.iron_ingot) }));

		ResearchRegistry.recipes.put("ItemChestCultistPlate", ThaumcraftApi.addArcaneCraftingRecipe("KNIGHTROBES", ItemApi.getItem("itemChestCultistPlate", 0), new AspectList().add(Aspect.EARTH, 10)
				.add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10), new Object[]{ "A A", "ABA", "BBB", 'A', new ItemStack(Items.iron_ingot), 'B', new ItemStack(ItemRegistry.ItemMaterial, 1, 8) }));

		ResearchRegistry.recipes.put("ItemLegsCultistPlate", ThaumcraftApi
				.addArcaneCraftingRecipe("KNIGHTROBES", ItemApi.getItem("itemLegsCultistPlate", 0), new AspectList().add(Aspect.EARTH, 8).add(Aspect.FIRE, 8).add(Aspect.ENTROPY, 8), new Object[]{
						"ABA", "ACA", " C ", 'A', new ItemStack(Items.iron_ingot), 'B', new ItemStack(Items.gold_ingot), 'C', new ItemStack(ItemRegistry.ItemMaterial, 1, 8) }));

		ResearchRegistry.recipes.put("ItemHelmetCultistLeaderPlate", ThaumcraftApi.addArcaneCraftingRecipe("PRAETORARMOR", new ItemStack(ConfigItems.itemHelmetCultistLeaderPlate), new AspectList()
				.add(Aspect.ORDER, 15).add(Aspect.ENTROPY, 15).add(Aspect.FIRE, 15), new Object[]{ "BCB", "DAD", "BCB", 'A', new ItemStack(ConfigItems.itemHelmetCultistPlate), 'B',
						new ItemStack(Items.iron_ingot), 'C', new ItemStack(ItemRegistry.ItemMaterial, 1, 8), 'D', new ItemStack(Items.gold_ingot) }));

		ResearchRegistry.recipes.put("ItemChestCultistLeaderPlate", ThaumcraftApi.addArcaneCraftingRecipe("PRAETORARMOR", new ItemStack(ConfigItems.itemChestCultistLeaderPlate), new AspectList()
				.add(Aspect.ORDER, 25).add(Aspect.ENTROPY, 25).add(Aspect.FIRE, 25), new Object[]{ "BBB", "CAC", "CDC", 'A', new ItemStack(ConfigItems.itemChestCultistPlate), 'B',
						new ItemStack(Items.iron_ingot), 'C', new ItemStack(ItemRegistry.ItemMaterial, 1, 8), 'D', new ItemStack(Items.gold_ingot) }));

		ResearchRegistry.recipes.put("ItemLegsCultistLeaderPlate", ThaumcraftApi.addArcaneCraftingRecipe("PRAETORARMOR", new ItemStack(ConfigItems.itemLegsCultistLeaderPlate), new AspectList()
				.add(Aspect.ORDER, 20).add(Aspect.ENTROPY, 20).add(Aspect.FIRE, 20), new Object[]{ "CDC", "BAB", "BCB", 'A', new ItemStack(ConfigItems.itemLegsCultistPlate), 'B',
						new ItemStack(Items.iron_ingot), 'C', new ItemStack(ItemRegistry.ItemMaterial, 1, 8), 'D', new ItemStack(Items.gold_ingot) }));

		ResearchRegistry.recipes.put("ItemMaterial:1", ThaumcraftApi
				.addArcaneCraftingRecipe("SHADOWCLOTH", new ItemStack(ItemRegistry.ItemMaterial, 1, 1), new AspectList().add(Aspect.ORDER, 1).add(Aspect.WATER, 1).add(Aspect.EARTH, 1)
						.add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.ENTROPY, 1), new Object[]{ "CAC", "ABA", "CAC", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial),
								Character.valueOf('B'), ItemApi.getItem("itemResource", 7), Character.valueOf('C'), new ItemStack(ItemRegistry.ItemMaterial, 1, 9) }));

		ResearchRegistry.recipes.put("ItemMagicFunguar", ThaumcraftApi.addShapelessArcaneCraftingRecipe("MAGICFUNGUAR", new ItemStack(ItemRegistry.ItemMagicFunguar), TaintedMagicHelper
				.getPrimals(1), new Object[]{ new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5), new ItemStack(ConfigItems.itemShard, 1, 32767), Items.glowstone_dust }));

		ResearchRegistry.recipes
				.put("ItemHollowDagger", ThaumcraftApi.addArcaneCraftingRecipe("CRYSTALDAGGER", new ItemStack(ItemRegistry.ItemHollowDagger), new AspectList().add(Aspect.ENTROPY, 85), new Object[]{
						"  A", "EBD", "C  ", Character.valueOf('A'), new ItemStack(ConfigItems.itemWandRod, 1, 7), Character.valueOf('B'), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0),
						Character.valueOf('C'), Items.stick, Character.valueOf('D'), ConfigItems.itemNugget, Character.valueOf('E'), Items.string }));

		ResearchRegistry.recipes.put("ItemWandCap:1", ThaumcraftApi.addArcaneCraftingRecipe("CAP_cloth", new ItemStack(ItemRegistry.ItemWandCap, 1, 1), new AspectList().add(Aspect.EARTH, 10)
				.add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.ORDER, 10), new Object[]{ "AAA", "A A", Character.valueOf('A'), ItemApi.getItem("itemResource", 7) }));

		ResearchRegistry.recipes.put("ItemWandCap:2", ThaumcraftApi.addArcaneCraftingRecipe("CAP_crimsoncloth", new ItemStack(ItemRegistry.ItemWandCap, 1, 2), new AspectList().add(Aspect.FIRE, 25)
				.add(Aspect.ENTROPY, 25).add(Aspect.ORDER, 25), new Object[]{ "AAA", "ABA", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 2), Character.valueOf('B'),
						ItemApi.getItem("itemResource", 14) }));

		ResearchRegistry.recipes.put("ItemWandCap:3", ThaumcraftApi.addArcaneCraftingRecipe("CAP_shadowcloth", new ItemStack(ItemRegistry.ItemWandCap, 1, 3), new AspectList().add(Aspect.EARTH, 55)
				.add(Aspect.FIRE, 55).add(Aspect.ENTROPY, 55).add(Aspect.ORDER, 55), new Object[]{ "AAA", "ABA", Character.valueOf('A'), new ItemStack(ItemRegistry.ItemMaterial, 1, 1),
						Character.valueOf('B'), ItemApi.getItem("itemResource", 14) }));

		ResearchRegistry.recipes.put("ItemFocusVisShard", ThaumcraftApi.addArcaneCraftingRecipe("FOCUSSHARD", new ItemStack(ItemRegistry.ItemFocusVisShard), new AspectList().add(Aspect.AIR, 46)
				.add(Aspect.ENTROPY, 38).add(Aspect.ORDER, 22), new Object[]{ "BDC", "DAD", "CDB", 'A', new ItemStack(ConfigItems.itemResource, 1, 3), 'B', new ItemStack(ConfigItems.itemShard, 1, 6),
						'C', new ItemStack(ItemRegistry.ItemMaterial, 1, 4), 'D', new ItemStack(ConfigItems.itemWispEssence, 1, 32767) }));

		ResearchRegistry.recipes.put("ItemFocusLumos", ThaumcraftApi.addArcaneCraftingRecipe("FOCUSLUMOS", new ItemStack(ItemRegistry.ItemFocusLumos), new AspectList().add(Aspect.AIR, 52)
				.add(Aspect.FIRE, 56).add(Aspect.ORDER, 28), new Object[]{ "BDC", "DAD", "CDB", 'A', new ItemStack(ConfigItems.itemResource, 1, 3), 'B', new ItemStack(ConfigItems.itemResource, 1, 1),
						'C', new ItemStack(ConfigItems.itemShard, 1, 6), 'D', new ItemStack(Items.glowstone_dust) }));

		ResearchRegistry.recipes.put("ItemMaterial:10", ThaumcraftApi.addShapelessArcaneCraftingRecipe("PRIMPEARL", new ItemStack(ItemRegistry.ItemMaterial, 3, 10), new AspectList()
				.add(Aspect.ENTROPY, 25), new ItemStack(ConfigItems.itemEldritchObject, 1, 3)));

		ResearchRegistry.recipes.put("ItemMaterial:11", ThaumcraftApi.addShapelessArcaneCraftingRecipe("PRIMPEARL", new ItemStack(ItemRegistry.ItemMaterial, 3, 11), new AspectList()
				.add(Aspect.ENTROPY, 15), new ItemStack(ItemRegistry.ItemMaterial, 1, 10)));

		ResearchRegistry.recipes.put("ItemMaterial:12", ThaumcraftApi.addShapelessArcaneCraftingRecipe("CREATIONSHARD", new ItemStack(ItemRegistry.ItemMaterial, 9, 12), new AspectList()
				.add(Aspect.ENTROPY, 99), new ItemStack(ItemRegistry.ItemMaterial, 1, 5)));

		ResearchRegistry.recipes.put("ItemVoidsentBlood", ThaumcraftApi.addArcaneCraftingRecipe("VOIDSENTBLOOD", new ItemStack(ItemRegistry.ItemVoidsentBlood), new AspectList().add(Aspect.ORDER, 35)
				.add(Aspect.ENTROPY, 25), new Object[]{ "BDB", "CAC", "BCB", 'A', new ItemStack(ItemRegistry.ItemMaterial, 1, 7), 'B', new ItemStack(ConfigItems.itemResource, 1, 17), 'C',
						new ItemStack(ConfigItems.itemResource, 1, 14), 'D', new ItemStack(ItemRegistry.ItemMaterial, 1, 11) }));
	}

	public static void initCrucible ()
	{
		ResearchRegistry.recipes.put("ItemMaterial:3", ThaumcraftApi
				.addCrucibleRecipe("EVILSHARDS", new ItemStack(ItemRegistry.ItemMaterial, 1, 3), ItemApi.getItem("itemShard", 6), new AspectList().merge(Aspect.ELDRITCH, 4)));
		ResearchRegistry.recipes.put("ItemMaterial:4", ThaumcraftApi
				.addCrucibleRecipe("EVILSHARDS", new ItemStack(ItemRegistry.ItemMaterial, 1, 4), ItemApi.getItem("itemShard", 6), new AspectList().merge(Aspect.TAINT, 4)));
		ResearchRegistry.recipes.put("BlockShadowOre", ThaumcraftApi.addCrucibleRecipe("SHADOWMETAL", new ItemStack(BlockRegistry.BlockShadowOre), new ItemStack(Blocks.stone), new AspectList()
				.merge(Aspect.DARKNESS, 1).merge(Aspect.METAL, 3).merge(Aspect.MAGIC, 1)));
		ResearchRegistry.recipes.put("Vishroom_brown", ThaumcraftApi.addCrucibleRecipe("VISHROOMCRAFT", new ItemStack(ConfigBlocks.blockCustomPlant, 1,
				5), new ItemStack(Blocks.brown_mushroom), new AspectList().merge(Aspect.MAGIC, 3).merge(Aspect.POISON, 1)));
		ResearchRegistry.recipes.put("Vishroom_red", ThaumcraftApi.addCrucibleRecipe("VISHROOMCRAFT", new ItemStack(ConfigBlocks.blockCustomPlant, 1,
				5), new ItemStack(Blocks.red_mushroom), new AspectList().merge(Aspect.MAGIC, 3).merge(Aspect.POISON, 1)));
	}
}