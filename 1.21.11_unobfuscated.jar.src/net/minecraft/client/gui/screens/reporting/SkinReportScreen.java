/*    */ package net.minecraft.client.gui.screens.reporting;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*    */ import net.minecraft.client.gui.components.PlayerSkinWidget;
/*    */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportReason;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportType;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*    */ import net.minecraft.client.multiplayer.chat.report.SkinReport;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ public class SkinReportScreen extends AbstractReportScreen<SkinReport.Builder> {
/*    */   private static final int SKIN_WIDTH = 85;
/*    */   private static final int FORM_WIDTH = 178;
/* 25 */   private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.skin.title");
/*    */   
/*    */   private MultiLineEditBox commentBox;
/*    */   private Button selectReasonButton;
/*    */   
/*    */   private SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, SkinReport.Builder reportBuilder) {
/* 31 */     super(TITLE, lastScreen, reportingContext, reportBuilder);
/*    */   }
/*    */   
/*    */   public SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, UUID playerId, Supplier<PlayerSkin> skinGetter) {
/* 35 */     this(lastScreen, reportingContext, new SkinReport.Builder(playerId, skinGetter, reportingContext.sender().reportLimits()));
/*    */   }
/*    */   
/*    */   public SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, SkinReport draft) {
/* 39 */     this(lastScreen, reportingContext, new SkinReport.Builder(draft, reportingContext.sender().reportLimits()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addContent() {
/* 44 */     LinearLayout contentLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 45 */     contentLayout.defaultCellSetting().alignVerticallyMiddle();
/*    */     
/* 47 */     contentLayout.addChild((LayoutElement)new PlayerSkinWidget(85, 120, this.minecraft.getEntityModels(), ((SkinReport)this.reportBuilder.report()).getSkinGetter()));
/*    */     
/* 49 */     LinearLayout formLayout = (LinearLayout)contentLayout.addChild((LayoutElement)LinearLayout.vertical().spacing(8));
/*    */     
/* 51 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       .selectReasonButton = Button.builder(SELECT_REASON, b -> this.minecraft.setScreen(new ReportReasonSelectionScreen(this, this.reportBuilder.reason(), ReportType.SKIN, ()))).width(178).build();
/* 58 */     formLayout.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.selectReasonButton, OBSERVED_WHAT_LABEL));
/*    */     
/* 60 */     Objects.requireNonNull(this.font); this.commentBox = createCommentBox(178, 9 * 8, comments -> {
/*    */           this.reportBuilder.setComments(comments);
/*    */           onReportChanged();
/*    */         });
/* 64 */     formLayout.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.commentBox, MORE_COMMENTS_LABEL, s -> s.paddingBottom(12)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onReportChanged() {
/* 69 */     ReportReason reportReason = this.reportBuilder.reason();
/* 70 */     if (reportReason != null) {
/* 71 */       this.selectReasonButton.setMessage(reportReason.title());
/*    */     } else {
/* 73 */       this.selectReasonButton.setMessage(SELECT_REASON);
/*    */     } 
/* 75 */     super.onReportChanged();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mouseReleased(MouseButtonEvent event) {
/* 84 */     if (super.mouseReleased(event)) {
/* 85 */       return true;
/*    */     }
/* 87 */     return this.commentBox.mouseReleased(event);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/SkinReportScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */