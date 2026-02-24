/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.font.TextFieldHelper;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.level.block.SignBlock;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public abstract class AbstractSignEditScreen
/*     */   extends Screen {
/*     */   protected final SignBlockEntity sign;
/*     */   private SignText text;
/*     */   private final String[] messages;
/*     */   private final boolean isFrontText;
/*     */   protected final WoodType woodType;
/*     */   private int frame;
/*     */   private int line;
/*     */   private TextFieldHelper signField;
/*     */   
/*     */   public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
/*  36 */     this(sign, isFrontText, shouldFilter, (Component)Component.translatable("sign.edit"));
/*     */   }
/*     */   
/*     */   public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter, Component title) {
/*  40 */     super(title);
/*     */     
/*  42 */     this.sign = sign;
/*  43 */     this.text = sign.getText(isFrontText);
/*  44 */     this.isFrontText = isFrontText;
/*  45 */     this.woodType = SignBlock.getWoodType(sign.getBlockState().getBlock());
/*     */     
/*  47 */     this.messages = (String[])IntStream.range(0, 4).mapToObj(index -> this.text.getMessage(shouldFilter, shouldFilter)).map(Component::getString).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  52 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build());
/*     */     
/*  54 */     this
/*     */ 
/*     */ 
/*     */       
/*  58 */       .signField = new TextFieldHelper(() -> this.messages[this.line], this::setMessage, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), s -> (this.minecraft.font.width(s) <= this.sign.getMaxTextLineWidth()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  65 */     this.frame++;
/*     */     
/*  67 */     if (!isValid()) {
/*  68 */       onDone();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isValid() {
/*  73 */     return (this.minecraft.player != null && 
/*  74 */       !this.sign.isRemoved() && 
/*  75 */       !this.sign.playerIsTooFarAwayToEdit(this.minecraft.player.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  80 */     if (event.isUp()) {
/*  81 */       this.line = this.line - 1 & 0x3;
/*  82 */       this.signField.setCursorToEnd();
/*  83 */       return true;
/*     */     } 
/*  85 */     if (event.isDown() || event.isConfirmation()) {
/*  86 */       this.line = this.line + 1 & 0x3;
/*  87 */       this.signField.setCursorToEnd();
/*  88 */       return true;
/*     */     } 
/*  90 */     if (this.signField.keyPressed(event)) {
/*  91 */       return true;
/*     */     }
/*     */     
/*  94 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/*  99 */     this.signField.charTyped(event);
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 105 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 107 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, -1);
/* 108 */     renderSign(graphics);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 113 */     onDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 118 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 119 */     if (connection != null) {
/* 120 */       connection.send((Packet)new ServerboundSignUpdatePacket(this.sign.getBlockPos(), this.isFrontText, this.messages[0], this.messages[1], this.messages[2], this.messages[3]));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderSign(GuiGraphics graphics) {
/* 141 */     graphics.pose().pushMatrix();
/* 142 */     graphics.pose().translate(this.width / 2.0F, getSignYOffset());
/*     */     
/* 144 */     graphics.pose().pushMatrix();
/* 145 */     renderSignBackground(graphics);
/* 146 */     graphics.pose().popMatrix();
/*     */     
/* 148 */     renderSignText(graphics);
/* 149 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   private void renderSignText(GuiGraphics graphics) {
/* 153 */     Vector3f textScale = getSignTextScale();
/* 154 */     graphics.pose().scale(textScale.x(), textScale.y());
/* 155 */     int color = this.text.hasGlowingText() ? this.text.getColor().getTextColor() : AbstractSignRenderer.getDarkColor(this.text);
/* 156 */     boolean showCursor = (this.frame / 6 % 2 == 0);
/* 157 */     int cursorPos = this.signField.getCursorPos();
/* 158 */     int selectionPos = this.signField.getSelectionPos();
/*     */     
/* 160 */     int signMidpoint = 4 * this.sign.getTextLineHeight() / 2;
/* 161 */     int yPosition = this.line * this.sign.getTextLineHeight() - signMidpoint;
/*     */     
/* 163 */     for (int i = 0; i < this.messages.length; i++) {
/* 164 */       String line = this.messages[i];
/* 165 */       if (line != null) {
/*     */ 
/*     */         
/* 168 */         if (this.font.isBidirectional()) {
/* 169 */           line = this.font.bidirectionalShaping(line);
/*     */         }
/*     */         
/* 172 */         int x1 = -this.font.width(line) / 2;
/* 173 */         graphics.drawString(this.font, line, x1, i * this.sign.getTextLineHeight() - signMidpoint, color, false);
/*     */ 
/*     */         
/* 176 */         if (i == this.line && cursorPos >= 0 && showCursor) {
/*     */ 
/*     */ 
/*     */           
/* 180 */           int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
/* 181 */           int xPosition = cursorPosition - this.font.width(line) / 2;
/*     */           
/* 183 */           if (cursorPos >= line.length())
/* 184 */             graphics.drawString(this.font, "_", xPosition, yPosition, color, false); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 188 */     for (int j = 0; j < this.messages.length; j++) {
/* 189 */       String line = this.messages[j];
/* 190 */       if (line != null && j == this.line && cursorPos >= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 195 */         int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
/* 196 */         int xPosition = cursorPosition - this.font.width(line) / 2;
/*     */         
/* 198 */         if (showCursor && cursorPos < line.length()) {
/* 199 */           graphics.fill(xPosition, yPosition - 1, xPosition + 1, yPosition + this.sign.getTextLineHeight(), ARGB.opaque(color));
/*     */         }
/*     */         
/* 202 */         if (selectionPos != cursorPos) {
/* 203 */           int startIndex = Math.min(cursorPos, selectionPos);
/* 204 */           int endIndex = Math.max(cursorPos, selectionPos);
/* 205 */           int startPosX = this.font.width(line.substring(0, startIndex)) - this.font.width(line) / 2;
/* 206 */           int endPosX = this.font.width(line.substring(0, endIndex)) - this.font.width(line) / 2;
/*     */           
/* 208 */           int fromX = Math.min(startPosX, endPosX);
/* 209 */           int toX = Math.max(startPosX, endPosX);
/*     */           
/* 211 */           graphics.textHighlight(fromX, yPosition, toX, yPosition + this.sign.getTextLineHeight(), true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void setMessage(String message) {
/* 217 */     this.messages[this.line] = message;
/* 218 */     this.text = this.text.setMessage(this.line, (Component)Component.literal(message));
/* 219 */     this.sign.setText(this.text, this.isFrontText);
/*     */   }
/*     */   
/*     */   private void onDone() {
/* 223 */     this.minecraft.setScreen(null);
/*     */   }
/*     */   
/*     */   protected abstract void renderSignBackground(GuiGraphics paramGuiGraphics);
/*     */   
/*     */   protected abstract Vector3f getSignTextScale();
/*     */   
/*     */   protected abstract float getSignYOffset();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/AbstractSignEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */