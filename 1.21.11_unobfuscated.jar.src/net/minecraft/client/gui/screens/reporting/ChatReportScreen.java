/*    */ package net.minecraft.client.gui.screens.reporting;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*    */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.multiplayer.chat.report.ChatReport;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportReason;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportType;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ChatReportScreen extends AbstractReportScreen<ChatReport.Builder> {
/* 19 */   private static final Component TITLE = (Component)Component.translatable("gui.chatReport.title");
/* 20 */   private static final Component SELECT_CHAT_MESSAGE = (Component)Component.translatable("gui.chatReport.select_chat");
/*    */   
/*    */   private MultiLineEditBox commentBox;
/*    */   private Button selectMessagesButton;
/*    */   private Button selectReasonButton;
/*    */   
/*    */   private ChatReportScreen(Screen lastScreen, ReportingContext reportingContext, ChatReport.Builder reportBuilder) {
/* 27 */     super(TITLE, lastScreen, reportingContext, reportBuilder);
/*    */   }
/*    */   
/*    */   public ChatReportScreen(Screen lastScreen, ReportingContext reportingContext, UUID playerId) {
/* 31 */     this(lastScreen, reportingContext, new ChatReport.Builder(playerId, reportingContext.sender().reportLimits()));
/*    */   }
/*    */   
/*    */   public ChatReportScreen(Screen lastScreen, ReportingContext reportingContext, ChatReport draft) {
/* 35 */     this(lastScreen, reportingContext, new ChatReport.Builder(draft, reportingContext.sender().reportLimits()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addContent() {
/* 40 */     this.selectMessagesButton = (Button)this.layout.addChild((LayoutElement)Button.builder(SELECT_CHAT_MESSAGE, b -> this.minecraft.setScreen(new ChatSelectionScreen(this, this.reportingContext, this.reportBuilder, ())))
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 45 */         .width(280).build());
/*    */     
/* 47 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 53 */       .selectReasonButton = Button.builder(SELECT_REASON, b -> this.minecraft.setScreen(new ReportReasonSelectionScreen(this, this.reportBuilder.reason(), ReportType.CHAT, ()))).width(280).build();
/* 54 */     this.layout.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.selectReasonButton, OBSERVED_WHAT_LABEL));
/*    */     
/* 56 */     Objects.requireNonNull(this.font); this.commentBox = createCommentBox(280, 9 * 8, comments -> {
/*    */           this.reportBuilder.setComments(comments);
/*    */           onReportChanged();
/*    */         });
/* 60 */     this.layout.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.commentBox, MORE_COMMENTS_LABEL, s -> s.paddingBottom(12)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onReportChanged() {
/* 65 */     IntSet reportedMessages = this.reportBuilder.reportedMessages();
/* 66 */     if (reportedMessages.isEmpty()) {
/* 67 */       this.selectMessagesButton.setMessage(SELECT_CHAT_MESSAGE);
/*    */     } else {
/* 69 */       this.selectMessagesButton.setMessage((Component)Component.translatable("gui.chatReport.selected_chat", new Object[] { reportedMessages.size() }));
/*    */     } 
/*    */     
/* 72 */     ReportReason reportReason = this.reportBuilder.reason();
/* 73 */     if (reportReason != null) {
/* 74 */       this.selectReasonButton.setMessage(reportReason.title());
/*    */     } else {
/* 76 */       this.selectReasonButton.setMessage(SELECT_REASON);
/*    */     } 
/* 78 */     super.onReportChanged();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mouseReleased(MouseButtonEvent event) {
/* 87 */     if (super.mouseReleased(event)) {
/* 88 */       return true;
/*    */     }
/* 90 */     return this.commentBox.mouseReleased(event);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/ChatReportScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */