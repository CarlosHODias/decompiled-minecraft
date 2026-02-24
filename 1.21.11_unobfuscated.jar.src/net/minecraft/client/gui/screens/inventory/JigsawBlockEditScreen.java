/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ 
/*     */ public class JigsawBlockEditScreen extends Screen {
/*  24 */   private static final Component JOINT_LABEL = (Component)Component.translatable("jigsaw_block.joint_label");
/*  25 */   private static final Component POOL_LABEL = (Component)Component.translatable("jigsaw_block.pool");
/*  26 */   private static final Component NAME_LABEL = (Component)Component.translatable("jigsaw_block.name");
/*  27 */   private static final Component TARGET_LABEL = (Component)Component.translatable("jigsaw_block.target");
/*  28 */   private static final Component FINAL_STATE_LABEL = (Component)Component.translatable("jigsaw_block.final_state");
/*  29 */   private static final Component PLACEMENT_PRIORITY_LABEL = (Component)Component.translatable("jigsaw_block.placement_priority");
/*  30 */   private static final Component PLACEMENT_PRIORITY_TOOLTIP = (Component)Component.translatable("jigsaw_block.placement_priority.tooltip");
/*  31 */   private static final Component SELECTION_PRIORITY_LABEL = (Component)Component.translatable("jigsaw_block.selection_priority");
/*  32 */   private static final Component SELECTION_PRIORITY_TOOLTIP = (Component)Component.translatable("jigsaw_block.selection_priority.tooltip");
/*     */   
/*     */   private final JigsawBlockEntity jigsawEntity;
/*     */   
/*     */   private EditBox nameEdit;
/*     */   
/*     */   private EditBox targetEdit;
/*     */   
/*     */   private EditBox poolEdit;
/*     */   
/*     */   private EditBox finalStateEdit;
/*     */   private EditBox selectionPriorityEdit;
/*     */   private EditBox placementPriorityEdit;
/*     */   private int levels;
/*     */   private boolean keepJigsaws = true;
/*     */   private CycleButton<JigsawBlockEntity.JointType> jointButton;
/*     */   private Button doneButton;
/*     */   private Button generateButton;
/*     */   private JigsawBlockEntity.JointType joint;
/*     */   
/*     */   public JigsawBlockEditScreen(JigsawBlockEntity jigsawEntity) {
/*  53 */     super(GameNarrator.NO_TITLE);
/*  54 */     this.jigsawEntity = jigsawEntity;
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  58 */     sendToServer();
/*  59 */     this.minecraft.setScreen(null);
/*     */   }
/*     */   
/*     */   private void onCancel() {
/*  63 */     this.minecraft.setScreen(null);
/*     */   }
/*     */   
/*     */   private void sendToServer() {
/*  67 */     this.minecraft.getConnection().send((Packet)new ServerboundSetJigsawBlockPacket(
/*  68 */           this.jigsawEntity.getBlockPos(), 
/*  69 */           Identifier.parse(this.nameEdit.getValue()), 
/*  70 */           Identifier.parse(this.targetEdit.getValue()), 
/*  71 */           Identifier.parse(this.poolEdit.getValue()), 
/*  72 */           this.finalStateEdit.getValue(), this.joint, 
/*     */           
/*  74 */           parseAsInt(this.selectionPriorityEdit.getValue()), 
/*  75 */           parseAsInt(this.placementPriorityEdit.getValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   private int parseAsInt(String value) {
/*     */     try {
/*  81 */       return Integer.parseInt(value);
/*  82 */     } catch (NumberFormatException ignored) {
/*  83 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendGenerate() {
/*  88 */     this.minecraft.getConnection().send((Packet)new ServerboundJigsawGeneratePacket(
/*  89 */           this.jigsawEntity.getBlockPos(), this.levels, this.keepJigsaws));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  97 */     onCancel();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 102 */     this.poolEdit = new EditBox(this.font, this.width / 2 - 153, 20, 300, 20, POOL_LABEL);
/* 103 */     this.poolEdit.setMaxLength(128);
/* 104 */     this.poolEdit.setValue(this.jigsawEntity.getPool().identifier().toString());
/* 105 */     this.poolEdit.setResponder(value -> updateValidity());
/* 106 */     addWidget((GuiEventListener)this.poolEdit);
/*     */     
/* 108 */     this.nameEdit = new EditBox(this.font, this.width / 2 - 153, 55, 300, 20, NAME_LABEL);
/* 109 */     this.nameEdit.setMaxLength(128);
/* 110 */     this.nameEdit.setValue(this.jigsawEntity.getName().toString());
/* 111 */     this.nameEdit.setResponder(value -> updateValidity());
/* 112 */     addWidget((GuiEventListener)this.nameEdit);
/*     */     
/* 114 */     this.targetEdit = new EditBox(this.font, this.width / 2 - 153, 90, 300, 20, TARGET_LABEL);
/* 115 */     this.targetEdit.setMaxLength(128);
/* 116 */     this.targetEdit.setValue(this.jigsawEntity.getTarget().toString());
/* 117 */     this.targetEdit.setResponder(value -> updateValidity());
/* 118 */     addWidget((GuiEventListener)this.targetEdit);
/*     */     
/* 120 */     this.finalStateEdit = new EditBox(this.font, this.width / 2 - 153, 125, 300, 20, FINAL_STATE_LABEL);
/* 121 */     this.finalStateEdit.setMaxLength(256);
/* 122 */     this.finalStateEdit.setValue(this.jigsawEntity.getFinalState());
/* 123 */     addWidget((GuiEventListener)this.finalStateEdit);
/*     */     
/* 125 */     this.selectionPriorityEdit = new EditBox(this.font, this.width / 2 - 153, 160, 98, 20, SELECTION_PRIORITY_LABEL);
/* 126 */     this.selectionPriorityEdit.setMaxLength(3);
/* 127 */     this.selectionPriorityEdit.setValue(Integer.toString(this.jigsawEntity.getSelectionPriority()));
/* 128 */     this.selectionPriorityEdit.setTooltip(Tooltip.create(SELECTION_PRIORITY_TOOLTIP));
/* 129 */     addWidget((GuiEventListener)this.selectionPriorityEdit);
/*     */     
/* 131 */     this.placementPriorityEdit = new EditBox(this.font, this.width / 2 - 50, 160, 98, 20, PLACEMENT_PRIORITY_LABEL);
/* 132 */     this.placementPriorityEdit.setMaxLength(3);
/* 133 */     this.placementPriorityEdit.setValue(Integer.toString(this.jigsawEntity.getPlacementPriority()));
/* 134 */     this.placementPriorityEdit.setTooltip(Tooltip.create(PLACEMENT_PRIORITY_TOOLTIP));
/* 135 */     addWidget((GuiEventListener)this.placementPriorityEdit);
/*     */     
/* 137 */     this.joint = this.jigsawEntity.getJoint();
/* 138 */     this.jointButton = (CycleButton<JigsawBlockEntity.JointType>)addRenderableWidget((GuiEventListener)CycleButton.builder(JigsawBlockEntity.JointType::getTranslatedName, this.joint).withValues((Object[])JigsawBlockEntity.JointType.values())
/* 139 */         .displayOnlyValue()
/* 140 */         .create(this.width / 2 + 54, 160, 100, 20, JOINT_LABEL, (button, value) -> this.joint = value));
/*     */     
/* 142 */     boolean vertical = JigsawBlock.getFrontFacing(this.jigsawEntity.getBlockState()).getAxis().isVertical();
/* 143 */     this.jointButton.active = vertical;
/* 144 */     this.jointButton.visible = vertical;
/*     */     
/* 146 */     addRenderableWidget((GuiEventListener)new AbstractSliderButton(this.width / 2 - 154, 185, 100, 20, CommonComponents.EMPTY, 0.0D) {
/*     */           {
/* 148 */             updateMessage();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void updateMessage() {
/* 153 */             setMessage((Component)Component.translatable("jigsaw_block.levels", new Object[] { JigsawBlockEditScreen.this.levels }));
/*     */           }
/*     */ 
/*     */           
/*     */           protected void applyValue() {
/* 158 */             JigsawBlockEditScreen.this.levels = Mth.floor(Mth.clampedLerp(this.value, 0.0D, 20.0D));
/*     */           }
/*     */         });
/*     */     
/* 162 */     addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(this.keepJigsaws).create(this.width / 2 - 50, 185, 100, 20, (Component)Component.translatable("jigsaw_block.keep_jigsaws"), (button, value) -> this.keepJigsaws = value));
/*     */     
/* 164 */     this.generateButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("jigsaw_block.generate"), button -> {
/*     */             onDone();
/*     */             
/*     */             sendGenerate();
/* 168 */           }).bounds(this.width / 2 + 54, 185, 100, 20).build());
/*     */     
/* 170 */     this.doneButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 4 - 150, 210, 150, 20).build());
/* 171 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).bounds(this.width / 2 + 4, 210, 150, 20).build());
/* 172 */     updateValidity();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 177 */     setInitialFocus((GuiEventListener)this.poolEdit);
/*     */   }
/*     */   
/*     */   public static boolean isValidIdentifier(String location) {
/* 181 */     return (Identifier.tryParse(location) != null);
/*     */   }
/*     */   
/*     */   private void updateValidity() {
/* 185 */     boolean isValid = (isValidIdentifier(this.nameEdit.getValue()) && 
/* 186 */       isValidIdentifier(this.targetEdit.getValue()) && 
/* 187 */       isValidIdentifier(this.poolEdit.getValue()));
/* 188 */     this.doneButton.active = isValid;
/* 189 */     this.generateButton.active = isValid;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 199 */     String oldNameEdit = this.nameEdit.getValue();
/* 200 */     String oldTargetEdit = this.targetEdit.getValue();
/* 201 */     String oldPoolEdit = this.poolEdit.getValue();
/* 202 */     String oldFinalStateEdit = this.finalStateEdit.getValue();
/* 203 */     String oldSelectionPriorityEdit = this.selectionPriorityEdit.getValue();
/* 204 */     String oldPlacementPriorityEdit = this.placementPriorityEdit.getValue();
/* 205 */     int oldLevels = this.levels;
/* 206 */     JigsawBlockEntity.JointType oldJointType = this.joint;
/*     */     
/* 208 */     init(width, height);
/*     */     
/* 210 */     this.nameEdit.setValue(oldNameEdit);
/* 211 */     this.targetEdit.setValue(oldTargetEdit);
/* 212 */     this.poolEdit.setValue(oldPoolEdit);
/* 213 */     this.finalStateEdit.setValue(oldFinalStateEdit);
/* 214 */     this.levels = oldLevels;
/* 215 */     this.joint = oldJointType;
/* 216 */     this.jointButton.setValue(oldJointType);
/* 217 */     this.selectionPriorityEdit.setValue(oldSelectionPriorityEdit);
/* 218 */     this.placementPriorityEdit.setValue(oldPlacementPriorityEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 223 */     if (super.keyPressed(event)) {
/* 224 */       return true;
/*     */     }
/*     */     
/* 227 */     if (this.doneButton.active && event.isConfirmation()) {
/* 228 */       onDone();
/* 229 */       return true;
/*     */     } 
/*     */     
/* 232 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 237 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 239 */     graphics.drawString(this.font, POOL_LABEL, this.width / 2 - 153, 10, -6250336);
/* 240 */     this.poolEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 242 */     graphics.drawString(this.font, NAME_LABEL, this.width / 2 - 153, 45, -6250336);
/* 243 */     this.nameEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 245 */     graphics.drawString(this.font, TARGET_LABEL, this.width / 2 - 153, 80, -6250336);
/* 246 */     this.targetEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 248 */     graphics.drawString(this.font, FINAL_STATE_LABEL, this.width / 2 - 153, 115, -6250336);
/* 249 */     this.finalStateEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 251 */     graphics.drawString(this.font, SELECTION_PRIORITY_LABEL, this.width / 2 - 153, 150, -6250336);
/* 252 */     this.placementPriorityEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 254 */     graphics.drawString(this.font, PLACEMENT_PRIORITY_LABEL, this.width / 2 - 50, 150, -6250336);
/* 255 */     this.selectionPriorityEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 257 */     if (JigsawBlock.getFrontFacing(this.jigsawEntity.getBlockState()).getAxis().isVertical())
/* 258 */       graphics.drawString(this.font, JOINT_LABEL, this.width / 2 + 53, 150, -6250336); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/JigsawBlockEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */