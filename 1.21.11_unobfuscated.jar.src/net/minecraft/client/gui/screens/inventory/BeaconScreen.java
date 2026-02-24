/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerListener;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*     */ 
/*     */ public class BeaconScreen extends AbstractContainerScreen<BeaconMenu> {
/*  33 */   private static final Identifier BEACON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/beacon.png");
/*  34 */   private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_disabled");
/*  35 */   private static final Identifier BUTTON_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_selected");
/*  36 */   private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_highlighted");
/*  37 */   private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("container/beacon/button");
/*  38 */   private static final Identifier CONFIRM_SPRITE = Identifier.withDefaultNamespace("container/beacon/confirm");
/*  39 */   private static final Identifier CANCEL_SPRITE = Identifier.withDefaultNamespace("container/beacon/cancel");
/*  40 */   private static final Component PRIMARY_EFFECT_LABEL = (Component)Component.translatable("block.minecraft.beacon.primary");
/*  41 */   private static final Component SECONDARY_EFFECT_LABEL = (Component)Component.translatable("block.minecraft.beacon.secondary");
/*     */   
/*  43 */   private final List<BeaconButton> beaconButtons = Lists.newArrayList();
/*     */   private Holder<MobEffect> primary;
/*     */   private Holder<MobEffect> secondary;
/*     */   
/*     */   public BeaconScreen(final BeaconMenu menu, Inventory inventory, Component title) {
/*  48 */     super(menu, inventory, title);
/*     */     
/*  50 */     this.imageWidth = 230;
/*  51 */     this.imageHeight = 219;
/*     */     
/*  53 */     menu.addSlotListener(new ContainerListener()
/*     */         {
/*     */           public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void dataChanged(AbstractContainerMenu container, int id, int value) {
/*  60 */             BeaconScreen.this.primary = menu.getPrimaryEffect();
/*  61 */             BeaconScreen.this.secondary = menu.getSecondaryEffect();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private <T extends net.minecraft.client.gui.components.AbstractWidget & BeaconButton> void addBeaconButton(T beaconButton) {
/*  67 */     addRenderableWidget((GuiEventListener)beaconButton);
/*  68 */     this.beaconButtons.add((BeaconButton)beaconButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  73 */     super.init();
/*     */     
/*  75 */     this.beaconButtons.clear();
/*     */     int tier;
/*  77 */     for (tier = 0; tier <= 2; tier++) {
/*  78 */       int i = ((List)BeaconBlockEntity.BEACON_EFFECTS.get(tier)).size();
/*  79 */       int j = i * 22 + (i - 1) * 2;
/*     */       
/*  81 */       for (int k = 0; k < i; k++) {
/*  82 */         Holder<MobEffect> effect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(tier)).get(k);
/*  83 */         BeaconPowerButton beaconPowerButton1 = new BeaconPowerButton(this.leftPos + 76 + k * 24 - j / 2, this.topPos + 22 + tier * 25, effect, true, tier);
/*  84 */         beaconPowerButton1.active = false;
/*  85 */         addBeaconButton(beaconPowerButton1);
/*     */       } 
/*     */     } 
/*  88 */     tier = 3;
/*     */     
/*  90 */     int count = ((List)BeaconBlockEntity.BEACON_EFFECTS.get(3)).size() + 1;
/*  91 */     int totalWidth = count * 22 + (count - 1) * 2;
/*     */     
/*  93 */     for (int c = 0; c < count - 1; c++) {
/*  94 */       Holder<MobEffect> effect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(3)).get(c);
/*  95 */       BeaconPowerButton beaconPowerButton1 = new BeaconPowerButton(this.leftPos + 167 + c * 24 - totalWidth / 2, this.topPos + 47, effect, false, 3);
/*  96 */       beaconPowerButton1.active = false;
/*  97 */       addBeaconButton(beaconPowerButton1);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     Holder<MobEffect> dummyEffect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(0)).get(0);
/* 102 */     BeaconPowerButton beaconPowerButton = new BeaconUpgradePowerButton(this.leftPos + 167 + (count - 1) * 24 - totalWidth / 2, this.topPos + 47, dummyEffect);
/* 103 */     beaconPowerButton.visible = false;
/* 104 */     addBeaconButton(beaconPowerButton);
/*     */     
/* 106 */     addBeaconButton(new BeaconConfirmButton(this.leftPos + 164, this.topPos + 107));
/* 107 */     addBeaconButton(new BeaconCancelButton(this.leftPos + 190, this.topPos + 107));
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerTick() {
/* 112 */     super.containerTick();
/* 113 */     updateButtons();
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/* 117 */     int levels = this.menu.getLevels();
/* 118 */     this.beaconButtons.forEach(b -> b.updateStatus(levels));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLabels(GuiGraphics graphics, int xm, int ym) {
/* 123 */     graphics.drawCenteredString(this.font, PRIMARY_EFFECT_LABEL, 62, 10, -2039584);
/* 124 */     graphics.drawCenteredString(this.font, SECONDARY_EFFECT_LABEL, 169, 10, -2039584);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
/* 129 */     int xo = (this.width - this.imageWidth) / 2;
/* 130 */     int yo = (this.height - this.imageHeight) / 2;
/* 131 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BEACON_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 132 */     graphics.renderItem(new ItemStack((ItemLike)Items.NETHERITE_INGOT), xo + 20, yo + 109);
/* 133 */     graphics.renderItem(new ItemStack((ItemLike)Items.EMERALD), xo + 41, yo + 109);
/* 134 */     graphics.renderItem(new ItemStack((ItemLike)Items.DIAMOND), xo + 41 + 22, yo + 109);
/* 135 */     graphics.renderItem(new ItemStack((ItemLike)Items.GOLD_INGOT), xo + 42 + 44, yo + 109);
/* 136 */     graphics.renderItem(new ItemStack((ItemLike)Items.IRON_INGOT), xo + 42 + 66, yo + 109);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 141 */     super.render(graphics, mouseX, mouseY, a);
/* 142 */     renderTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class BeaconScreenButton
/*     */     extends AbstractButton
/*     */     implements BeaconButton
/*     */   {
/*     */     private boolean selected;
/*     */     
/*     */     protected BeaconScreenButton(int x, int y) {
/* 153 */       super(x, y, 22, 22, CommonComponents.EMPTY);
/*     */     }
/*     */     
/*     */     protected BeaconScreenButton(int x, int y, Component component) {
/* 157 */       super(x, y, 22, 22, component);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */       Identifier sprite;
/* 163 */       if (!this.active) {
/* 164 */         sprite = BeaconScreen.BUTTON_DISABLED_SPRITE;
/* 165 */       } else if (this.selected) {
/* 166 */         sprite = BeaconScreen.BUTTON_SELECTED_SPRITE;
/* 167 */       } else if (isHoveredOrFocused()) {
/* 168 */         sprite = BeaconScreen.BUTTON_HIGHLIGHTED_SPRITE;
/*     */       } else {
/* 170 */         sprite = BeaconScreen.BUTTON_SPRITE;
/*     */       } 
/*     */       
/* 173 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/* 174 */       renderIcon(graphics);
/*     */     }
/*     */     
/*     */     protected abstract void renderIcon(GuiGraphics param1GuiGraphics);
/*     */     
/*     */     public boolean isSelected() {
/* 180 */       return this.selected;
/*     */     }
/*     */     
/*     */     public void setSelected(boolean selected) {
/* 184 */       this.selected = selected;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateWidgetNarration(NarrationElementOutput output) {
/* 189 */       defaultButtonNarrationText(output);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconPowerButton extends BeaconScreenButton {
/*     */     private final boolean isPrimary;
/*     */     protected final int tier;
/*     */     private Holder<MobEffect> effect;
/*     */     private Identifier sprite;
/*     */     
/*     */     public BeaconPowerButton(int x, int y, Holder<MobEffect> effect, boolean isPrimary, int tier) {
/* 200 */       super(x, y);
/* 201 */       this.isPrimary = isPrimary;
/* 202 */       this.tier = tier;
/* 203 */       setEffect(effect);
/*     */     }
/*     */     
/*     */     protected void setEffect(Holder<MobEffect> effect) {
/* 207 */       this.effect = effect;
/* 208 */       this.sprite = Gui.getMobEffectSprite(effect);
/* 209 */       setTooltip(Tooltip.create((Component)createEffectDescription(effect), null));
/*     */     }
/*     */     
/*     */     protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 213 */       return Component.translatable(((MobEffect)effect.value()).getDescriptionId());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 218 */       if (isSelected()) {
/*     */         return;
/*     */       }
/*     */       
/* 222 */       if (this.isPrimary) {
/* 223 */         BeaconScreen.this.primary = this.effect;
/*     */       } else {
/* 225 */         BeaconScreen.this.secondary = this.effect;
/*     */       } 
/* 227 */       BeaconScreen.this.updateButtons();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderIcon(GuiGraphics graphics) {
/* 232 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateStatus(int levels) {
/* 237 */       this.active = (this.tier < levels);
/* 238 */       setSelected(this.effect.equals(this.isPrimary ? BeaconScreen.this.primary : BeaconScreen.this.secondary));
/*     */     }
/*     */ 
/*     */     
/*     */     protected MutableComponent createNarrationMessage() {
/* 243 */       return createEffectDescription(this.effect);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconUpgradePowerButton extends BeaconPowerButton {
/*     */     public BeaconUpgradePowerButton(int x, int y, Holder<MobEffect> effect) {
/* 249 */       super(x, y, effect, false, 3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 254 */       return Component.translatable(((MobEffect)effect.value()).getDescriptionId()).append(" II");
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateStatus(int levels) {
/* 259 */       if (BeaconScreen.this.primary != null) {
/* 260 */         this.visible = true;
/* 261 */         setEffect(BeaconScreen.this.primary);
/* 262 */         super.updateStatus(levels);
/*     */       } else {
/* 264 */         this.visible = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class BeaconSpriteScreenButton extends BeaconScreenButton {
/*     */     private final Identifier sprite;
/*     */     
/*     */     protected BeaconSpriteScreenButton(int x, int y, Identifier sprite, Component label) {
/* 273 */       super(x, y, label);
/* 274 */       setTooltip(Tooltip.create(label));
/* 275 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderIcon(GuiGraphics graphics) {
/* 280 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconConfirmButton extends BeaconSpriteScreenButton {
/*     */     public BeaconConfirmButton(int x, int y) {
/* 286 */       super(x, y, BeaconScreen.CONFIRM_SPRITE, CommonComponents.GUI_DONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 291 */       BeaconScreen.this.minecraft.getConnection().send((Packet)new ServerboundSetBeaconPacket(Optional.ofNullable(BeaconScreen.this.primary), Optional.ofNullable(BeaconScreen.this.secondary)));
/* 292 */       BeaconScreen.this.minecraft.player.closeContainer();
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateStatus(int levels) {
/* 297 */       this.active = (BeaconScreen.this.menu.hasPayment() && BeaconScreen.this.primary != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconCancelButton extends BeaconSpriteScreenButton {
/*     */     public BeaconCancelButton(int x, int y) {
/* 303 */       super(x, y, BeaconScreen.CANCEL_SPRITE, CommonComponents.GUI_CANCEL);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 308 */       BeaconScreen.this.minecraft.player.closeContainer();
/*     */     }
/*     */     
/*     */     public void updateStatus(int levels) {}
/*     */   }
/*     */   
/*     */   private static interface BeaconButton {
/*     */     void updateStatus(int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BeaconScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */