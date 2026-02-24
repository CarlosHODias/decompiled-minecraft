/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ 
/*     */ public class RecipeButton
/*     */   extends AbstractWidget {
/*  26 */   private static final Identifier SLOT_MANY_CRAFTABLE_SPRITE = Identifier.withDefaultNamespace("recipe_book/slot_many_craftable");
/*  27 */   private static final Identifier SLOT_CRAFTABLE_SPRITE = Identifier.withDefaultNamespace("recipe_book/slot_craftable");
/*  28 */   private static final Identifier SLOT_MANY_UNCRAFTABLE_SPRITE = Identifier.withDefaultNamespace("recipe_book/slot_many_uncraftable");
/*  29 */   private static final Identifier SLOT_UNCRAFTABLE_SPRITE = Identifier.withDefaultNamespace("recipe_book/slot_uncraftable");
/*     */   private static final float ANIMATION_TIME = 15.0F;
/*     */   private static final int BACKGROUND_SIZE = 25;
/*  32 */   private static final Component MORE_RECIPES_TOOLTIP = (Component)Component.translatable("gui.recipebook.moreRecipes");
/*     */   
/*  34 */   private RecipeCollection collection = RecipeCollection.EMPTY;
/*  35 */   private List<ResolvedEntry> selectedEntries = List.of();
/*     */   
/*     */   private boolean allRecipesHaveSameResultDisplay;
/*     */   private final SlotSelectTime slotSelectTime;
/*     */   private float animationTime;
/*     */   
/*     */   public RecipeButton(SlotSelectTime slotSelectTime) {
/*  42 */     super(0, 0, 25, 25, CommonComponents.EMPTY);
/*  43 */     this.slotSelectTime = slotSelectTime;
/*     */   }
/*     */   
/*     */   public void init(RecipeCollection collection, boolean isFiltering, RecipeBookPage page, ContextMap resolutionContext) {
/*  47 */     this.collection = collection;
/*     */     
/*  49 */     List<RecipeDisplayEntry> fittingRecipes = collection.getSelectedRecipes(isFiltering ? RecipeCollection.CraftableStatus.CRAFTABLE : RecipeCollection.CraftableStatus.ANY);
/*  50 */     this.selectedEntries = fittingRecipes.stream().map(entry -> new ResolvedEntry(entry.id(), entry.resultItems(resolutionContext))).toList();
/*  51 */     this.allRecipesHaveSameResultDisplay = allRecipesHaveSameResultDisplay(this.selectedEntries);
/*     */     
/*  53 */     Objects.requireNonNull(page.getRecipeBook()); List<RecipeDisplayId> newlyShownRecipes = fittingRecipes.stream().map(RecipeDisplayEntry::id).filter(page.getRecipeBook()::willHighlight).toList();
/*  54 */     if (!newlyShownRecipes.isEmpty()) {
/*  55 */       Objects.requireNonNull(page); newlyShownRecipes.forEach(page::recipeShown);
/*  56 */       this.animationTime = 15.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean allRecipesHaveSameResultDisplay(List<ResolvedEntry> entries) {
/*  61 */     Iterator<ItemStack> itemsIterator = entries.stream().flatMap(e -> e.displayItems().stream()).iterator();
/*  62 */     if (!itemsIterator.hasNext())
/*     */     {
/*  64 */       return true;
/*     */     }
/*     */     
/*  67 */     ItemStack firstItem = itemsIterator.next();
/*     */     
/*  69 */     while (itemsIterator.hasNext()) {
/*  70 */       ItemStack nextItem = itemsIterator.next();
/*     */       
/*  72 */       if (!ItemStack.isSameItemSameComponents(firstItem, nextItem)) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   public RecipeCollection getCollection() {
/*  80 */     return this.collection;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier sprite;
/*  86 */     if (this.collection.hasCraftable()) {
/*  87 */       if (hasMultipleRecipes()) {
/*  88 */         sprite = SLOT_MANY_CRAFTABLE_SPRITE;
/*     */       } else {
/*  90 */         sprite = SLOT_CRAFTABLE_SPRITE;
/*     */       }
/*     */     
/*  93 */     } else if (hasMultipleRecipes()) {
/*  94 */       sprite = SLOT_MANY_UNCRAFTABLE_SPRITE;
/*     */     } else {
/*  96 */       sprite = SLOT_UNCRAFTABLE_SPRITE;
/*     */     } 
/*     */ 
/*     */     
/* 100 */     boolean shouldAnimate = (this.animationTime > 0.0F);
/* 101 */     if (shouldAnimate) {
/* 102 */       float squeeze = 1.0F + 0.1F * (float)Math.sin((this.animationTime / 15.0F * 3.1415927F));
/*     */       
/* 104 */       graphics.pose().pushMatrix();
/* 105 */       graphics.pose().translate((getX() + 8), (getY() + 12));
/* 106 */       graphics.pose().scale(squeeze, squeeze);
/* 107 */       graphics.pose().translate(-(getX() + 8), -(getY() + 12));
/*     */       
/* 109 */       this.animationTime -= a;
/*     */     } 
/*     */     
/* 112 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/*     */     
/* 114 */     ItemStack currentItemStack = getDisplayStack();
/* 115 */     int offset = 4;
/*     */ 
/*     */ 
/*     */     
/* 119 */     if (hasMultipleRecipes() && this.allRecipesHaveSameResultDisplay) {
/* 120 */       graphics.renderItem(currentItemStack, getX() + offset + 1, getY() + offset + 1, 0);
/* 121 */       offset--;
/*     */     } 
/*     */     
/* 124 */     graphics.renderFakeItem(currentItemStack, getX() + offset, getY() + offset);
/*     */     
/* 126 */     if (shouldAnimate) {
/* 127 */       graphics.pose().popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasMultipleRecipes() {
/* 132 */     return (this.selectedEntries.size() > 1);
/*     */   }
/*     */   
/*     */   public boolean isOnlyOption() {
/* 136 */     return (this.selectedEntries.size() == 1);
/*     */   }
/*     */   
/*     */   public RecipeDisplayId getCurrentRecipe() {
/* 140 */     int index = this.slotSelectTime.currentIndex() % this.selectedEntries.size();
/* 141 */     return ((ResolvedEntry)this.selectedEntries.get(index)).id;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getDisplayStack() {
/* 146 */     int currentIndex = this.slotSelectTime.currentIndex();
/* 147 */     int entryCount = this.selectedEntries.size();
/* 148 */     int offsetIndex = currentIndex / entryCount;
/* 149 */     int entryIndex = currentIndex - entryCount * offsetIndex;
/* 150 */     return ((ResolvedEntry)this.selectedEntries.get(entryIndex)).selectItem(offsetIndex);
/*     */   }
/*     */   
/*     */   public List<Component> getTooltipText(ItemStack displayStack) {
/* 154 */     List<Component> texts = new ArrayList<>(Screen.getTooltipFromItem(Minecraft.getInstance(), displayStack));
/* 155 */     if (hasMultipleRecipes()) {
/* 156 */       texts.add(MORE_RECIPES_TOOLTIP);
/*     */     }
/* 158 */     return texts;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 163 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("narration.recipe", new Object[] { getDisplayStack().getHoverName() }));
/* 164 */     if (hasMultipleRecipes()) {
/* 165 */       output.add(NarratedElementType.USAGE, new Component[] {
/* 166 */             (Component)Component.translatable("narration.button.usage.hovered"), 
/* 167 */             (Component)Component.translatable("narration.recipe.usage.more")
/*     */           });
/*     */     } else {
/* 170 */       output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.button.usage.hovered"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 176 */     return 25;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
/* 181 */     return (buttonInfo.button() == 0 || buttonInfo.button() == 1);
/*     */   }
/*     */   private static final class ResolvedEntry extends Record { private final RecipeDisplayId id; private final List<ItemStack> displayItems;
/* 184 */     private ResolvedEntry(RecipeDisplayId id, List<ItemStack> displayItems) { this.id = id; this.displayItems = displayItems; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 184 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry; } public RecipeDisplayId id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeButton$ResolvedEntry;
/* 184 */       //   0	8	1	o	Ljava/lang/Object; } public List<ItemStack> displayItems() { return this.displayItems; }
/*     */      public ItemStack selectItem(int index) {
/* 186 */       if (this.displayItems.isEmpty()) {
/* 187 */         return ItemStack.EMPTY;
/*     */       }
/*     */       
/* 190 */       int offset = index % this.displayItems.size();
/* 191 */       return this.displayItems.get(offset);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/recipebook/RecipeButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */