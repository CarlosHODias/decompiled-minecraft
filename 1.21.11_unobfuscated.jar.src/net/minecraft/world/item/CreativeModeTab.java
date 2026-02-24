/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreativeModeTab
/*     */ {
/*  19 */   private static final Identifier DEFAULT_BACKGROUND = createTextureLocation("items");
/*     */   private final Component displayName;
/*  21 */   private Identifier backgroundTexture = DEFAULT_BACKGROUND;
/*     */   private boolean canScroll = true;
/*     */   private boolean showTitle = true;
/*     */   private boolean alignedRight = false;
/*     */   private final Row row;
/*     */   private final int column;
/*     */   private final Type type;
/*     */   private ItemStack iconItemStack;
/*  29 */   private Collection<ItemStack> displayItems = ItemStackLinkedSet.createTypeAndComponentsSet();
/*  30 */   private Set<ItemStack> displayItemsSearchTab = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   private final Supplier<ItemStack> iconGenerator;
/*     */   private final DisplayItemsGenerator displayItemsGenerator;
/*     */   
/*     */   private CreativeModeTab(Row row, int column, Type type, Component displayName, Supplier<ItemStack> iconGenerator, DisplayItemsGenerator displayItemsGenerator) {
/*  35 */     this.row = row;
/*  36 */     this.column = column;
/*  37 */     this.displayName = displayName;
/*  38 */     this.iconGenerator = iconGenerator;
/*  39 */     this.displayItemsGenerator = displayItemsGenerator;
/*  40 */     this.type = type;
/*     */   }
/*     */   
/*     */   public static Identifier createTextureLocation(String name) {
/*  44 */     return Identifier.withDefaultNamespace("textures/gui/container/creative_inventory/tab_" + name + ".png");
/*     */   }
/*     */   
/*     */   public static Builder builder(Row row, int column) {
/*  48 */     return new Builder(row, column);
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  52 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public ItemStack getIconItem() {
/*  56 */     if (this.iconItemStack == null) {
/*  57 */       this.iconItemStack = this.iconGenerator.get();
/*     */     }
/*  59 */     return this.iconItemStack;
/*     */   }
/*     */   
/*     */   public Identifier getBackgroundTexture() {
/*  63 */     return this.backgroundTexture;
/*     */   }
/*     */   
/*     */   public boolean showTitle() {
/*  67 */     return this.showTitle;
/*     */   }
/*     */   
/*     */   public boolean canScroll() {
/*  71 */     return this.canScroll;
/*     */   }
/*     */   
/*     */   public int column() {
/*  75 */     return this.column;
/*     */   }
/*     */   
/*     */   public Row row() {
/*  79 */     return this.row;
/*     */   }
/*     */   
/*     */   public boolean hasAnyItems() {
/*  83 */     return !this.displayItems.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean shouldDisplay() {
/*  87 */     return (this.type != Type.CATEGORY || hasAnyItems());
/*     */   }
/*     */   
/*     */   public boolean isAlignedRight() {
/*  91 */     return this.alignedRight;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  95 */     return this.type;
/*     */   }
/*     */   
/*     */   public void buildContents(ItemDisplayParameters parameters) {
/*  99 */     ItemDisplayBuilder displayList = new ItemDisplayBuilder(this, parameters.enabledFeatures);
/* 100 */     ResourceKey<CreativeModeTab> key = (ResourceKey<CreativeModeTab>)BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(this).orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + String.valueOf(this)));
/* 101 */     this.displayItemsGenerator.accept(parameters, displayList);
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.displayItems = displayList.tabContents;
/* 106 */     this.displayItemsSearchTab = displayList.searchTabContents;
/*     */   }
/*     */   
/*     */   public Collection<ItemStack> getDisplayItems() {
/* 110 */     return this.displayItems;
/*     */   }
/*     */   
/*     */   public Collection<ItemStack> getSearchTabDisplayItems() {
/* 114 */     return this.displayItemsSearchTab;
/*     */   }
/*     */   
/*     */   public boolean contains(ItemStack stack) {
/* 118 */     return this.displayItemsSearchTab.contains(stack);
/*     */   }
/*     */   public static final class ItemDisplayParameters extends Record { private final FeatureFlagSet enabledFeatures; private final boolean hasPermissions; private final HolderLookup.Provider holders;
/* 121 */     public ItemDisplayParameters(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) { this.enabledFeatures = enabledFeatures; this.hasPermissions = hasPermissions; this.holders = holders; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 121 */       //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters; } public FeatureFlagSet enabledFeatures() { return this.enabledFeatures; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/* 121 */       //   0	8	1	o	Ljava/lang/Object; } public boolean hasPermissions() { return this.hasPermissions; } public HolderLookup.Provider holders() { return this.holders; }
/*     */      public boolean needsUpdate(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/* 123 */       return (!this.enabledFeatures.equals(enabledFeatures) || this.hasPermissions != hasPermissions || this.holders != holders);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 133 */     CATEGORY,
/* 134 */     INVENTORY,
/* 135 */     HOTBAR,
/* 136 */     SEARCH;
/*     */   }
/*     */   
/*     */   public enum Row {
/* 140 */     TOP,
/* 141 */     BOTTOM; }
/*     */   
/*     */   public static class Builder {
/*     */     private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (parameters, output) -> {
/*     */       
/*     */       };
/*     */     private final CreativeModeTab.Row row;
/*     */     private final int column;
/* 149 */     private Component displayName = (Component)Component.empty();
/*     */     private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;
/* 151 */     private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;
/*     */     private boolean canScroll = true;
/*     */     private boolean showTitle = true;
/*     */     private boolean alignedRight = false;
/* 155 */     private CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;
/* 156 */     private Identifier backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;
/*     */     
/*     */     public Builder(CreativeModeTab.Row row, int column) {
/* 159 */       this.row = row;
/* 160 */       this.column = column;
/*     */     }
/*     */     
/*     */     public Builder title(Component displayName) {
/* 164 */       this.displayName = displayName;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder icon(Supplier<ItemStack> iconGenerator) {
/* 169 */       this.iconGenerator = iconGenerator;
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
/* 174 */       this.displayItemsGenerator = displayItemsGenerator;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder alignedRight() {
/* 179 */       this.alignedRight = true;
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hideTitle() {
/* 184 */       this.showTitle = false;
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder noScrollBar() {
/* 189 */       this.canScroll = false;
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder type(CreativeModeTab.Type type) {
/* 194 */       this.type = type;
/* 195 */       return this;
/*     */     }
/*     */     
/*     */     public Builder backgroundTexture(Identifier backgroundTexture) {
/* 199 */       this.backgroundTexture = backgroundTexture;
/* 200 */       return this;
/*     */     }
/*     */     
/*     */     public CreativeModeTab build() {
/* 204 */       if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR) {
/* 205 */         throw new IllegalStateException("Special tabs can't have display items");
/*     */       }
/*     */       
/* 208 */       CreativeModeTab tab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
/* 209 */       tab.alignedRight = this.alignedRight;
/* 210 */       tab.showTitle = this.showTitle;
/* 211 */       tab.canScroll = this.canScroll;
/* 212 */       tab.backgroundTexture = this.backgroundTexture;
/* 213 */       return tab;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ItemDisplayBuilder implements Output {
/* 218 */     public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 219 */     public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */     private final CreativeModeTab tab;
/*     */     private final FeatureFlagSet featureFlagSet;
/*     */     
/*     */     public ItemDisplayBuilder(CreativeModeTab tab, FeatureFlagSet featureFlagSet) {
/* 224 */       this.tab = tab;
/* 225 */       this.featureFlagSet = featureFlagSet;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
/* 230 */       if (stack.getCount() != 1) {
/* 231 */         throw new IllegalArgumentException("Stack size must be exactly 1");
/*     */       }
/*     */ 
/*     */       
/* 235 */       boolean foundDuplicateStack = (this.tabContents.contains(stack) && tabVisibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
/*     */       
/* 237 */       if (foundDuplicateStack) {
/* 238 */         throw new IllegalStateException("Accidentally adding the same item stack twice " + 
/* 239 */             stack.getDisplayName().getString() + " to a Creative Mode Tab: " + 
/*     */             
/* 241 */             this.tab.getDisplayName().getString());
/*     */       }
/*     */       
/* 244 */       if (stack.getItem().isEnabled(this.featureFlagSet))
/* 245 */         switch (tabVisibility.ordinal()) {
/*     */           case 0:
/* 247 */             this.tabContents.add(stack);
/* 248 */             this.searchTabContents.add(stack); break;
/*     */           case 1:
/* 250 */             this.tabContents.add(stack); break;
/* 251 */           case 2: this.searchTabContents.add(stack);
/*     */             break;
/*     */         }  
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum TabVisibility {
/* 258 */     PARENT_AND_SEARCH_TABS,
/* 259 */     PARENT_TAB_ONLY,
/* 260 */     SEARCH_TAB_ONLY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface Output
/*     */   {
/*     */     default void accept(ItemStack stack) {
/* 267 */       accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     default void accept(ItemLike item, CreativeModeTab.TabVisibility tabVisibility) {
/* 271 */       accept(new ItemStack(item), tabVisibility);
/*     */     }
/*     */     
/*     */     default void accept(ItemLike item) {
/* 275 */       accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     default void acceptAll(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
/* 279 */       stacks.forEach(stack -> accept(tabVisibility, tabVisibility));
/*     */     }
/*     */     
/*     */     default void acceptAll(Collection<ItemStack> stacks) {
/* 283 */       acceptAll(stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     void accept(ItemStack param1ItemStack, CreativeModeTab.TabVisibility param1TabVisibility);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface DisplayItemsGenerator {
/*     */     void accept(CreativeModeTab.ItemDisplayParameters param1ItemDisplayParameters, CreativeModeTab.Output param1Output);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/CreativeModeTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */