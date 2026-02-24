/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportReason;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportType;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ 
/*     */ public class ReportReasonSelectionScreen extends Screen {
/*  26 */   private static final Component REASON_TITLE = (Component)Component.translatable("gui.abuseReport.reason.title");
/*  27 */   private static final Component REASON_DESCRIPTION = (Component)Component.translatable("gui.abuseReport.reason.description");
/*  28 */   private static final Component READ_INFO_LABEL = (Component)Component.translatable("gui.abuseReport.read_info");
/*     */   
/*     */   private static final int DESCRIPTION_BOX_WIDTH = 320;
/*     */   
/*     */   private static final int DESCRIPTION_BOX_HEIGHT = 62;
/*     */   
/*     */   private static final int PADDING = 4;
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private ReasonSelectionList reasonSelectionList;
/*     */   private ReportReason currentlySelectedReason;
/*     */   private final Consumer<ReportReason> onSelectedReason;
/*  41 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final ReportType reportType;
/*     */   
/*     */   public ReportReasonSelectionScreen(Screen lastScreen, ReportReason selectedReason, ReportType reportType, Consumer<ReportReason> onSelectedReason) {
/*  46 */     super(REASON_TITLE);
/*  47 */     this.lastScreen = lastScreen;
/*  48 */     this.currentlySelectedReason = selectedReason;
/*  49 */     this.onSelectedReason = onSelectedReason;
/*  50 */     this.reportType = reportType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  55 */     this.layout.addTitleHeader(REASON_TITLE, this.font);
/*  56 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical().spacing(4));
/*  57 */     this.reasonSelectionList = (ReasonSelectionList)content.addChild((LayoutElement)new ReasonSelectionList(this.minecraft));
/*  58 */     Objects.requireNonNull(this.reasonSelectionList); ReasonSelectionList.Entry selectedEntry = (ReasonSelectionList.Entry)Optionull.map(this.currentlySelectedReason, this.reasonSelectionList::findEntry);
/*  59 */     this.reasonSelectionList.setSelected(selectedEntry);
/*  60 */     content.addChild((LayoutElement)net.minecraft.client.gui.layouts.SpacerElement.height(descriptionHeight()));
/*     */     
/*  62 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  63 */     footer.addChild((LayoutElement)Button.builder(READ_INFO_LABEL, ConfirmLinkScreen.confirmLink(this, net.minecraft.util.CommonLinks.REPORTING_HELP)).build());
/*  64 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> {
/*     */             ReasonSelectionList.Entry selected = (ReasonSelectionList.Entry)this.reasonSelectionList.getSelected();
/*     */             if (selected != null) {
/*     */               this.onSelectedReason.accept(selected.getReason());
/*     */             }
/*     */             this.minecraft.setScreen(this.lastScreen);
/*  70 */           }).build());
/*     */     
/*  72 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  73 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  78 */     this.layout.arrangeElements();
/*  79 */     if (this.reasonSelectionList != null) {
/*  80 */       this.reasonSelectionList.updateSizeAndPosition(this.width, listHeight(), this.layout.getHeaderHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  86 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  88 */     graphics.fill(descriptionLeft(), descriptionTop(), descriptionRight(), descriptionBottom(), -16777216);
/*  89 */     graphics.renderOutline(descriptionLeft(), descriptionTop(), descriptionWidth(), descriptionHeight(), -1);
/*  90 */     graphics.drawString(this.font, REASON_DESCRIPTION, descriptionLeft() + 4, descriptionTop() + 4, -1);
/*     */     
/*  92 */     ReasonSelectionList.Entry selectedEntry = (ReasonSelectionList.Entry)this.reasonSelectionList.getSelected();
/*  93 */     if (selectedEntry != null) {
/*  94 */       int textLeft = descriptionLeft() + 4 + 16;
/*  95 */       int textRight = descriptionRight() - 4;
/*  96 */       Objects.requireNonNull(this.font); int textTop = descriptionTop() + 4 + 9 + 2;
/*  97 */       int textBottom = descriptionBottom() - 4;
/*     */       
/*  99 */       int textWidth = textRight - textLeft;
/* 100 */       int textHeight = textBottom - textTop;
/*     */       
/* 102 */       int contentHeight = this.font.wordWrapHeight((FormattedText)selectedEntry.reason.description(), textWidth);
/* 103 */       graphics.drawWordWrap(this.font, (FormattedText)selectedEntry.reason.description(), textLeft, textTop + (textHeight - contentHeight) / 2, textWidth, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int descriptionLeft() {
/* 108 */     return (this.width - 320) / 2;
/*     */   }
/*     */   
/*     */   private int descriptionRight() {
/* 112 */     return (this.width + 320) / 2;
/*     */   }
/*     */   
/*     */   private int descriptionTop() {
/* 116 */     return descriptionBottom() - descriptionHeight();
/*     */   }
/*     */   
/*     */   private int descriptionBottom() {
/* 120 */     return this.height - this.layout.getFooterHeight() - 4;
/*     */   }
/*     */   
/*     */   private int descriptionWidth() {
/* 124 */     return 320;
/*     */   }
/*     */   
/*     */   private int descriptionHeight() {
/* 128 */     return 62;
/*     */   }
/*     */   
/*     */   private int listHeight() {
/* 132 */     return this.layout.getContentHeight() - descriptionHeight() - 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 137 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public class ReasonSelectionList extends ObjectSelectionList<ReasonSelectionList.Entry> {
/*     */     public ReasonSelectionList(Minecraft minecraft) {
/* 142 */       super(minecraft, ReportReasonSelectionScreen.this.width, ReportReasonSelectionScreen.this.listHeight(), ReportReasonSelectionScreen.this.layout.getHeaderHeight(), 18);
/* 143 */       for (ReportReason reason : ReportReason.values()) {
/* 144 */         if (!ReportReason.getIncompatibleCategories(ReportReasonSelectionScreen.this.reportType).contains(reason)) {
/* 145 */           addEntry((AbstractSelectionList.Entry)new Entry(reason));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public Entry findEntry(ReportReason reason) {
/* 151 */       return children().stream()
/* 152 */         .filter(entry -> (entry.reason == reason))
/* 153 */         .findFirst()
/* 154 */         .orElse(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 159 */       return 320;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 164 */       super.setSelected((AbstractSelectionList.Entry)selected);
/* 165 */       ReportReasonSelectionScreen.this.currentlySelectedReason = (selected != null) ? selected.getReason() : null;
/*     */     }
/*     */     
/*     */     public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final ReportReason reason;
/*     */       
/*     */       public Entry(ReportReason reason) {
/* 172 */         this.reason = reason;
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 177 */         int textX = getContentX() + 1;
/* 178 */         Objects.requireNonNull(ReportReasonSelectionScreen.this.font); int textY = getContentY() + (getContentHeight() - 9) / 2 + 1;
/* 179 */         graphics.drawString(ReportReasonSelectionScreen.this.font, this.reason.title(), textX, textY, -1);
/*     */       }
/*     */ 
/*     */       
/*     */       public Component getNarration() {
/* 184 */         return (Component)Component.translatable("gui.abuseReport.reason.narration", new Object[] { this.reason.title(), this.reason.description() });
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 189 */         ReportReasonSelectionScreen.ReasonSelectionList.this.setSelected(this);
/* 190 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       public ReportReason getReason() {
/* 194 */         return this.reason; } } } public class Entry extends ObjectSelectionList.Entry<ReasonSelectionList.Entry> { public ReportReason getReason() { return this.reason; }
/*     */ 
/*     */     
/*     */     private final ReportReason reason;
/*     */     
/*     */     public Entry(ReportReason reason) {
/*     */       this.reason = reason;
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       int textX = getContentX() + 1;
/*     */       Objects.requireNonNull(ReportReasonSelectionScreen.this.font);
/*     */       int textY = getContentY() + (getContentHeight() - 9) / 2 + 1;
/*     */       graphics.drawString(ReportReasonSelectionScreen.this.font, this.reason.title(), textX, textY, -1);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/*     */       return (Component)Component.translatable("gui.abuseReport.reason.narration", new Object[] { this.reason.title(), this.reason.description() });
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*     */       ReportReasonSelectionScreen.ReasonSelectionList.this.setSelected(this);
/*     */       return super.mouseClicked(event, doubleClick);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/ReportReasonSelectionScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */