/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ 
/*     */ public class StructureBlockEditScreen extends Screen {
/*  28 */   private static final Component NAME_LABEL = (Component)Component.translatable("structure_block.structure_name");
/*  29 */   private static final Component POSITION_LABEL = (Component)Component.translatable("structure_block.position");
/*  30 */   private static final Component SIZE_LABEL = (Component)Component.translatable("structure_block.size");
/*  31 */   private static final Component INTEGRITY_LABEL = (Component)Component.translatable("structure_block.integrity");
/*  32 */   private static final Component CUSTOM_DATA_LABEL = (Component)Component.translatable("structure_block.custom_data");
/*  33 */   private static final Component INCLUDE_ENTITIES_LABEL = (Component)Component.translatable("structure_block.include_entities");
/*  34 */   private static final Component STRICT_LABEL = (Component)Component.translatable("structure_block.strict");
/*  35 */   private static final Component DETECT_SIZE_LABEL = (Component)Component.translatable("structure_block.detect_size");
/*  36 */   private static final Component SHOW_AIR_LABEL = (Component)Component.translatable("structure_block.show_air");
/*  37 */   private static final Component SHOW_BOUNDING_BOX_LABEL = (Component)Component.translatable("structure_block.show_boundingbox");
/*  38 */   private static final ImmutableList<StructureMode> ALL_MODES = ImmutableList.copyOf((Object[])StructureMode.values()); private static final ImmutableList<StructureMode> DEFAULT_MODES; static {
/*  39 */     DEFAULT_MODES = (ImmutableList<StructureMode>)ALL_MODES.stream().filter(m -> (m != StructureMode.DATA)).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */   private final StructureBlockEntity structure;
/*  42 */   private Mirror initialMirror = Mirror.NONE;
/*  43 */   private Rotation initialRotation = Rotation.NONE;
/*  44 */   private StructureMode initialMode = StructureMode.DATA;
/*     */   
/*     */   private boolean initialEntityIgnoring;
/*     */   
/*     */   private boolean initialStrict;
/*     */   private boolean initialShowAir;
/*     */   private boolean initialShowBoundingBox;
/*     */   private EditBox nameEdit;
/*     */   private EditBox posXEdit;
/*     */   private EditBox posYEdit;
/*     */   private EditBox posZEdit;
/*     */   private EditBox sizeXEdit;
/*     */   private EditBox sizeYEdit;
/*     */   private EditBox sizeZEdit;
/*     */   private EditBox integrityEdit;
/*     */   private EditBox seedEdit;
/*     */   private EditBox dataEdit;
/*     */   private Button saveButton;
/*     */   private Button loadButton;
/*     */   private Button rot0Button;
/*     */   private Button rot90Button;
/*     */   private Button rot180Button;
/*     */   private Button rot270Button;
/*     */   private Button detectButton;
/*     */   private CycleButton<Boolean> includeEntitiesButton;
/*     */   private CycleButton<Boolean> strictButton;
/*     */   private CycleButton<Mirror> mirrorButton;
/*     */   private CycleButton<Boolean> toggleAirButton;
/*     */   private CycleButton<Boolean> toggleBoundingBox;
/*  73 */   private final DecimalFormat decimalFormat = new DecimalFormat("0.0###", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */   
/*     */   public StructureBlockEditScreen(StructureBlockEntity structure) {
/*  76 */     super((Component)Component.translatable(net.minecraft.world.level.block.Blocks.STRUCTURE_BLOCK.getDescriptionId()));
/*  77 */     this.structure = structure;
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  81 */     if (sendToServer(StructureBlockEntity.UpdateType.UPDATE_DATA)) {
/*  82 */       this.minecraft.setScreen(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onCancel() {
/*  87 */     this.structure.setMirror(this.initialMirror);
/*  88 */     this.structure.setRotation(this.initialRotation);
/*  89 */     this.structure.setMode(this.initialMode);
/*  90 */     this.structure.setIgnoreEntities(this.initialEntityIgnoring);
/*  91 */     this.structure.setStrict(this.initialStrict);
/*  92 */     this.structure.setShowAir(this.initialShowAir);
/*  93 */     this.structure.setShowBoundingBox(this.initialShowBoundingBox);
/*  94 */     this.minecraft.setScreen(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  99 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 4 - 150, 210, 150, 20).build());
/* 100 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).bounds(this.width / 2 + 4, 210, 150, 20).build());
/*     */     
/* 102 */     this.initialMirror = this.structure.getMirror();
/* 103 */     this.initialRotation = this.structure.getRotation();
/* 104 */     this.initialMode = this.structure.getMode();
/* 105 */     this.initialEntityIgnoring = this.structure.isIgnoreEntities();
/* 106 */     this.initialStrict = this.structure.isStrict();
/* 107 */     this.initialShowAir = this.structure.getShowAir();
/* 108 */     this.initialShowBoundingBox = this.structure.getShowBoundingBox();
/*     */     
/* 110 */     this.saveButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("structure_block.button.save"), button -> {
/*     */             if (this.structure.getMode() == StructureMode.SAVE) {
/*     */               sendToServer(StructureBlockEntity.UpdateType.SAVE_AREA);
/*     */               this.minecraft.setScreen(null);
/*     */             } 
/* 115 */           }).bounds(this.width / 2 + 4 + 100, 185, 50, 20).build());
/* 116 */     this.loadButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("structure_block.button.load"), button -> {
/*     */             if (this.structure.getMode() == StructureMode.LOAD) {
/*     */               sendToServer(StructureBlockEntity.UpdateType.LOAD_AREA);
/*     */               this.minecraft.setScreen(null);
/*     */             } 
/* 121 */           }).bounds(this.width / 2 + 4 + 100, 185, 50, 20).build());
/* 122 */     addRenderableWidget((GuiEventListener)CycleButton.builder(value -> Component.translatable("structure_block.mode." + value.getSerializedName()), this.initialMode)
/* 123 */         .withValues((List)DEFAULT_MODES, (List)ALL_MODES)
/* 124 */         .displayOnlyValue()
/* 125 */         .create(this.width / 2 - 4 - 150, 185, 50, 20, (Component)Component.literal("MODE"), (button, value) -> {
/*     */             this.structure.setMode(value);
/*     */             
/*     */             updateMode(value);
/*     */           }));
/*     */     
/* 131 */     this.detectButton = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("structure_block.button.detect_size"), button -> {
/*     */             if (this.structure.getMode() == StructureMode.SAVE) {
/*     */               sendToServer(StructureBlockEntity.UpdateType.SCAN_AREA);
/*     */               this.minecraft.setScreen(null);
/*     */             } 
/* 136 */           }).bounds(this.width / 2 + 4 + 100, 120, 50, 20).build());
/* 137 */     this.includeEntitiesButton = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(!this.structure.isIgnoreEntities()).displayOnlyValue().create(this.width / 2 + 4 + 100, 160, 50, 20, INCLUDE_ENTITIES_LABEL, (button, value) -> this.structure.setIgnoreEntities(!value)));
/*     */ 
/*     */ 
/*     */     
/* 141 */     this.strictButton = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(this.structure.isStrict()).displayOnlyValue().create(this.width / 2 + 4 + 100, 120, 50, 20, STRICT_LABEL, (button, value) -> this.structure.setStrict(value)));
/*     */ 
/*     */ 
/*     */     
/* 145 */     this.mirrorButton = (CycleButton<Mirror>)addRenderableWidget((GuiEventListener)CycleButton.builder(Mirror::symbol, this.initialMirror)
/* 146 */         .withValues((Object[])Mirror.values())
/* 147 */         .displayOnlyValue()
/* 148 */         .create(this.width / 2 - 20, 185, 40, 20, (Component)Component.literal("MIRROR"), (button, value) -> this.structure.setMirror(value)));
/*     */ 
/*     */     
/* 151 */     this.toggleAirButton = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(this.structure.getShowAir()).displayOnlyValue().create(this.width / 2 + 4 + 100, 80, 50, 20, SHOW_AIR_LABEL, (button, value) -> this.structure.setShowAir(value)));
/*     */ 
/*     */ 
/*     */     
/* 155 */     this.toggleBoundingBox = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(this.structure.getShowBoundingBox()).displayOnlyValue().create(this.width / 2 + 4 + 100, 80, 50, 20, SHOW_BOUNDING_BOX_LABEL, (button, value) -> this.structure.setShowBoundingBox(value)));
/*     */ 
/*     */ 
/*     */     
/* 159 */     this.rot0Button = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal("0"), button -> {
/*     */             this.structure.setRotation(Rotation.NONE);
/*     */             updateDirectionButtons();
/* 162 */           }).bounds(this.width / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20).build());
/* 163 */     this.rot90Button = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal("90"), button -> {
/*     */             this.structure.setRotation(Rotation.CLOCKWISE_90);
/*     */             updateDirectionButtons();
/* 166 */           }).bounds(this.width / 2 - 1 - 40 - 20, 185, 40, 20).build());
/* 167 */     this.rot180Button = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal("180"), button -> {
/*     */             this.structure.setRotation(Rotation.CLOCKWISE_180);
/*     */             updateDirectionButtons();
/* 170 */           }).bounds(this.width / 2 + 1 + 20, 185, 40, 20).build());
/* 171 */     this.rot270Button = (Button)addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal("270"), button -> {
/*     */             this.structure.setRotation(Rotation.COUNTERCLOCKWISE_90);
/*     */             updateDirectionButtons();
/* 174 */           }).bounds(this.width / 2 + 1 + 40 + 1 + 20, 185, 40, 20).build());
/*     */     
/* 176 */     this.nameEdit = new EditBox(this.font, this.width / 2 - 152, 40, 300, 20, (Component)Component.translatable("structure_block.structure_name"))
/*     */       {
/*     */         public boolean charTyped(CharacterEvent event)
/*     */         {
/* 180 */           if (!StructureBlockEditScreen.this.isValidCharacterForName(getValue(), event.codepoint(), getCursorPosition())) {
/* 181 */             return false;
/*     */           }
/* 183 */           return super.charTyped(event);
/*     */         }
/*     */       };
/* 186 */     this.nameEdit.setMaxLength(128);
/* 187 */     this.nameEdit.setValue(this.structure.getStructureName());
/* 188 */     addWidget((GuiEventListener)this.nameEdit);
/*     */     
/* 190 */     BlockPos minPos = this.structure.getStructurePos();
/* 191 */     this.posXEdit = new EditBox(this.font, this.width / 2 - 152, 80, 80, 20, (Component)Component.translatable("structure_block.position.x"));
/* 192 */     this.posXEdit.setMaxLength(15);
/* 193 */     this.posXEdit.setValue(Integer.toString(minPos.getX()));
/* 194 */     addWidget((GuiEventListener)this.posXEdit);
/* 195 */     this.posYEdit = new EditBox(this.font, this.width / 2 - 72, 80, 80, 20, (Component)Component.translatable("structure_block.position.y"));
/* 196 */     this.posYEdit.setMaxLength(15);
/* 197 */     this.posYEdit.setValue(Integer.toString(minPos.getY()));
/* 198 */     addWidget((GuiEventListener)this.posYEdit);
/* 199 */     this.posZEdit = new EditBox(this.font, this.width / 2 + 8, 80, 80, 20, (Component)Component.translatable("structure_block.position.z"));
/* 200 */     this.posZEdit.setMaxLength(15);
/* 201 */     this.posZEdit.setValue(Integer.toString(minPos.getZ()));
/* 202 */     addWidget((GuiEventListener)this.posZEdit);
/*     */     
/* 204 */     Vec3i maxPos = this.structure.getStructureSize();
/* 205 */     this.sizeXEdit = new EditBox(this.font, this.width / 2 - 152, 120, 80, 20, (Component)Component.translatable("structure_block.size.x"));
/* 206 */     this.sizeXEdit.setMaxLength(15);
/* 207 */     this.sizeXEdit.setValue(Integer.toString(maxPos.getX()));
/* 208 */     addWidget((GuiEventListener)this.sizeXEdit);
/* 209 */     this.sizeYEdit = new EditBox(this.font, this.width / 2 - 72, 120, 80, 20, (Component)Component.translatable("structure_block.size.y"));
/* 210 */     this.sizeYEdit.setMaxLength(15);
/* 211 */     this.sizeYEdit.setValue(Integer.toString(maxPos.getY()));
/* 212 */     addWidget((GuiEventListener)this.sizeYEdit);
/* 213 */     this.sizeZEdit = new EditBox(this.font, this.width / 2 + 8, 120, 80, 20, (Component)Component.translatable("structure_block.size.z"));
/* 214 */     this.sizeZEdit.setMaxLength(15);
/* 215 */     this.sizeZEdit.setValue(Integer.toString(maxPos.getZ()));
/* 216 */     addWidget((GuiEventListener)this.sizeZEdit);
/*     */     
/* 218 */     this.integrityEdit = new EditBox(this.font, this.width / 2 - 152, 120, 80, 20, (Component)Component.translatable("structure_block.integrity.integrity"));
/* 219 */     this.integrityEdit.setMaxLength(15);
/* 220 */     this.integrityEdit.setValue(this.decimalFormat.format(this.structure.getIntegrity()));
/* 221 */     addWidget((GuiEventListener)this.integrityEdit);
/* 222 */     this.seedEdit = new EditBox(this.font, this.width / 2 - 72, 120, 80, 20, (Component)Component.translatable("structure_block.integrity.seed"));
/* 223 */     this.seedEdit.setMaxLength(31);
/* 224 */     this.seedEdit.setValue(Long.toString(this.structure.getSeed()));
/* 225 */     addWidget((GuiEventListener)this.seedEdit);
/*     */     
/* 227 */     this.dataEdit = new EditBox(this.font, this.width / 2 - 152, 120, 240, 20, (Component)Component.translatable("structure_block.custom_data"));
/* 228 */     this.dataEdit.setMaxLength(128);
/* 229 */     this.dataEdit.setValue(this.structure.getMetaData());
/* 230 */     addWidget((GuiEventListener)this.dataEdit);
/*     */     
/* 232 */     updateDirectionButtons();
/* 233 */     updateMode(this.initialMode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 238 */     setInitialFocus((GuiEventListener)this.nameEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 243 */     String oldNameEdit = this.nameEdit.getValue();
/* 244 */     String oldPosXEdit = this.posXEdit.getValue();
/* 245 */     String oldPosYEdit = this.posYEdit.getValue();
/* 246 */     String oldPosZEdit = this.posZEdit.getValue();
/* 247 */     String oldSizeXEdit = this.sizeXEdit.getValue();
/* 248 */     String oldSizeYEdit = this.sizeYEdit.getValue();
/* 249 */     String oldSizeZEdit = this.sizeZEdit.getValue();
/* 250 */     String oldIntegrityEdit = this.integrityEdit.getValue();
/* 251 */     String oldSeedEdit = this.seedEdit.getValue();
/* 252 */     String oldDataEdit = this.dataEdit.getValue();
/*     */     
/* 254 */     init(width, height);
/*     */     
/* 256 */     this.nameEdit.setValue(oldNameEdit);
/* 257 */     this.posXEdit.setValue(oldPosXEdit);
/* 258 */     this.posYEdit.setValue(oldPosYEdit);
/* 259 */     this.posZEdit.setValue(oldPosZEdit);
/* 260 */     this.sizeXEdit.setValue(oldSizeXEdit);
/* 261 */     this.sizeYEdit.setValue(oldSizeYEdit);
/* 262 */     this.sizeZEdit.setValue(oldSizeZEdit);
/* 263 */     this.integrityEdit.setValue(oldIntegrityEdit);
/* 264 */     this.seedEdit.setValue(oldSeedEdit);
/* 265 */     this.dataEdit.setValue(oldDataEdit);
/*     */   }
/*     */   
/*     */   private void updateDirectionButtons() {
/* 269 */     this.rot0Button.active = true;
/* 270 */     this.rot90Button.active = true;
/* 271 */     this.rot180Button.active = true;
/* 272 */     this.rot270Button.active = true;
/*     */     
/* 274 */     switch (this.structure.getRotation()) {
/*     */       case NONE:
/* 276 */         this.rot0Button.active = false;
/*     */         break;
/*     */       case CLOCKWISE_180:
/* 279 */         this.rot180Button.active = false;
/*     */         break;
/*     */       case COUNTERCLOCKWISE_90:
/* 282 */         this.rot270Button.active = false;
/*     */         break;
/*     */       case CLOCKWISE_90:
/* 285 */         this.rot90Button.active = false;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateMode(StructureMode mode) {
/* 291 */     this.nameEdit.setVisible(false);
/* 292 */     this.posXEdit.setVisible(false);
/* 293 */     this.posYEdit.setVisible(false);
/* 294 */     this.posZEdit.setVisible(false);
/* 295 */     this.sizeXEdit.setVisible(false);
/* 296 */     this.sizeYEdit.setVisible(false);
/* 297 */     this.sizeZEdit.setVisible(false);
/* 298 */     this.integrityEdit.setVisible(false);
/* 299 */     this.seedEdit.setVisible(false);
/* 300 */     this.dataEdit.setVisible(false);
/*     */     
/* 302 */     this.saveButton.visible = false;
/* 303 */     this.loadButton.visible = false;
/* 304 */     this.detectButton.visible = false;
/* 305 */     this.includeEntitiesButton.visible = false;
/* 306 */     this.strictButton.visible = false;
/* 307 */     this.mirrorButton.visible = false;
/* 308 */     this.rot0Button.visible = false;
/* 309 */     this.rot90Button.visible = false;
/* 310 */     this.rot180Button.visible = false;
/* 311 */     this.rot270Button.visible = false;
/* 312 */     this.toggleAirButton.visible = false;
/* 313 */     this.toggleBoundingBox.visible = false;
/*     */     
/* 315 */     switch (mode) {
/*     */       case SAVE:
/* 317 */         this.nameEdit.setVisible(true);
/* 318 */         this.posXEdit.setVisible(true);
/* 319 */         this.posYEdit.setVisible(true);
/* 320 */         this.posZEdit.setVisible(true);
/* 321 */         this.sizeXEdit.setVisible(true);
/* 322 */         this.sizeYEdit.setVisible(true);
/* 323 */         this.sizeZEdit.setVisible(true);
/* 324 */         this.saveButton.visible = true;
/* 325 */         this.detectButton.visible = true;
/* 326 */         this.includeEntitiesButton.visible = true;
/* 327 */         this.strictButton.visible = false;
/* 328 */         this.toggleAirButton.visible = true;
/*     */         break;
/*     */       case LOAD:
/* 331 */         this.nameEdit.setVisible(true);
/* 332 */         this.posXEdit.setVisible(true);
/* 333 */         this.posYEdit.setVisible(true);
/* 334 */         this.posZEdit.setVisible(true);
/* 335 */         this.integrityEdit.setVisible(true);
/* 336 */         this.seedEdit.setVisible(true);
/* 337 */         this.loadButton.visible = true;
/* 338 */         this.includeEntitiesButton.visible = true;
/* 339 */         this.strictButton.visible = true;
/* 340 */         this.mirrorButton.visible = true;
/* 341 */         this.rot0Button.visible = true;
/* 342 */         this.rot90Button.visible = true;
/* 343 */         this.rot180Button.visible = true;
/* 344 */         this.rot270Button.visible = true;
/* 345 */         this.toggleBoundingBox.visible = true;
/* 346 */         updateDirectionButtons();
/*     */         break;
/*     */       case CORNER:
/* 349 */         this.nameEdit.setVisible(true);
/*     */         break;
/*     */       case DATA:
/* 352 */         this.dataEdit.setVisible(true);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean sendToServer(StructureBlockEntity.UpdateType updateType) {
/* 358 */     BlockPos offset = new BlockPos(parseCoordinate(this.posXEdit.getValue()), parseCoordinate(this.posYEdit.getValue()), parseCoordinate(this.posZEdit.getValue()));
/* 359 */     Vec3i size = new Vec3i(parseCoordinate(this.sizeXEdit.getValue()), parseCoordinate(this.sizeYEdit.getValue()), parseCoordinate(this.sizeZEdit.getValue()));
/* 360 */     float integrity = parseIntegrity(this.integrityEdit.getValue());
/* 361 */     long seed = parseSeed(this.seedEdit.getValue());
/* 362 */     this.minecraft.getConnection().send((Packet)new net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket(this.structure.getBlockPos(), updateType, this.structure.getMode(), this.nameEdit.getValue(), offset, size, this.structure.getMirror(), this.structure.getRotation(), this.dataEdit.getValue(), this.structure.isIgnoreEntities(), this.structure.isStrict(), this.structure.getShowAir(), this.structure.getShowBoundingBox(), integrity, seed));
/* 363 */     return true;
/*     */   }
/*     */   
/*     */   private long parseSeed(String value) {
/*     */     try {
/* 368 */       return Long.valueOf(value);
/* 369 */     } catch (NumberFormatException ignored) {
/* 370 */       return 0L;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float parseIntegrity(String value) {
/*     */     try {
/* 376 */       return Float.valueOf(value);
/* 377 */     } catch (NumberFormatException ignored) {
/* 378 */       return 1.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int parseCoordinate(String value) {
/*     */     try {
/* 384 */       return Integer.parseInt(value);
/* 385 */     } catch (NumberFormatException ignored) {
/* 386 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 392 */     onCancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 397 */     if (super.keyPressed(event)) {
/* 398 */       return true;
/*     */     }
/*     */     
/* 401 */     if (event.isConfirmation()) {
/* 402 */       onDone();
/* 403 */       return true;
/*     */     } 
/*     */     
/* 406 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 411 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 413 */     StructureMode mode = this.structure.getMode();
/* 414 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, -1);
/*     */     
/* 416 */     if (mode != StructureMode.DATA) {
/* 417 */       graphics.drawString(this.font, NAME_LABEL, this.width / 2 - 153, 30, -6250336);
/* 418 */       this.nameEdit.render(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */     
/* 421 */     if (mode == StructureMode.LOAD || mode == StructureMode.SAVE) {
/* 422 */       graphics.drawString(this.font, POSITION_LABEL, this.width / 2 - 153, 70, -6250336);
/* 423 */       this.posXEdit.render(graphics, mouseX, mouseY, a);
/* 424 */       this.posYEdit.render(graphics, mouseX, mouseY, a);
/* 425 */       this.posZEdit.render(graphics, mouseX, mouseY, a);
/*     */       
/* 427 */       graphics.drawString(this.font, INCLUDE_ENTITIES_LABEL, this.width / 2 + 154 - this.font.width((FormattedText)INCLUDE_ENTITIES_LABEL), 150, -6250336);
/*     */     } 
/*     */     
/* 430 */     if (mode == StructureMode.SAVE) {
/* 431 */       graphics.drawString(this.font, SIZE_LABEL, this.width / 2 - 153, 110, -6250336);
/* 432 */       this.sizeXEdit.render(graphics, mouseX, mouseY, a);
/* 433 */       this.sizeYEdit.render(graphics, mouseX, mouseY, a);
/* 434 */       this.sizeZEdit.render(graphics, mouseX, mouseY, a);
/*     */       
/* 436 */       graphics.drawString(this.font, DETECT_SIZE_LABEL, this.width / 2 + 154 - this.font.width((FormattedText)DETECT_SIZE_LABEL), 110, -6250336);
/* 437 */       graphics.drawString(this.font, SHOW_AIR_LABEL, this.width / 2 + 154 - this.font.width((FormattedText)SHOW_AIR_LABEL), 70, -6250336);
/*     */     } 
/*     */     
/* 440 */     if (mode == StructureMode.LOAD) {
/* 441 */       graphics.drawString(this.font, INTEGRITY_LABEL, this.width / 2 - 153, 110, -6250336);
/* 442 */       this.integrityEdit.render(graphics, mouseX, mouseY, a);
/* 443 */       this.seedEdit.render(graphics, mouseX, mouseY, a);
/* 444 */       graphics.drawString(this.font, STRICT_LABEL, this.width / 2 + 154 - this.font.width((FormattedText)STRICT_LABEL), 110, -6250336);
/* 445 */       graphics.drawString(this.font, SHOW_BOUNDING_BOX_LABEL, this.width / 2 + 154 - this.font.width((FormattedText)SHOW_BOUNDING_BOX_LABEL), 70, -6250336);
/*     */     } 
/*     */     
/* 448 */     if (mode == StructureMode.DATA) {
/* 449 */       graphics.drawString(this.font, CUSTOM_DATA_LABEL, this.width / 2 - 153, 110, -6250336);
/* 450 */       this.dataEdit.render(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */     
/* 453 */     graphics.drawString(this.font, mode.getDisplayName(), this.width / 2 - 153, 174, -6250336);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 458 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 463 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/StructureBlockEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */