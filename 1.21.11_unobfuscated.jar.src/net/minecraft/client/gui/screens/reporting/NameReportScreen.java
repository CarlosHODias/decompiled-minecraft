/*    */ package net.minecraft.client.gui.screens.reporting;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.multiplayer.chat.report.NameReport;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public class NameReportScreen extends AbstractReportScreen<NameReport.Builder> {
/* 18 */   private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.name.title");
/* 19 */   private static final Component COMMENT_BOX_LABEL = (Component)Component.translatable("gui.abuseReport.name.comment_box_label");
/*    */   
/*    */   private MultiLineEditBox commentBox;
/*    */   
/*    */   private NameReportScreen(Screen lastScreen, ReportingContext reportingContext, NameReport.Builder reportBuilder) {
/* 24 */     super(TITLE, lastScreen, reportingContext, reportBuilder);
/*    */   }
/*    */   
/*    */   public NameReportScreen(Screen lastScreen, ReportingContext reportingContext, UUID playerId, String reportedName) {
/* 28 */     this(lastScreen, reportingContext, new NameReport.Builder(playerId, reportedName, reportingContext.sender().reportLimits()));
/*    */   }
/*    */   
/*    */   public NameReportScreen(Screen lastScreen, ReportingContext reportingContext, NameReport draft) {
/* 32 */     this(lastScreen, reportingContext, new NameReport.Builder(draft, reportingContext.sender().reportLimits()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addContent() {
/* 37 */     MutableComponent mutableComponent = Component.literal(((NameReport)this.reportBuilder.report()).getReportedName()).withStyle(ChatFormatting.YELLOW);
/* 38 */     this.layout.addChild((LayoutElement)new StringWidget((Component)Component.translatable("gui.abuseReport.name.reporting", new Object[] { mutableComponent }), this.font), s -> s.alignHorizontallyCenter().padding(0, 8));
/*    */     
/* 40 */     Objects.requireNonNull(this.font); this.commentBox = createCommentBox(280, 9 * 8, comments -> {
/*    */           this.reportBuilder.setComments(comments);
/*    */           onReportChanged();
/*    */         });
/* 44 */     this.layout.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.commentBox, COMMENT_BOX_LABEL, s -> s.paddingBottom(12)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mouseReleased(MouseButtonEvent event) {
/* 53 */     if (super.mouseReleased(event)) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (this.commentBox != null) {
/* 57 */       return this.commentBox.mouseReleased(event);
/*    */     }
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/NameReportScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */