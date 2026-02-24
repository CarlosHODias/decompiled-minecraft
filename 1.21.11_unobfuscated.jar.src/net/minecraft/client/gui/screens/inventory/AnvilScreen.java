/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AnvilMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class AnvilScreen extends ItemCombinerScreen<AnvilMenu> {
/*  20 */   private static final Identifier TEXT_FIELD_SPRITE = Identifier.withDefaultNamespace("container/anvil/text_field");
/*  21 */   private static final Identifier TEXT_FIELD_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/anvil/text_field_disabled");
/*  22 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
/*  23 */   private static final Identifier ANVIL_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/anvil.png");
/*  24 */   private static final Component TOO_EXPENSIVE_TEXT = (Component)Component.translatable("container.repair.expensive");
/*     */   private EditBox name;
/*     */   private final Player player;
/*     */   
/*     */   public AnvilScreen(AnvilMenu menu, Inventory inventory, Component title) {
/*  29 */     super(menu, inventory, title, ANVIL_LOCATION);
/*  30 */     this.player = inventory.player;
/*  31 */     this.titleLabelX = 60;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subInit() {
/*  36 */     int xo = (this.width - this.imageWidth) / 2;
/*  37 */     int yo = (this.height - this.imageHeight) / 2;
/*     */     
/*  39 */     this.name = new EditBox(this.font, xo + 62, yo + 24, 103, 12, (Component)Component.translatable("container.repair"));
/*  40 */     this.name.setCanLoseFocus(false);
/*  41 */     this.name.setTextColor(-1);
/*  42 */     this.name.setTextColorUneditable(-1);
/*  43 */     this.name.setInvertHighlightedTextColor(false);
/*  44 */     this.name.setBordered(false);
/*  45 */     this.name.setMaxLength(50);
/*  46 */     this.name.setResponder(this::onNameChanged);
/*  47 */     this.name.setValue("");
/*  48 */     addRenderableWidget((GuiEventListener)this.name);
/*  49 */     this.name.setEditable(this.menu.getSlot(0).hasItem());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void containerTick() {
/*  54 */     super.containerTick();
/*  55 */     this.minecraft.player.experienceDisplayStartTick = this.minecraft.player.tickCount;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  60 */     setInitialFocus((GuiEventListener)this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/*  65 */     String oldEdit = this.name.getValue();
/*  66 */     init(width, height);
/*  67 */     this.name.setValue(oldEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  72 */     if (event.isEscape()) {
/*  73 */       this.minecraft.player.closeContainer();
/*  74 */       return true;
/*     */     } 
/*     */     
/*  77 */     if (this.name.keyPressed(event) || this.name.canConsumeInput()) {
/*  78 */       return true;
/*     */     }
/*  80 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   private void onNameChanged(String name) {
/*  84 */     Slot slot = this.menu.getSlot(0);
/*  85 */     if (!slot.hasItem()) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     String newName = name;
/*  90 */     if (!slot.getItem().has(net.minecraft.core.component.DataComponents.CUSTOM_NAME) && newName.equals(slot.getItem().getHoverName().getString())) {
/*  91 */       newName = "";
/*     */     }
/*     */     
/*  94 */     if (this.menu.setItemName(newName)) {
/*  95 */       this.minecraft.player.connection.send((Packet)new ServerboundRenameItemPacket(newName));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/* 101 */     super.renderLabels(graphics, xm, ym);
/*     */     
/* 103 */     int cost = this.menu.getCost();
/* 104 */     if (cost > 0) {
/* 105 */       MutableComponent mutableComponent; int color = -8323296;
/*     */       
/* 107 */       if (cost >= 40 && !this.minecraft.player.hasInfiniteMaterials()) {
/* 108 */         Component line = TOO_EXPENSIVE_TEXT;
/* 109 */         color = -40864;
/* 110 */       } else if (!this.menu.getSlot(2).hasItem()) {
/* 111 */         Component line = null;
/*     */       } else {
/* 113 */         mutableComponent = Component.translatable("container.repair.cost", new Object[] { cost });
/* 114 */         if (!this.menu.getSlot(2).mayPickup(this.player)) {
/* 115 */           color = -40864;
/*     */         }
/*     */       } 
/*     */       
/* 119 */       if (mutableComponent != null) {
/* 120 */         int tx = this.imageWidth - 8 - this.font.width((FormattedText)mutableComponent) - 2;
/* 121 */         int ty = 69;
/* 122 */         graphics.fill(tx - 2, 67, this.imageWidth - 8, 79, 1325400064);
/* 123 */         graphics.drawString(this.font, (Component)mutableComponent, tx, 69, color);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 130 */     super.renderBg(graphics, a, xm, ym);
/* 131 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.menu.getSlot(0).hasItem() ? TEXT_FIELD_SPRITE : TEXT_FIELD_DISABLED_SPRITE, this.leftPos + 59, this.topPos + 20, 110, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderErrorIcon(GuiGraphics graphics, int xo, int yo) {
/* 136 */     if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
/* 137 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 99, yo + 45, 28, 21);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotChanged(net.minecraft.world.inventory.AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 143 */     if (slotIndex == 0) {
/* 144 */       this.name.setValue(itemStack.isEmpty() ? "" : itemStack.getHoverName().getString());
/* 145 */       this.name.setEditable(!itemStack.isEmpty());
/* 146 */       setFocused((GuiEventListener)this.name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AnvilScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */