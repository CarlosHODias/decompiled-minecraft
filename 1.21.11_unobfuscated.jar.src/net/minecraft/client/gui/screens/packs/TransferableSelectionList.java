/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.SelectableEntry;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.PackCompatibility;
/*     */ 
/*     */ public class TransferableSelectionList extends ObjectSelectionList<TransferableSelectionList.Entry> {
/*  27 */   private static final Identifier SELECT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/select_highlighted");
/*  28 */   private static final Identifier SELECT_SPRITE = Identifier.withDefaultNamespace("transferable_list/select");
/*  29 */   private static final Identifier UNSELECT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/unselect_highlighted");
/*  30 */   private static final Identifier UNSELECT_SPRITE = Identifier.withDefaultNamespace("transferable_list/unselect");
/*  31 */   private static final Identifier MOVE_UP_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_up_highlighted");
/*  32 */   private static final Identifier MOVE_UP_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_up");
/*  33 */   private static final Identifier MOVE_DOWN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_down_highlighted");
/*  34 */   private static final Identifier MOVE_DOWN_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_down");
/*  35 */   private static final Component INCOMPATIBLE_TITLE = (Component)Component.translatable("pack.incompatible");
/*  36 */   private static final Component INCOMPATIBLE_CONFIRM_TITLE = (Component)Component.translatable("pack.incompatible.confirm.title");
/*     */   
/*     */   private static final int ENTRY_PADDING = 2;
/*     */   private final Component title;
/*     */   private final PackSelectionScreen screen;
/*     */   
/*     */   public TransferableSelectionList(Minecraft minecraft, PackSelectionScreen screen, int width, int height, Component title) {
/*  43 */     super(minecraft, width, height, 33, 36);
/*  44 */     this.screen = screen;
/*  45 */     this.title = title;
/*  46 */     this.centerListVertically = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowWidth() {
/*  51 */     return this.width - 4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int scrollBarX() {
/*  56 */     return getRight() - 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  61 */     if (getSelected() != null) {
/*  62 */       return ((Entry)getSelected()).keyPressed(event);
/*     */     }
/*  64 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void updateList(Stream<PackSelectionModel.Entry> entries, PackSelectionModel.EntryBase transferredEntry) {
/*  68 */     clearEntries();
/*  69 */     MutableComponent mutableComponent = Component.empty().append(this.title).withStyle(new ChatFormatting[] { ChatFormatting.UNDERLINE, ChatFormatting.BOLD });
/*  70 */     Objects.requireNonNull(this.minecraft.font); addEntry((AbstractSelectionList.Entry)new HeaderEntry(this, this.minecraft.font, (Component)mutableComponent), (int)(9.0F * 1.5F));
/*  71 */     setSelected(null);
/*  72 */     entries.forEach(e -> {
/*     */           PackEntry entry = new PackEntry(this.minecraft, this, transferredEntry);
/*     */           addEntry((AbstractSelectionList.Entry)entry);
/*     */           if (transferredEntry != null && transferredEntry.getId().equals(transferredEntry.getId())) {
/*     */             this.screen.setFocused((GuiEventListener)this);
/*     */             setFocused((GuiEventListener)entry);
/*     */           } 
/*     */         });
/*  80 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public abstract class Entry
/*     */     extends ObjectSelectionList.Entry<Entry> {
/*     */     public int getWidth() {
/*  86 */       return super.getWidth() - (TransferableSelectionList.this.scrollbarVisible() ? 6 : 0);
/*     */     }
/*     */     
/*     */     public abstract String getPackId();
/*     */   }
/*     */   
/*     */   public class PackEntry
/*     */     extends Entry
/*     */     implements SelectableEntry {
/*     */     private static final int MAX_DESCRIPTION_WIDTH_PIXELS = 157;
/*     */     public static final int ICON_SIZE = 32;
/*     */     private final TransferableSelectionList parent;
/*     */     protected final Minecraft minecraft;
/*     */     private final PackSelectionModel.Entry pack;
/*     */     private final StringWidget nameWidget;
/*     */     private final MultiLineTextWidget descriptionWidget;
/*     */     
/*     */     public PackEntry(Minecraft minecraft, TransferableSelectionList parent, PackSelectionModel.Entry pack) {
/* 104 */       this.minecraft = minecraft;
/* 105 */       this.pack = pack;
/* 106 */       this.parent = parent;
/*     */       
/* 108 */       this.nameWidget = new StringWidget(pack.getTitle(), minecraft.font);
/* 109 */       this.descriptionWidget = new MultiLineTextWidget(ComponentUtils.mergeStyles(pack.getExtendedDescription(), Style.EMPTY.withColor(-8355712)), minecraft.font);
/* 110 */       this.descriptionWidget.setMaxRows(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 115 */       return (Component)Component.translatable("narrator.select", new Object[] { this.pack.getTitle() });
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 120 */       PackCompatibility compatibility = this.pack.getCompatibility();
/* 121 */       if (!compatibility.isCompatible()) {
/* 122 */         int x0 = getContentX() - 1;
/* 123 */         int y0 = getContentY() - 1;
/* 124 */         int x1 = getContentRight() + 1;
/* 125 */         int y1 = getContentBottom() + 1;
/* 126 */         graphics.fill(x0, y0, x1, y1, -8978432);
/*     */       } 
/*     */       
/* 129 */       graphics.blit(RenderPipelines.GUI_TEXTURED, this.pack.getIconTexture(), getContentX(), getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
/*     */       
/* 131 */       if (!this.nameWidget.getMessage().equals(this.pack.getTitle())) {
/* 132 */         this.nameWidget.setMessage(this.pack.getTitle());
/*     */       }
/* 134 */       if (!this.descriptionWidget.getMessage().getContents().equals(this.pack.getExtendedDescription().getContents())) {
/* 135 */         this.descriptionWidget.setMessage(ComponentUtils.mergeStyles(this.pack.getExtendedDescription(), Style.EMPTY.withColor(-8355712)));
/*     */       }
/*     */       
/* 138 */       if (showHoverOverlay() && ((Boolean)this.minecraft.options.touchscreen().get() || hovered || (this.parent.getSelected() == this && this.parent.isFocused()))) {
/* 139 */         graphics.fill(getContentX(), getContentY(), getContentX() + 32, getContentY() + 32, -1601138544);
/*     */         
/* 141 */         int relX = mouseX - getContentX();
/* 142 */         int relY = mouseY - getContentY();
/*     */         
/* 144 */         if (!this.pack.getCompatibility().isCompatible()) {
/* 145 */           this.nameWidget.setMessage(TransferableSelectionList.INCOMPATIBLE_TITLE);
/* 146 */           this.descriptionWidget.setMessage(this.pack.getCompatibility().getDescription());
/*     */         } 
/*     */         
/* 149 */         if (this.pack.canSelect()) {
/* 150 */           if (mouseOverIcon(relX, relY, 32)) {
/* 151 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.SELECT_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 152 */             TransferableSelectionList.this.handleCursor(graphics);
/*     */           } else {
/* 154 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.SELECT_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */           } 
/*     */         } else {
/* 157 */           if (this.pack.canUnselect()) {
/* 158 */             if (mouseOverLeftHalf(relX, relY, 32)) {
/* 159 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.UNSELECT_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 160 */               TransferableSelectionList.this.handleCursor(graphics);
/*     */             } else {
/* 162 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.UNSELECT_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */             } 
/*     */           }
/* 165 */           if (this.pack.canMoveUp()) {
/* 166 */             if (mouseOverTopRightQuarter(relX, relY, 32)) {
/* 167 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_UP_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 168 */               TransferableSelectionList.this.handleCursor(graphics);
/*     */             } else {
/* 170 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_UP_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */             } 
/*     */           }
/* 173 */           if (this.pack.canMoveDown()) {
/* 174 */             if (mouseOverBottomRightQuarter(relX, relY, 32)) {
/* 175 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_DOWN_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 176 */               TransferableSelectionList.this.handleCursor(graphics);
/*     */             } else {
/* 178 */               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_DOWN_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 184 */       this.nameWidget.setMaxWidth(157 - (TransferableSelectionList.this.scrollbarVisible() ? 6 : 0));
/* 185 */       this.nameWidget.setPosition(getContentX() + 32 + 2, getContentY() + 1);
/* 186 */       this.nameWidget.render(graphics, mouseX, mouseY, a);
/*     */       
/* 188 */       this.descriptionWidget.setMaxWidth(157 - (TransferableSelectionList.this.scrollbarVisible() ? 6 : 0));
/* 189 */       this.descriptionWidget.setPosition(getContentX() + 32 + 2, getContentY() + 12);
/* 190 */       this.descriptionWidget.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 195 */       if (showHoverOverlay()) {
/* 196 */         int relX = (int)event.x() - getContentX();
/* 197 */         int relY = (int)event.y() - getContentY();
/*     */         
/* 199 */         if (this.pack.canSelect() && mouseOverIcon(relX, relY, 32)) {
/* 200 */           handlePackSelection();
/* 201 */           return true;
/* 202 */         }  if (this.pack.canUnselect() && mouseOverLeftHalf(relX, relY, 32)) {
/* 203 */           this.pack.unselect();
/* 204 */           return true;
/*     */         } 
/* 206 */         if (this.pack.canMoveUp() && mouseOverTopRightQuarter(relX, relY, 32)) {
/* 207 */           this.pack.moveUp();
/* 208 */           return true;
/*     */         } 
/* 210 */         if (this.pack.canMoveDown() && mouseOverBottomRightQuarter(relX, relY, 32)) {
/* 211 */           this.pack.moveDown();
/* 212 */           return true;
/*     */         } 
/*     */       } 
/* 215 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 220 */       if (event.isConfirmation()) {
/* 221 */         keyboardSelection();
/* 222 */         return true;
/*     */       } 
/* 224 */       if (event.hasShiftDown()) {
/* 225 */         if (event.isUp()) {
/* 226 */           keyboardMoveUp();
/* 227 */           return true;
/*     */         } 
/* 229 */         if (event.isDown()) {
/* 230 */           keyboardMoveDown();
/* 231 */           return true;
/*     */         } 
/*     */       } 
/* 234 */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     private boolean showHoverOverlay() {
/* 238 */       return (!this.pack.isFixedPosition() || !this.pack.isRequired());
/*     */     }
/*     */     
/*     */     public void keyboardSelection() {
/* 242 */       if (this.pack.canSelect()) {
/* 243 */         handlePackSelection();
/* 244 */       } else if (this.pack.canUnselect()) {
/* 245 */         this.pack.unselect();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void keyboardMoveUp() {
/* 250 */       if (this.pack.canMoveUp()) {
/* 251 */         this.pack.moveUp();
/*     */       }
/*     */     }
/*     */     
/*     */     private void keyboardMoveDown() {
/* 256 */       if (this.pack.canMoveDown()) {
/* 257 */         this.pack.moveDown();
/*     */       }
/*     */     }
/*     */     
/*     */     private void handlePackSelection() {
/* 262 */       if (this.pack.getCompatibility().isCompatible()) {
/* 263 */         this.pack.select();
/*     */       } else {
/* 265 */         Component reason = this.pack.getCompatibility().getConfirmation();
/* 266 */         this.minecraft.setScreen((Screen)new ConfirmScreen(result -> { this.minecraft.setScreen(this.parent.screen); if (result) this.pack.select();  }, TransferableSelectionList.INCOMPATIBLE_CONFIRM_TITLE, reason));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPackId() {
/* 277 */       return this.pack.getId();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldTakeFocusAfterInteraction() {
/* 282 */       return TransferableSelectionList.this.children().stream().anyMatch(entry -> entry.getPackId().equals(getPackId()));
/*     */     } }
/*     */   
/*     */   public class HeaderEntry extends Entry {
/*     */     private final Font font;
/*     */     private final Component text;
/*     */     
/*     */     public HeaderEntry(TransferableSelectionList this$0, Font font, Component text) {
/* 290 */       super(this$0);
/* 291 */       this.font = font;
/* 292 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 297 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, this.text, getX() + getWidth() / 2, getContentYMiddle() - 9 / 2, -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 302 */       return this.text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPackId() {
/* 307 */       return "";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/packs/TransferableSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */