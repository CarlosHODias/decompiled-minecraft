/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.GenericWaitingScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
/*     */ import net.minecraft.client.multiplayer.chat.report.Report;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ThrowingComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractReportScreen<B extends Report.Builder<?>> extends Screen {
/*  32 */   private static final Component REPORT_SENT_MESSAGE = (Component)Component.translatable("gui.abuseReport.report_sent_msg");
/*  33 */   private static final Component REPORT_SENDING_TITLE = (Component)Component.translatable("gui.abuseReport.sending.title").withStyle(ChatFormatting.BOLD);
/*  34 */   private static final Component REPORT_SENT_TITLE = (Component)Component.translatable("gui.abuseReport.sent.title").withStyle(ChatFormatting.BOLD);
/*  35 */   private static final Component REPORT_ERROR_TITLE = (Component)Component.translatable("gui.abuseReport.error.title").withStyle(ChatFormatting.BOLD);
/*  36 */   private static final Component REPORT_SEND_GENERIC_ERROR = (Component)Component.translatable("gui.abuseReport.send.generic_error");
/*     */   
/*  38 */   protected static final Component SEND_REPORT = (Component)Component.translatable("gui.abuseReport.send");
/*  39 */   protected static final Component OBSERVED_WHAT_LABEL = (Component)Component.translatable("gui.abuseReport.observed_what");
/*  40 */   protected static final Component SELECT_REASON = (Component)Component.translatable("gui.abuseReport.select_reason");
/*  41 */   private static final Component DESCRIBE_PLACEHOLDER = (Component)Component.translatable("gui.abuseReport.describe");
/*  42 */   protected static final Component MORE_COMMENTS_LABEL = (Component)Component.translatable("gui.abuseReport.more_comments");
/*  43 */   private static final Component MORE_COMMENTS_NARRATION = (Component)Component.translatable("gui.abuseReport.comments");
/*  44 */   private static final Component ATTESTATION_CHECKBOX = (Component)Component.translatable("gui.abuseReport.attestation").withColor(-2039584);
/*     */   
/*     */   protected static final int BUTTON_WIDTH = 120;
/*     */   
/*     */   protected static final int MARGIN = 20;
/*     */   protected static final int SCREEN_WIDTH = 280;
/*     */   protected static final int SPACING = 8;
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   protected final ReportingContext reportingContext;
/*  56 */   protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
/*     */   
/*     */   protected B reportBuilder;
/*     */   
/*     */   private Checkbox attestation;
/*     */   protected Button sendButton;
/*     */   
/*     */   protected AbstractReportScreen(Component title, Screen lastScreen, ReportingContext reportingContext, B reportBuilder) {
/*  64 */     super(title);
/*  65 */     this.lastScreen = lastScreen;
/*  66 */     this.reportingContext = reportingContext;
/*  67 */     this.reportBuilder = reportBuilder;
/*     */   }
/*     */   
/*     */   protected MultiLineEditBox createCommentBox(int width, int height, Consumer<String> valueListener) {
/*  71 */     AbuseReportLimits reportLimits = this.reportingContext.sender().reportLimits();
/*     */     
/*  73 */     MultiLineEditBox commentBox = MultiLineEditBox.builder().setPlaceholder(DESCRIBE_PLACEHOLDER).build(this.font, width, height, MORE_COMMENTS_NARRATION);
/*  74 */     commentBox.setValue(this.reportBuilder.comments());
/*  75 */     commentBox.setCharacterLimit(reportLimits.maxOpinionCommentsLength());
/*  76 */     commentBox.setValueListener(valueListener);
/*     */     
/*  78 */     return commentBox;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  83 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*     */     
/*  85 */     createHeader();
/*  86 */     addContent();
/*  87 */     createFooter();
/*     */     
/*  89 */     onReportChanged();
/*     */     
/*  91 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  92 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void createHeader() {
/*  96 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createFooter() {
/* 102 */     this.attestation = (Checkbox)this.layout.addChild((LayoutElement)Checkbox.builder(ATTESTATION_CHECKBOX, this.font)
/* 103 */         .selected(this.reportBuilder.attested())
/* 104 */         .maxWidth(280)
/* 105 */         .onValueChange((checkbox, value) -> {
/*     */             this.reportBuilder.setAttested(value);
/*     */             
/*     */             onReportChanged();
/* 109 */           }).build());
/*     */     
/* 111 */     LinearLayout buttonsLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 112 */     buttonsLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, b -> onClose()).width(120).build());
/* 113 */     this.sendButton = (Button)buttonsLayout.addChild((LayoutElement)Button.builder(SEND_REPORT, b -> sendReport()).width(120).build());
/*     */   }
/*     */   
/*     */   protected void onReportChanged() {
/* 117 */     Report.CannotBuildReason cannotBuildReason = this.reportBuilder.checkBuildable();
/* 118 */     this.sendButton.active = (cannotBuildReason == null && this.attestation.selected());
/* 119 */     this.sendButton.setTooltip((Tooltip)Optionull.map(cannotBuildReason, Report.CannotBuildReason::tooltip));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 124 */     this.layout.arrangeElements();
/* 125 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   protected void sendReport() {
/* 129 */     this.reportBuilder.build(this.reportingContext).ifLeft(result -> {
/*     */           CompletableFuture<?> sendFuture = this.reportingContext.sender().send(result.id(), result.reportType(), result.report());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           this.minecraft.setScreen((Screen)GenericWaitingScreen.createWaiting(REPORT_SENDING_TITLE, CommonComponents.GUI_CANCEL, ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           sendFuture.handleAsync((), (Executor)this.minecraft);
/* 147 */         }).ifRight(reason -> displayReportSendError(reason.message()));
/*     */   }
/*     */   
/*     */   private void onReportSendSuccess() {
/* 151 */     clearDraft();
/* 152 */     this.minecraft.setScreen((Screen)GenericWaitingScreen.createCompleted(REPORT_SENT_TITLE, REPORT_SENT_MESSAGE, CommonComponents.GUI_DONE, () -> this.minecraft.setScreen(null)));
/*     */   }
/*     */   private void onReportSendError(Throwable throwable) {
/*     */     Component message;
/* 156 */     LOGGER.error("Encountered error while sending abuse report", throwable);
/*     */ 
/*     */     
/* 159 */     Throwable throwable1 = throwable.getCause(); if (throwable1 instanceof ThrowingComponent) { ThrowingComponent error = (ThrowingComponent)throwable1;
/* 160 */       message = error.getComponent(); }
/*     */     else
/* 162 */     { message = REPORT_SEND_GENERIC_ERROR; }
/*     */ 
/*     */     
/* 165 */     displayReportSendError(message);
/*     */   }
/*     */   
/*     */   private void displayReportSendError(Component message) {
/* 169 */     MutableComponent mutableComponent = message.copy().withStyle(ChatFormatting.RED);
/*     */     
/* 171 */     this.minecraft.setScreen((Screen)GenericWaitingScreen.createCompleted(REPORT_ERROR_TITLE, (Component)mutableComponent, CommonComponents.GUI_BACK, () -> this.minecraft.setScreen(this)));
/*     */   }
/*     */   
/*     */   private void saveDraft() {
/* 175 */     if (this.reportBuilder.hasContent()) {
/* 176 */       this.reportingContext.setReportDraft(this.reportBuilder.report().copy());
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearDraft() {
/* 181 */     this.reportingContext.setReportDraft(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 186 */     if (this.reportBuilder.hasContent()) {
/* 187 */       this.minecraft.setScreen((Screen)new DiscardReportWarningScreen());
/*     */     } else {
/* 189 */       this.minecraft.setScreen(this.lastScreen);
/*     */     } 
/*     */   }
/*     */   protected abstract void addContent();
/*     */   
/*     */   public void removed() {
/* 195 */     saveDraft();
/* 196 */     super.removed();
/*     */   }
/*     */   
/*     */   private class DiscardReportWarningScreen extends WarningScreen {
/* 200 */     private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.discard.title").withStyle(ChatFormatting.BOLD);
/* 201 */     private static final Component MESSAGE = (Component)Component.translatable("gui.abuseReport.discard.content");
/* 202 */     private static final Component RETURN = (Component)Component.translatable("gui.abuseReport.discard.return");
/* 203 */     private static final Component DRAFT = (Component)Component.translatable("gui.abuseReport.discard.draft");
/* 204 */     private static final Component DISCARD = (Component)Component.translatable("gui.abuseReport.discard.discard");
/*     */     
/*     */     protected DiscardReportWarningScreen() {
/* 207 */       super(TITLE, MESSAGE, MESSAGE);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Layout addFooterButtons() {
/* 212 */       LinearLayout footer = LinearLayout.vertical().spacing(8);
/* 213 */       footer.defaultCellSetting().alignHorizontallyCenter();
/* 214 */       LinearLayout firstFooterRow = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 215 */       firstFooterRow.addChild(
/* 216 */           (LayoutElement)Button.builder(RETURN, button -> onClose())
/* 217 */           .build());
/*     */       
/* 219 */       firstFooterRow.addChild(
/* 220 */           (LayoutElement)Button.builder(DRAFT, button -> {
/*     */               AbstractReportScreen.this.saveDraft();
/*     */               
/*     */               this.minecraft.setScreen(AbstractReportScreen.this.lastScreen);
/* 224 */             }).build());
/*     */       
/* 226 */       footer.addChild(
/* 227 */           (LayoutElement)Button.builder(DISCARD, button -> {
/*     */               AbstractReportScreen.this.clearDraft();
/*     */               
/*     */               this.minecraft.setScreen(AbstractReportScreen.this.lastScreen);
/* 231 */             }).build());
/*     */       
/* 233 */       return (Layout)footer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onClose() {
/* 238 */       this.minecraft.setScreen(AbstractReportScreen.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldCloseOnEsc() {
/* 243 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/AbstractReportScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */