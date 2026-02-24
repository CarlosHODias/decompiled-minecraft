/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.inventory.MenuAccess;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MenuScreens
/*     */ {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static <T extends net.minecraft.world.inventory.AbstractContainerMenu> void create(MenuType<T> type, Minecraft minecraft, int containerId, Component title) {
/*  41 */     ScreenConstructor<T, ?> constructor = getConstructor(type);
/*  42 */     if (constructor == null) {
/*     */       
/*  44 */       LOGGER.warn("Failed to create screen for menu type: {}", BuiltInRegistries.MENU.getKey(type));
/*     */       return;
/*     */     } 
/*  47 */     constructor.fromPacket(title, type, minecraft, containerId);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends net.minecraft.world.inventory.AbstractContainerMenu> ScreenConstructor<T, ?> getConstructor(MenuType<T> type) {
/*  52 */     return (ScreenConstructor<T, ?>)SCREENS.get(type);
/*     */   }
/*     */   
/*     */   private static interface ScreenConstructor<T extends net.minecraft.world.inventory.AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
/*     */     default void fromPacket(Component title, MenuType<T> type, Minecraft minecraft, int containerId) {
/*  57 */       U screen = create((T)type.create(containerId, minecraft.player.getInventory()), minecraft.player.getInventory(), title);
/*     */       
/*  59 */       minecraft.player.containerMenu = ((MenuAccess)screen).getMenu();
/*  60 */       minecraft.setScreen((Screen)screen);
/*     */     }
/*     */     
/*     */     U create(T param1T, Inventory param1Inventory, Component param1Component);
/*     */   }
/*     */   
/*  66 */   private static final Map<MenuType<?>, ScreenConstructor<?, ?>> SCREENS = Maps.newHashMap();
/*     */   
/*     */   private static <M extends net.minecraft.world.inventory.AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ScreenConstructor<M, U> factory) {
/*  69 */     ScreenConstructor<?, ?> prev = SCREENS.put(type, factory);
/*  70 */     if (prev != null) {
/*  71 */       throw new IllegalStateException("Duplicate registration for " + String.valueOf(BuiltInRegistries.MENU.getKey(type)));
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  76 */     register(MenuType.GENERIC_9x1, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*  77 */     register(MenuType.GENERIC_9x2, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*  78 */     register(MenuType.GENERIC_9x3, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*  79 */     register(MenuType.GENERIC_9x4, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*  80 */     register(MenuType.GENERIC_9x5, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*  81 */     register(MenuType.GENERIC_9x6, net.minecraft.client.gui.screens.inventory.ContainerScreen::new);
/*     */     
/*  83 */     register(MenuType.GENERIC_3x3, net.minecraft.client.gui.screens.inventory.DispenserScreen::new);
/*  84 */     register(MenuType.CRAFTER_3x3, net.minecraft.client.gui.screens.inventory.CrafterScreen::new);
/*     */     
/*  86 */     register(MenuType.ANVIL, net.minecraft.client.gui.screens.inventory.AnvilScreen::new);
/*  87 */     register(MenuType.BEACON, net.minecraft.client.gui.screens.inventory.BeaconScreen::new);
/*  88 */     register(MenuType.BLAST_FURNACE, net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen::new);
/*  89 */     register(MenuType.BREWING_STAND, net.minecraft.client.gui.screens.inventory.BrewingStandScreen::new);
/*  90 */     register(MenuType.CRAFTING, net.minecraft.client.gui.screens.inventory.CraftingScreen::new);
/*  91 */     register(MenuType.ENCHANTMENT, net.minecraft.client.gui.screens.inventory.EnchantmentScreen::new);
/*  92 */     register(MenuType.FURNACE, net.minecraft.client.gui.screens.inventory.FurnaceScreen::new);
/*  93 */     register(MenuType.GRINDSTONE, net.minecraft.client.gui.screens.inventory.GrindstoneScreen::new);
/*  94 */     register(MenuType.HOPPER, net.minecraft.client.gui.screens.inventory.HopperScreen::new);
/*  95 */     register(MenuType.LECTERN, net.minecraft.client.gui.screens.inventory.LecternScreen::new);
/*  96 */     register(MenuType.LOOM, net.minecraft.client.gui.screens.inventory.LoomScreen::new);
/*  97 */     register(MenuType.MERCHANT, net.minecraft.client.gui.screens.inventory.MerchantScreen::new);
/*  98 */     register(MenuType.SHULKER_BOX, net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen::new);
/*  99 */     register(MenuType.SMITHING, net.minecraft.client.gui.screens.inventory.SmithingScreen::new);
/* 100 */     register(MenuType.SMOKER, net.minecraft.client.gui.screens.inventory.SmokerScreen::new);
/* 101 */     register(MenuType.CARTOGRAPHY_TABLE, net.minecraft.client.gui.screens.inventory.CartographyTableScreen::new);
/* 102 */     register(MenuType.STONECUTTER, net.minecraft.client.gui.screens.inventory.StonecutterScreen::new);
/*     */   }
/*     */   
/*     */   public static boolean selfTest() {
/*     */     boolean failed = false;
/* 107 */     for (MenuType<?> menuType : (Iterable<MenuType<?>>)BuiltInRegistries.MENU) {
/* 108 */       if (!SCREENS.containsKey(menuType)) {
/* 109 */         LOGGER.debug("Menu {} has no matching screen", BuiltInRegistries.MENU.getKey(menuType));
/* 110 */         failed = true;
/*     */       } 
/*     */     } 
/* 113 */     return failed;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/MenuScreens.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */