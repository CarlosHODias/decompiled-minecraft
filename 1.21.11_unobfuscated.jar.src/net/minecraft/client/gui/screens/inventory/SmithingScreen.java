/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.SmithingMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SmithingTemplateItem;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SmithingScreen
/*     */   extends ItemCombinerScreen<SmithingMenu>
/*     */ {
/*  28 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/smithing/error");
/*  29 */   private static final Identifier EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM = Identifier.withDefaultNamespace("container/slot/smithing_template_armor_trim");
/*  30 */   private static final Identifier EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE = Identifier.withDefaultNamespace("container/slot/smithing_template_netherite_upgrade");
/*  31 */   private static final Component MISSING_TEMPLATE_TOOLTIP = (Component)Component.translatable("container.upgrade.missing_template_tooltip");
/*  32 */   private static final Component ERROR_TOOLTIP = (Component)Component.translatable("container.upgrade.error_tooltip");
/*  33 */   private static final List<Identifier> EMPTY_SLOT_SMITHING_TEMPLATES = List.of(EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM, EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE);
/*     */   
/*     */   private static final int TITLE_LABEL_X = 44;
/*     */   
/*     */   private static final int TITLE_LABEL_Y = 15;
/*     */   
/*     */   private static final int ERROR_ICON_WIDTH = 28;
/*     */   
/*     */   private static final int ERROR_ICON_HEIGHT = 21;
/*     */   private static final int ERROR_ICON_X = 65;
/*     */   private static final int ERROR_ICON_Y = 46;
/*     */   private static final int TOOLTIP_WIDTH = 115;
/*     */   private static final int ARMOR_STAND_Y_ROT = 210;
/*     */   private static final int ARMOR_STAND_X_ROT = 25;
/*  47 */   private static final Vector3f ARMOR_STAND_TRANSLATION = new Vector3f(0.0F, 1.0F, 0.0F);
/*  48 */   private static final Quaternionf ARMOR_STAND_ANGLE = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, 3.1415927F);
/*     */   
/*     */   private static final int ARMOR_STAND_SCALE = 25;
/*     */   private static final int ARMOR_STAND_LEFT = 121;
/*     */   private static final int ARMOR_STAND_TOP = 20;
/*     */   private static final int ARMOR_STAND_RIGHT = 161;
/*     */   private static final int ARMOR_STAND_BOTTOM = 80;
/*  55 */   private final CyclingSlotBackground templateIcon = new CyclingSlotBackground(0);
/*  56 */   private final CyclingSlotBackground baseIcon = new CyclingSlotBackground(1);
/*  57 */   private final CyclingSlotBackground additionalIcon = new CyclingSlotBackground(2);
/*  58 */   private final ArmorStandRenderState armorStandPreview = new ArmorStandRenderState();
/*     */   
/*     */   public SmithingScreen(SmithingMenu menu, Inventory inventory, Component title) {
/*  61 */     super(menu, inventory, title, Identifier.withDefaultNamespace("textures/gui/container/smithing.png"));
/*  62 */     this.titleLabelX = 44;
/*  63 */     this.titleLabelY = 15;
/*     */     
/*  65 */     this.armorStandPreview.entityType = EntityType.ARMOR_STAND;
/*  66 */     this.armorStandPreview.showBasePlate = false;
/*  67 */     this.armorStandPreview.showArms = true;
/*  68 */     this.armorStandPreview.xRot = 25.0F;
/*  69 */     this.armorStandPreview.bodyRot = 210.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subInit() {
/*  74 */     updateArmorStandPreview(this.menu.getSlot(3).getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerTick() {
/*  79 */     super.containerTick();
/*     */     
/*  81 */     Optional<SmithingTemplateItem> template = getTemplateItem();
/*  82 */     this.templateIcon.tick(EMPTY_SLOT_SMITHING_TEMPLATES);
/*  83 */     this.baseIcon.tick(template.<List<Identifier>>map(SmithingTemplateItem::getBaseSlotEmptyIcons).orElse(List.of()));
/*  84 */     this.additionalIcon.tick(template.<List<Identifier>>map(SmithingTemplateItem::getAdditionalSlotEmptyIcons).orElse(List.of()));
/*     */   }
/*     */   
/*     */   private Optional<SmithingTemplateItem> getTemplateItem() {
/*  88 */     ItemStack templateSlotItem = this.menu.getSlot(0).getItem();
/*  89 */     if (!templateSlotItem.isEmpty()) { Item item = templateSlotItem.getItem(); if (item instanceof SmithingTemplateItem) { SmithingTemplateItem templateItem = (SmithingTemplateItem)item;
/*  90 */         return Optional.of(templateItem); }
/*     */        }
/*  92 */      return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  97 */     super.render(graphics, mouseX, mouseY, a);
/*  98 */     renderOnboardingTooltips(graphics, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderBg(GuiGraphics graphics, float a, int xMouse, int yMouse) {
/* 103 */     super.renderBg(graphics, a, xMouse, yMouse);
/* 104 */     this.templateIcon.render((AbstractContainerMenu)this.menu, graphics, a, this.leftPos, this.topPos);
/* 105 */     this.baseIcon.render((AbstractContainerMenu)this.menu, graphics, a, this.leftPos, this.topPos);
/* 106 */     this.additionalIcon.render((AbstractContainerMenu)this.menu, graphics, a, this.leftPos, this.topPos);
/*     */     
/* 108 */     int x0 = this.leftPos + 121;
/* 109 */     int y0 = this.topPos + 20;
/* 110 */     int x1 = this.leftPos + 161;
/* 111 */     int y1 = this.topPos + 80;
/* 112 */     graphics.submitEntityRenderState((EntityRenderState)this.armorStandPreview, 25.0F, ARMOR_STAND_TRANSLATION, ARMOR_STAND_ANGLE, null, x0, y0, x1, y1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 117 */     if (slotIndex == 3) {
/* 118 */       updateArmorStandPreview(itemStack);
/*     */     }
/*     */   }
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
/*     */   private void updateArmorStandPreview(ItemStack itemStack) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   4: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   7: putfield leftHandItemStack : Lnet/minecraft/world/item/ItemStack;
/*     */     //   10: aload_0
/*     */     //   11: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   14: getfield leftHandItemState : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   17: invokevirtual clear : ()V
/*     */     //   20: aload_0
/*     */     //   21: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   24: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   27: putfield headEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   30: aload_0
/*     */     //   31: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   34: getfield headItem : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   37: invokevirtual clear : ()V
/*     */     //   40: aload_0
/*     */     //   41: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   44: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   47: putfield chestEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   50: aload_0
/*     */     //   51: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   54: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   57: putfield legsEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   60: aload_0
/*     */     //   61: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   64: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   67: putfield feetEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   70: aload_1
/*     */     //   71: invokevirtual isEmpty : ()Z
/*     */     //   74: ifne -> 278
/*     */     //   77: aload_1
/*     */     //   78: getstatic net/minecraft/core/component/DataComponents.EQUIPPABLE : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   81: invokevirtual get : (Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;
/*     */     //   84: checkcast net/minecraft/world/item/equipment/Equippable
/*     */     //   87: astore_2
/*     */     //   88: aload_2
/*     */     //   89: ifnull -> 99
/*     */     //   92: aload_2
/*     */     //   93: invokevirtual slot : ()Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   96: goto -> 100
/*     */     //   99: aconst_null
/*     */     //   100: astore_3
/*     */     //   101: aload_0
/*     */     //   102: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   105: invokevirtual getItemModelResolver : ()Lnet/minecraft/client/renderer/item/ItemModelResolver;
/*     */     //   108: astore #4
/*     */     //   110: aload_3
/*     */     //   111: astore #5
/*     */     //   113: iconst_0
/*     */     //   114: istore #6
/*     */     //   116: aload #5
/*     */     //   118: iload #6
/*     */     //   120: <illegal opcode> enumSwitch : (Lnet/minecraft/world/entity/EquipmentSlot;I)I
/*     */     //   125: tableswitch default -> 248, -1 -> 248, 0 -> 160, 1 -> 206, 2 -> 220, 3 -> 234
/*     */     //   160: aload_1
/*     */     //   161: getstatic net/minecraft/world/entity/EquipmentSlot.HEAD : Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   164: invokestatic shouldRender : (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z
/*     */     //   167: ifeq -> 184
/*     */     //   170: aload_0
/*     */     //   171: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   174: aload_1
/*     */     //   175: invokevirtual copy : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   178: putfield headEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   181: goto -> 278
/*     */     //   184: aload #4
/*     */     //   186: aload_0
/*     */     //   187: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   190: getfield headItem : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   193: aload_1
/*     */     //   194: getstatic net/minecraft/world/item/ItemDisplayContext.HEAD : Lnet/minecraft/world/item/ItemDisplayContext;
/*     */     //   197: aconst_null
/*     */     //   198: aconst_null
/*     */     //   199: iconst_0
/*     */     //   200: invokevirtual updateForTopItem : (Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/ItemOwner;I)V
/*     */     //   203: goto -> 278
/*     */     //   206: aload_0
/*     */     //   207: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   210: aload_1
/*     */     //   211: invokevirtual copy : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   214: putfield chestEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   217: goto -> 278
/*     */     //   220: aload_0
/*     */     //   221: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   224: aload_1
/*     */     //   225: invokevirtual copy : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   228: putfield legsEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   231: goto -> 278
/*     */     //   234: aload_0
/*     */     //   235: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   238: aload_1
/*     */     //   239: invokevirtual copy : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   242: putfield feetEquipment : Lnet/minecraft/world/item/ItemStack;
/*     */     //   245: goto -> 278
/*     */     //   248: aload_0
/*     */     //   249: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   252: aload_1
/*     */     //   253: invokevirtual copy : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   256: putfield leftHandItemStack : Lnet/minecraft/world/item/ItemStack;
/*     */     //   259: aload #4
/*     */     //   261: aload_0
/*     */     //   262: getfield armorStandPreview : Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;
/*     */     //   265: getfield leftHandItemState : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   268: aload_1
/*     */     //   269: getstatic net/minecraft/world/item/ItemDisplayContext.THIRD_PERSON_LEFT_HAND : Lnet/minecraft/world/item/ItemDisplayContext;
/*     */     //   272: aconst_null
/*     */     //   273: aconst_null
/*     */     //   274: iconst_0
/*     */     //   275: invokevirtual updateForTopItem : (Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/ItemOwner;I)V
/*     */     //   278: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     //   #124	-> 10
/*     */     //   #125	-> 20
/*     */     //   #126	-> 30
/*     */     //   #127	-> 40
/*     */     //   #128	-> 50
/*     */     //   #129	-> 60
/*     */     //   #131	-> 70
/*     */     //   #132	-> 77
/*     */     //   #133	-> 88
/*     */     //   #134	-> 101
/*     */     //   #135	-> 110
/*     */     //   #137	-> 160
/*     */     //   #138	-> 170
/*     */     //   #140	-> 184
/*     */     //   #142	-> 203
/*     */     //   #143	-> 206
/*     */     //   #144	-> 220
/*     */     //   #145	-> 234
/*     */     //   #148	-> 248
/*     */     //   #149	-> 259
/*     */     //   #153	-> 278
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   88	190	2	equippable	Lnet/minecraft/world/item/equipment/Equippable;
/*     */     //   101	177	3	slot	Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   110	168	4	itemModelResolver	Lnet/minecraft/client/renderer/item/ItemModelResolver;
/*     */     //   0	279	0	this	Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;
/*     */     //   0	279	1	itemStack	Lnet/minecraft/world/item/ItemStack;
/*     */   }
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
/*     */   protected void renderErrorIcon(GuiGraphics graphics, int xo, int yo) {
/* 157 */     if (hasRecipeError()) {
/* 158 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 65, yo + 46, 28, 21);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderOnboardingTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
/* 163 */     Optional<Component> tooltip = Optional.empty();
/*     */     
/* 165 */     if (hasRecipeError() && isHovering(65, 46, 28, 21, mouseX, mouseY)) {
/* 166 */       tooltip = Optional.of(ERROR_TOOLTIP);
/*     */     }
/*     */     
/* 169 */     if (this.hoveredSlot != null) {
/* 170 */       ItemStack template = this.menu.getSlot(0).getItem();
/* 171 */       ItemStack hoveredStack = this.hoveredSlot.getItem();
/*     */       
/* 173 */       if (template.isEmpty()) {
/* 174 */         if (this.hoveredSlot.index == 0)
/* 175 */           tooltip = Optional.of(MISSING_TEMPLATE_TOOLTIP); 
/*     */       } else {
/* 177 */         Item item = template.getItem(); if (item instanceof SmithingTemplateItem) { SmithingTemplateItem templateItem = (SmithingTemplateItem)item; if (hoveredStack.isEmpty())
/* 178 */             if (this.hoveredSlot.index == 1) {
/* 179 */               tooltip = Optional.of(templateItem.getBaseSlotDescription());
/* 180 */             } else if (this.hoveredSlot.index == 2) {
/* 181 */               tooltip = Optional.of(templateItem.getAdditionSlotDescription());
/*     */             }   }
/*     */       
/*     */       } 
/*     */     } 
/* 186 */     tooltip.ifPresent(component -> graphics.setTooltipForNextFrame(this.font, this.font.split((FormattedText)mouseY, 115), graphics, mouseX));
/*     */   }
/*     */   
/*     */   private boolean hasRecipeError() {
/* 190 */     return this.menu.hasRecipeError();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/SmithingScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */