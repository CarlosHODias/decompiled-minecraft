/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundTestInstanceBlockActionPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ 
/*     */ public class TestInstanceBlockEditScreen
/*     */   extends Screen {
/*  30 */   private static final Component ID_LABEL = (Component)Component.translatable("test_instance_block.test_id");
/*  31 */   private static final Component SIZE_LABEL = (Component)Component.translatable("test_instance_block.size");
/*  32 */   private static final Component INCLUDE_ENTITIES_LABEL = (Component)Component.translatable("test_instance_block.entities");
/*  33 */   private static final Component ROTATION_LABEL = (Component)Component.translatable("test_instance_block.rotation");
/*     */   
/*     */   private static final int BUTTON_PADDING = 8;
/*     */   
/*     */   private static final int WIDTH = 316;
/*     */   private final TestInstanceBlockEntity blockEntity;
/*     */   private EditBox idEdit;
/*     */   private EditBox sizeXEdit;
/*     */   private EditBox sizeYEdit;
/*     */   private EditBox sizeZEdit;
/*     */   private FittingMultiLineTextWidget infoWidget;
/*     */   private Button saveButton;
/*     */   private Button exportButton;
/*     */   private CycleButton<Boolean> includeEntitiesButton;
/*     */   private CycleButton<Rotation> rotationButton;
/*     */   
/*     */   public TestInstanceBlockEditScreen(TestInstanceBlockEntity blockEntity) {
/*  50 */     super((Component)blockEntity.getBlockState().getBlock().getName());
/*  51 */     this.blockEntity = blockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  56 */     int leftEdge = this.width / 2 - 158;
/*  57 */     boolean includeExport = SharedConstants.IS_RUNNING_IN_IDE;
/*  58 */     int actionButtonCount = includeExport ? 3 : 2;
/*  59 */     int buttonSize = widgetSize(actionButtonCount);
/*     */     
/*  61 */     this.idEdit = new EditBox(this.font, leftEdge, 40, 316, 20, (Component)Component.translatable("test_instance_block.test_id"));
/*  62 */     this.idEdit.setMaxLength(128);
/*  63 */     Optional<ResourceKey<GameTestInstance>> test = this.blockEntity.test();
/*  64 */     if (test.isPresent()) {
/*  65 */       this.idEdit.setValue(((ResourceKey)test.get()).identifier().toString());
/*     */     }
/*  67 */     this.idEdit.setResponder(s -> updateTestInfo(false));
/*  68 */     addRenderableWidget((GuiEventListener)this.idEdit);
/*     */     
/*  70 */     Objects.requireNonNull(this.font); this.infoWidget = new FittingMultiLineTextWidget(leftEdge, 70, 316, 8 * 9, (Component)Component.literal(""), this.font);
/*  71 */     addRenderableWidget((GuiEventListener)this.infoWidget);
/*     */     
/*  73 */     Vec3i size = this.blockEntity.getSize();
/*     */     
/*  75 */     int index = 0;
/*     */     
/*  77 */     this.sizeXEdit = new EditBox(this.font, widgetX(index++, 5), 160, widgetSize(5), 20, (Component)Component.translatable("structure_block.size.x"));
/*  78 */     this.sizeXEdit.setMaxLength(15);
/*  79 */     addRenderableWidget((GuiEventListener)this.sizeXEdit);
/*  80 */     this.sizeYEdit = new EditBox(this.font, widgetX(index++, 5), 160, widgetSize(5), 20, (Component)Component.translatable("structure_block.size.y"));
/*  81 */     this.sizeYEdit.setMaxLength(15);
/*  82 */     addRenderableWidget((GuiEventListener)this.sizeYEdit);
/*  83 */     this.sizeZEdit = new EditBox(this.font, widgetX(index++, 5), 160, widgetSize(5), 20, (Component)Component.translatable("structure_block.size.z"));
/*  84 */     this.sizeZEdit.setMaxLength(15);
/*  85 */     addRenderableWidget((GuiEventListener)this.sizeZEdit);
/*  86 */     setSize(size);
/*     */     
/*  88 */     this.rotationButton = (CycleButton<Rotation>)addRenderableWidget(
/*  89 */         (GuiEventListener)CycleButton.builder(TestInstanceBlockEditScreen::rotationDisplay, this.blockEntity.getRotation())
/*  90 */         .withValues((Object[])Rotation.values())
/*  91 */         .displayOnlyValue()
/*  92 */         .create(widgetX(index++, 5), 160, widgetSize(5), 20, ROTATION_LABEL, (button, value) -> updateSaveState()));
/*     */     
/*  94 */     this.includeEntitiesButton = (CycleButton<Boolean>)addRenderableWidget(
/*  95 */         (GuiEventListener)CycleButton.onOffBuilder(!this.blockEntity.ignoreEntities())
/*  96 */         .displayOnlyValue()
/*  97 */         .create(widgetX(index++, 5), 160, widgetSize(5), 20, INCLUDE_ENTITIES_LABEL));
/*     */ 
/*     */     
/* 100 */     index = 0;
/* 101 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("test_instance.action.reset"), button -> {
/*     */             sendToServer(ServerboundTestInstanceBlockActionPacket.Action.RESET);
/*     */             this.minecraft.setScreen(null);
/* 104 */           }).bounds(widgetX(index++, actionButtonCount), 185, buttonSize, 20).build());
/*     */     
/* 106 */     this.saveButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("test_instance.action.save"), button -> {
/*     */             sendToServer(ServerboundTestInstanceBlockActionPacket.Action.SAVE);
/*     */             this.minecraft.setScreen(null);
/* 109 */           }).bounds(widgetX(index++, actionButtonCount), 185, buttonSize, 20).build());
/*     */     
/* 111 */     if (includeExport) {
/* 112 */       this.exportButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal("Export Structure"), button -> {
/*     */               sendToServer(ServerboundTestInstanceBlockActionPacket.Action.EXPORT);
/*     */               this.minecraft.setScreen(null);
/* 115 */             }).bounds(widgetX(index++, actionButtonCount), 185, buttonSize, 20).build());
/*     */     }
/*     */     
/* 118 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("test_instance.action.run"), button -> {
/*     */             sendToServer(ServerboundTestInstanceBlockActionPacket.Action.RUN);
/*     */             this.minecraft.setScreen(null);
/* 121 */           }).bounds(widgetX(0, 3), 210, widgetSize(3), 20).build());
/* 122 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(widgetX(1, 3), 210, widgetSize(3), 20).build());
/* 123 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).bounds(widgetX(2, 3), 210, widgetSize(3), 20).build());
/*     */     
/* 125 */     updateTestInfo(true);
/*     */   }
/*     */   
/*     */   private void updateSaveState() {
/* 129 */     boolean allowSaving = (this.rotationButton.getValue() == Rotation.NONE && Identifier.tryParse(this.idEdit.getValue()) != null);
/* 130 */     this.saveButton.active = allowSaving;
/* 131 */     if (this.exportButton != null) {
/* 132 */       this.exportButton.active = allowSaving;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Component rotationDisplay(Rotation rotation) {
/* 137 */     switch (rotation) { default: throw new MatchException(null, null);case NONE: case CLOCKWISE_90: case CLOCKWISE_180: case COUNTERCLOCKWISE_90: break; }  return (Component)Component.literal(
/*     */ 
/*     */ 
/*     */         
/* 141 */         "270");
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSize(Vec3i size) {
/* 146 */     this.sizeXEdit.setValue(Integer.toString(size.getX()));
/* 147 */     this.sizeYEdit.setValue(Integer.toString(size.getY()));
/* 148 */     this.sizeZEdit.setValue(Integer.toString(size.getZ()));
/*     */   }
/*     */   
/*     */   private int widgetX(int index, int count) {
/* 152 */     int leftEdge = this.width / 2 - 158;
/* 153 */     float buttonSize = exactWidgetSize(count);
/* 154 */     return (int)(leftEdge + index * (8.0F + buttonSize));
/*     */   }
/*     */   
/*     */   private static int widgetSize(int count) {
/* 158 */     return (int)exactWidgetSize(count);
/*     */   }
/*     */   
/*     */   private static float exactWidgetSize(int count) {
/* 162 */     return (316 - (count - 1) * 8) / count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 167 */     super.render(graphics, mouseX, mouseY, a);
/* 168 */     int leftEdge = this.width / 2 - 158;
/* 169 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, -1);
/* 170 */     graphics.drawString(this.font, ID_LABEL, leftEdge, 30, -6250336);
/* 171 */     graphics.drawString(this.font, SIZE_LABEL, leftEdge, 150, -6250336);
/* 172 */     graphics.drawString(this.font, ROTATION_LABEL, this.rotationButton.getX(), 150, -6250336);
/* 173 */     graphics.drawString(this.font, INCLUDE_ENTITIES_LABEL, this.includeEntitiesButton.getX(), 150, -6250336);
/*     */   }
/*     */   
/*     */   private void updateTestInfo(boolean isInit) {
/* 177 */     boolean valid = sendToServer(isInit ? ServerboundTestInstanceBlockActionPacket.Action.INIT : ServerboundTestInstanceBlockActionPacket.Action.QUERY);
/* 178 */     if (!valid) {
/* 179 */       this.infoWidget.setMessage((Component)Component.translatable("test_instance.description.invalid_id").withStyle(ChatFormatting.RED));
/*     */     }
/* 181 */     updateSaveState();
/*     */   }
/*     */   
/*     */   private void onDone() {
/* 185 */     sendToServer(ServerboundTestInstanceBlockActionPacket.Action.SET);
/* 186 */     onClose();
/*     */   }
/*     */   
/*     */   private boolean sendToServer(ServerboundTestInstanceBlockActionPacket.Action action) {
/* 190 */     Optional<Identifier> id = Optional.ofNullable(Identifier.tryParse(this.idEdit.getValue()));
/* 191 */     Optional<ResourceKey<GameTestInstance>> key = id.map(i -> ResourceKey.create(Registries.TEST_INSTANCE, i));
/* 192 */     Vec3i size = new Vec3i(parseSize(this.sizeXEdit.getValue()), parseSize(this.sizeYEdit.getValue()), parseSize(this.sizeZEdit.getValue()));
/* 193 */     boolean ignoreEntities = !((Boolean)this.includeEntitiesButton.getValue());
/* 194 */     this.minecraft.getConnection().send((Packet)new ServerboundTestInstanceBlockActionPacket(this.blockEntity.getBlockPos(), action, key, size, (Rotation)this.rotationButton.getValue(), ignoreEntities));
/* 195 */     return id.isPresent();
/*     */   }
/*     */   
/*     */   public void setStatus(Component status, Optional<Vec3i> size) {
/* 199 */     MutableComponent description = Component.empty();
/* 200 */     this.blockEntity.errorMessage().ifPresent(error -> description.append((Component)Component.translatable("test_instance.description.failed", new Object[] { Component.empty().withStyle(ChatFormatting.RED).append(error) })).append("\n\n"));
/*     */ 
/*     */ 
/*     */     
/* 204 */     description.append(status);
/* 205 */     this.infoWidget.setMessage((Component)description);
/* 206 */     size.ifPresent(this::setSize);
/*     */   }
/*     */   
/*     */   private void onCancel() {
/* 210 */     onClose();
/*     */   }
/*     */   
/*     */   private static int parseSize(String value) {
/*     */     try {
/* 215 */       return Mth.clamp(Integer.parseInt(value), 1, 48);
/* 216 */     } catch (NumberFormatException ignored) {
/* 217 */       return 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 223 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/TestInstanceBlockEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */