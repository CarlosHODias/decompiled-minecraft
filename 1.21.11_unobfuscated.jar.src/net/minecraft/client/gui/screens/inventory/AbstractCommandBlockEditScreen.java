/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommandSuggestions;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ 
/*     */ public abstract class AbstractCommandBlockEditScreen extends Screen {
/*  20 */   private static final Component SET_COMMAND_LABEL = (Component)Component.translatable("advMode.setCommand");
/*  21 */   private static final Component COMMAND_LABEL = (Component)Component.translatable("advMode.command");
/*  22 */   private static final Component PREVIOUS_OUTPUT_LABEL = (Component)Component.translatable("advMode.previousOutput");
/*     */   
/*     */   protected EditBox commandEdit;
/*     */   
/*     */   protected EditBox previousEdit;
/*     */   protected Button doneButton;
/*     */   protected Button cancelButton;
/*     */   protected CycleButton<Boolean> outputButton;
/*     */   private CommandSuggestions commandSuggestions;
/*     */   
/*     */   public AbstractCommandBlockEditScreen() {
/*  33 */     super(GameNarrator.NO_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  38 */     if (!getCommandBlock().isValid()) {
/*  39 */       onClose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/*  49 */     boolean trackOutput = getCommandBlock().isTrackOutput();
/*     */     
/*  51 */     this.commandEdit = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, (Component)Component.translatable("advMode.command"))
/*     */       {
/*     */         protected MutableComponent createNarrationMessage() {
/*  54 */           return super.createNarrationMessage().append(AbstractCommandBlockEditScreen.this.commandSuggestions.getNarrationMessage());
/*     */         }
/*     */       };
/*  57 */     this.commandEdit.setMaxLength(32500);
/*  58 */     this.commandEdit.setResponder(this::onEdited);
/*  59 */     addWidget((GuiEventListener)this.commandEdit);
/*     */     
/*  61 */     this.previousEdit = new EditBox(this.font, this.width / 2 - 150, getPreviousY(), 276, 20, (Component)Component.translatable("advMode.previousOutput"));
/*  62 */     this.previousEdit.setMaxLength(32500);
/*  63 */     this.previousEdit.setEditable(false);
/*  64 */     this.previousEdit.setValue("-");
/*  65 */     addWidget((GuiEventListener)this.previousEdit);
/*     */     
/*  67 */     this.outputButton = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.booleanBuilder((Component)Component.literal("O"), (Component)Component.literal("X"), trackOutput)
/*  68 */         .displayOnlyValue()
/*  69 */         .create(this.width / 2 + 150 - 20, getPreviousY(), 20, 20, (Component)Component.translatable("advMode.trackOutput"), (button, value) -> {
/*     */             BaseCommandBlock commandBlock = getCommandBlock();
/*     */             
/*     */             commandBlock.setTrackOutput(value);
/*     */             
/*     */             updatePreviousOutput(value);
/*     */           }));
/*  76 */     addExtraControls();
/*  77 */     this.doneButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20).build());
/*  78 */     this.cancelButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).bounds(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20).build());
/*     */     
/*  80 */     this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.commandEdit, this.font, true, true, 0, 7, false, Integer.MIN_VALUE);
/*  81 */     this.commandSuggestions.setAllowSuggestions(true);
/*  82 */     this.commandSuggestions.updateCommandInfo();
/*     */     
/*  84 */     updatePreviousOutput(trackOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addExtraControls() {}
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  92 */     setInitialFocus((GuiEventListener)this.commandEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getUsageNarration() {
/*  97 */     if (this.commandSuggestions.isVisible()) {
/*  98 */       return this.commandSuggestions.getUsageNarration();
/*     */     }
/* 100 */     return super.getUsageNarration();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 105 */     String oldText = this.commandEdit.getValue();
/* 106 */     init(width, height);
/* 107 */     this.commandEdit.setValue(oldText);
/*     */     
/* 109 */     this.commandSuggestions.updateCommandInfo();
/*     */   }
/*     */   
/*     */   protected void updatePreviousOutput(boolean isTracking) {
/* 113 */     this.previousEdit.setValue(isTracking ? getCommandBlock().getLastOutput().getString() : "-");
/*     */   }
/*     */   
/*     */   protected void onDone() {
/* 117 */     populateAndSendPacket();
/*     */     
/* 119 */     BaseCommandBlock commandBlock = getCommandBlock();
/* 120 */     if (!commandBlock.isTrackOutput()) {
/* 121 */       commandBlock.setLastOutput(null);
/*     */     }
/* 123 */     this.minecraft.setScreen(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void onEdited(String value) {
/* 129 */     this.commandSuggestions.updateCommandInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 140 */     if (this.commandSuggestions.keyPressed(event)) {
/* 141 */       return true;
/*     */     }
/*     */     
/* 144 */     if (super.keyPressed(event)) {
/* 145 */       return true;
/*     */     }
/*     */     
/* 148 */     if (event.isConfirmation()) {
/* 149 */       onDone();
/* 150 */       return true;
/*     */     } 
/*     */     
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 158 */     if (this.commandSuggestions.mouseScrolled(scrollY)) {
/* 159 */       return true;
/*     */     }
/*     */     
/* 162 */     return super.mouseScrolled(x, y, scrollX, scrollY);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 167 */     if (this.commandSuggestions.mouseClicked(event)) {
/* 168 */       return true;
/*     */     }
/*     */     
/* 171 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 176 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 178 */     graphics.drawCenteredString(this.font, SET_COMMAND_LABEL, this.width / 2, 20, -1);
/* 179 */     graphics.drawString(this.font, COMMAND_LABEL, this.width / 2 - 150 + 1, 40, -6250336);
/* 180 */     this.commandEdit.render(graphics, mouseX, mouseY, a);
/*     */     
/* 182 */     int y = 75;
/* 183 */     if (!this.previousEdit.getValue().isEmpty()) {
/* 184 */       Objects.requireNonNull(this.font); y += 5 * 9 + 1 + getPreviousY() - 135;
/* 185 */       graphics.drawString(this.font, PREVIOUS_OUTPUT_LABEL, this.width / 2 - 150 + 1, y + 4, -6250336);
/* 186 */       this.previousEdit.render(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */     
/* 189 */     this.commandSuggestions.render(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   abstract BaseCommandBlock getCommandBlock();
/*     */   
/*     */   abstract int getPreviousY();
/*     */   
/*     */   protected abstract void populateAndSendPacket();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AbstractCommandBlockEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */