/*    */ package net.minecraft.client.gui.screens.reporting;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.multiplayer.chat.ChatLog;
/*    */ import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
/*    */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*    */ import net.minecraft.client.multiplayer.chat.report.ChatReportContextBuilder;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.network.chat.SignedMessageLink;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChatSelectionLogFiller
/*    */ {
/*    */   private final ChatLog log;
/*    */   private final ChatReportContextBuilder contextBuilder;
/*    */   private final Predicate<LoggedChatMessage.Player> canReport;
/* 21 */   private SignedMessageLink previousLink = null;
/*    */   
/*    */   private int eventId;
/*    */   private int missedCount;
/*    */   private PlayerChatMessage lastMessage;
/*    */   
/*    */   public ChatSelectionLogFiller(ReportingContext reportingContext, Predicate<LoggedChatMessage.Player> canReport) {
/* 28 */     this.log = reportingContext.chatLog();
/* 29 */     this.contextBuilder = new ChatReportContextBuilder(reportingContext.sender().reportLimits().leadingContextMessageCount());
/* 30 */     this.canReport = canReport;
/* 31 */     this.eventId = this.log.end();
/*    */   }
/*    */   
/*    */   public void fillNextPage(int pageSize, Output output) {
/* 35 */     int count = 0;
/* 36 */     while (count < pageSize) {
/* 37 */       LoggedChatEvent event = this.log.lookup(this.eventId);
/* 38 */       if (event == null) {
/*    */         break;
/*    */       }
/* 41 */       int eventId = this.eventId;
/* 42 */       this.eventId--;
/*    */       
/* 44 */       if (event instanceof LoggedChatMessage.Player) { LoggedChatMessage.Player message = (LoggedChatMessage.Player)event;
/* 45 */         if (message.message().equals(this.lastMessage)) {
/*    */           continue;
/*    */         }
/*    */         
/* 49 */         if (acceptMessage(output, message)) {
/* 50 */           if (this.missedCount > 0) {
/* 51 */             output.acceptDivider((Component)Component.translatable("gui.chatSelection.fold", new Object[] { this.missedCount }));
/* 52 */             this.missedCount = 0;
/*    */           } 
/* 54 */           output.acceptMessage(eventId, message);
/* 55 */           count++;
/*    */         } else {
/* 57 */           this.missedCount++;
/*    */         } 
/* 59 */         this.lastMessage = message.message(); }
/*    */     
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean acceptMessage(Output output, LoggedChatMessage.Player event) {
/* 65 */     PlayerChatMessage message = event.message();
/* 66 */     boolean context = this.contextBuilder.acceptContext(message);
/* 67 */     if (this.canReport.test(event)) {
/* 68 */       this.contextBuilder.trackContext(message);
/* 69 */       if (this.previousLink != null && !this.previousLink.isDescendantOf(message.link())) {
/* 70 */         output.acceptDivider((Component)Component.translatable("gui.chatSelection.join", new Object[] { event.profile().name() }).withStyle(ChatFormatting.YELLOW));
/*    */       }
/* 72 */       this.previousLink = message.link();
/* 73 */       return true;
/*    */     } 
/* 75 */     return context;
/*    */   }
/*    */   
/*    */   public static interface Output {
/*    */     void acceptMessage(int param1Int, LoggedChatMessage.Player param1Player);
/*    */     
/*    */     void acceptDivider(Component param1Component);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/ChatSelectionLogFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */