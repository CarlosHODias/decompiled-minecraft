/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.book.BookModel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.EnchantmentMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ 
/*     */ public class EnchantmentScreen extends AbstractContainerScreen<EnchantmentMenu> {
/*  30 */   private static final Identifier[] ENABLED_LEVEL_SPRITES = new Identifier[] {
/*  31 */       Identifier.withDefaultNamespace("container/enchanting_table/level_1"), 
/*  32 */       Identifier.withDefaultNamespace("container/enchanting_table/level_2"), 
/*  33 */       Identifier.withDefaultNamespace("container/enchanting_table/level_3")
/*     */     };
/*  35 */   private static final Identifier[] DISABLED_LEVEL_SPRITES = new Identifier[] {
/*  36 */       Identifier.withDefaultNamespace("container/enchanting_table/level_1_disabled"), 
/*  37 */       Identifier.withDefaultNamespace("container/enchanting_table/level_2_disabled"), 
/*  38 */       Identifier.withDefaultNamespace("container/enchanting_table/level_3_disabled")
/*     */     };
/*  40 */   private static final Identifier ENCHANTMENT_SLOT_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_disabled");
/*  41 */   private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_highlighted");
/*  42 */   private static final Identifier ENCHANTMENT_SLOT_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot");
/*  43 */   private static final Identifier ENCHANTING_TABLE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/enchanting_table.png");
/*  44 */   private static final Identifier ENCHANTING_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/entity/enchanting_table_book.png");
/*     */   
/*  46 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private BookModel bookModel;
/*     */   
/*     */   public float flip;
/*     */   public float oFlip;
/*     */   public float flipT;
/*     */   public float flipA;
/*     */   public float open;
/*     */   public float oOpen;
/*  56 */   private ItemStack last = ItemStack.EMPTY;
/*     */   
/*     */   public EnchantmentScreen(EnchantmentMenu menu, Inventory inventory, Component title) {
/*  59 */     super(menu, inventory, title);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  64 */     super.init();
/*  65 */     this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerTick() {
/*  70 */     super.containerTick();
/*  71 */     this.minecraft.player.experienceDisplayStartTick = this.minecraft.player.tickCount;
/*  72 */     tickBook();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  77 */     int xo = (this.width - this.imageWidth) / 2;
/*  78 */     int yo = (this.height - this.imageHeight) / 2;
/*  79 */     for (int i = 0; i < 3; i++) {
/*  80 */       double xx = event.x() - (xo + 60);
/*  81 */       double yy = event.y() - (yo + 14 + 19 * i);
/*  82 */       if (xx >= 0.0D && yy >= 0.0D && xx < 108.0D && yy < 19.0D && 
/*  83 */         this.menu.clickMenuButton((Player)this.minecraft.player, i)) {
/*  84 */         this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
/*  85 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float ignored, int xm, int ym) {
/*  94 */     int xo = (this.width - this.imageWidth) / 2;
/*  95 */     int yo = (this.height - this.imageHeight) / 2;
/*  96 */     graphics.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*  97 */     renderBook(graphics, xo, yo);
/*     */     
/*  99 */     EnchantmentNames.getInstance().initSeed(this.menu.getEnchantmentSeed());
/*     */     
/* 101 */     int goldCount = this.menu.getGoldCount();
/* 102 */     for (int i = 0; i < 3; i++) {
/* 103 */       int leftPos = xo + 60;
/* 104 */       int leftPosText = leftPos + 20;
/*     */       
/* 106 */       int cost = this.menu.costs[i];
/* 107 */       if (cost == 0) {
/* 108 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
/*     */       } else {
/*     */         
/* 111 */         String costText = "" + cost;
/* 112 */         int textWidth = 86 - this.font.width(costText);
/* 113 */         FormattedText message = EnchantmentNames.getInstance().getRandomName(this.font, textWidth);
/* 114 */         int col = -9937334;
/*     */         
/* 116 */         if ((goldCount < i + 1 || this.minecraft.player.experienceLevel < cost) && !this.minecraft.player.hasInfiniteMaterials()) {
/* 117 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
/* 118 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_LEVEL_SPRITES[i], leftPos + 1, yo + 15 + 19 * i, 16, 16);
/* 119 */           graphics.drawWordWrap(this.font, message, leftPosText, yo + 16 + 19 * i, textWidth, ARGB.opaque((col & 0xFEFEFE) >> 1), false);
/* 120 */           col = -12550384;
/*     */         } else {
/* 122 */           int xx = xm - xo + 60;
/* 123 */           int yy = ym - yo + 14 + 19 * i;
/* 124 */           if (xx >= 0 && yy >= 0 && xx < 108 && yy < 19) {
/* 125 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
/* 126 */             graphics.requestCursor(CursorTypes.POINTING_HAND);
/* 127 */             col = -128;
/*     */           } else {
/* 129 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
/*     */           } 
/* 131 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENABLED_LEVEL_SPRITES[i], leftPos + 1, yo + 15 + 19 * i, 16, 16);
/* 132 */           graphics.drawWordWrap(this.font, message, leftPosText, yo + 16 + 19 * i, textWidth, col, false);
/* 133 */           col = -8323296;
/*     */         } 
/* 135 */         graphics.drawString(this.font, costText, leftPosText + 86 - this.font.width(costText), yo + 16 + 19 * i + 7, col);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void renderBook(GuiGraphics graphics, int left, int top) {
/* 140 */     float a = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
/* 141 */     float open = Mth.lerp(a, this.oOpen, this.open);
/* 142 */     float flip = Mth.lerp(a, this.oFlip, this.flip);
/*     */     
/* 144 */     int x0 = left + 14;
/* 145 */     int y0 = top + 14;
/* 146 */     int x1 = x0 + 38;
/* 147 */     int y1 = y0 + 31;
/* 148 */     graphics.submitBookModelRenderState(this.bookModel, ENCHANTING_BOOK_LOCATION, 40.0F, open, flip, x0, y0, x1, y1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float ignored) {
/* 153 */     float a = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
/* 154 */     super.render(graphics, mouseX, mouseY, a);
/* 155 */     renderTooltip(graphics, mouseX, mouseY);
/*     */     
/* 157 */     boolean infiniteMaterials = this.minecraft.player.hasInfiniteMaterials();
/* 158 */     int gold = this.menu.getGoldCount();
/*     */     
/* 160 */     for (int i = 0; i < 3; i++) {
/* 161 */       int minLevel = this.menu.costs[i];
/*     */       
/* 163 */       Optional<Holder.Reference<Enchantment>> enchant = this.minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(this.menu.enchantClue[i]);
/* 164 */       if (!enchant.isEmpty()) {
/*     */ 
/*     */         
/* 167 */         int enchantLevel = this.menu.levelClue[i];
/*     */         
/* 169 */         int cost = i + 1;
/* 170 */         if (isHovering(60, 14 + 19 * i, 108, 17, mouseX, mouseY) && minLevel > 0 && enchantLevel >= 0) {
/* 171 */           List<Component> texts = Lists.newArrayList();
/*     */           
/* 173 */           texts.add(Component.translatable("container.enchant.clue", new Object[] { Enchantment.getFullname((Holder)enchant.get(), enchantLevel) }).withStyle(ChatFormatting.WHITE));
/*     */           
/* 175 */           if (!infiniteMaterials) {
/* 176 */             texts.add(CommonComponents.EMPTY);
/* 177 */             if (this.minecraft.player.experienceLevel < minLevel) {
/* 178 */               texts.add(Component.translatable("container.enchant.level.requirement", new Object[] { this.menu.costs[i] }).withStyle(ChatFormatting.RED));
/*     */             } else {
/*     */               MutableComponent lapisCost, levelCost;
/* 181 */               if (cost == 1) {
/* 182 */                 lapisCost = Component.translatable("container.enchant.lapis.one");
/*     */               } else {
/* 184 */                 lapisCost = Component.translatable("container.enchant.lapis.many", new Object[] { cost });
/*     */               } 
/* 186 */               texts.add(lapisCost.withStyle((gold >= cost) ? ChatFormatting.GRAY : ChatFormatting.RED));
/*     */ 
/*     */               
/* 189 */               if (cost == 1) {
/* 190 */                 levelCost = Component.translatable("container.enchant.level.one");
/*     */               } else {
/* 192 */                 levelCost = Component.translatable("container.enchant.level.many", new Object[] { cost });
/*     */               } 
/* 194 */               texts.add(levelCost.withStyle(ChatFormatting.GRAY));
/*     */             } 
/*     */           } 
/*     */           
/* 198 */           graphics.setComponentTooltipForNextFrame(this.font, texts, mouseX, mouseY);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void tickBook() {
/* 205 */     ItemStack current = this.menu.getSlot(0).getItem();
/*     */     
/* 207 */     if (!ItemStack.matches(current, this.last)) {
/* 208 */       this.last = current;
/*     */       do {
/* 210 */         this.flipT += (this.random.nextInt(4) - this.random.nextInt(4));
/* 211 */       } while (this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
/*     */     } 
/*     */     
/* 214 */     this.oFlip = this.flip;
/* 215 */     this.oOpen = this.open;
/*     */     
/*     */     boolean shouldBeOpen = false;
/* 218 */     for (int i = 0; i < 3; i++) {
/* 219 */       if (this.menu.costs[i] != 0) {
/* 220 */         shouldBeOpen = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 225 */     if (shouldBeOpen) {
/* 226 */       this.open += 0.2F;
/*     */     } else {
/* 228 */       this.open -= 0.2F;
/*     */     } 
/* 230 */     this.open = Mth.clamp(this.open, 0.0F, 1.0F);
/*     */     
/* 232 */     float diff = (this.flipT - this.flip) * 0.4F;
/* 233 */     float max = 0.2F;
/* 234 */     diff = Mth.clamp(diff, -0.2F, 0.2F);
/* 235 */     this.flipA += (diff - this.flipA) * 0.9F;
/*     */     
/* 237 */     this.flip += this.flipA;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/EnchantmentScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */