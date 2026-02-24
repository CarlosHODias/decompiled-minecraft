/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.advancements.DisplayInfo;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AdvancementToast
/*    */   implements Toast {
/* 22 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
/*    */   public static final int DISPLAY_TIME = 5000;
/*    */   private final AdvancementHolder advancement;
/* 25 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*    */   
/*    */   public AdvancementToast(AdvancementHolder advancement) {
/* 28 */     this.advancement = advancement;
/*    */   }
/*    */ 
/*    */   
/*    */   public Toast.Visibility getWantedVisibility() {
/* 33 */     return this.wantedVisibility;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(ToastManager manager, long fullyVisibleForMs) {
/* 38 */     DisplayInfo display = this.advancement.value().display().orElse(null);
/* 39 */     if (display == null) {
/* 40 */       this.wantedVisibility = Toast.Visibility.HIDE;
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     this.wantedVisibility = (fullyVisibleForMs >= 5000.0D * manager.getNotificationDisplayTimeMultiplier()) ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getSoundEvent() {
/* 49 */     return isChallengeAdvancement() ? SoundEvents.UI_TOAST_CHALLENGE_COMPLETE : null;
/*    */   }
/*    */   
/*    */   private boolean isChallengeAdvancement() {
/* 53 */     Optional<DisplayInfo> displayInfo = this.advancement.value().display();
/* 54 */     return (displayInfo.isPresent() && ((DisplayInfo)displayInfo.get()).getType().equals(AdvancementType.CHALLENGE));
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
/* 59 */     DisplayInfo display = this.advancement.value().display().orElse(null);
/* 60 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());
/*    */     
/* 62 */     if (display == null) {
/*    */       return;
/*    */     }
/*    */     
/* 66 */     List<FormattedCharSequence> lines = font.split((FormattedText)display.getTitle(), 125);
/* 67 */     int titleColor = (display.getType() == AdvancementType.CHALLENGE) ? -30465 : -256;
/*    */     
/* 69 */     if (lines.size() == 1) {
/* 70 */       graphics.drawString(font, display.getType().getDisplayName(), 30, 7, titleColor, false);
/* 71 */       graphics.drawString(font, lines.get(0), 30, 18, -1, false);
/*    */     } else {
/* 73 */       int unlockTextTime = 1500;
/* 74 */       float unlockFadeTime = 300.0F;
/* 75 */       if (fullyVisibleForMs < 1500L) {
/* 76 */         int alpha = Mth.floor(Mth.clamp((float)(1500L - fullyVisibleForMs) / 300.0F, 0.0F, 1.0F) * 255.0F);
/* 77 */         graphics.drawString(font, display.getType().getDisplayName(), 30, 11, ARGB.color(alpha, titleColor), false);
/*    */       } else {
/* 79 */         int alpha = Mth.floor(Mth.clamp((float)(fullyVisibleForMs - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F);
/* 80 */         Objects.requireNonNull(font); int y = height() / 2 - lines.size() * 9 / 2;
/* 81 */         for (FormattedCharSequence line : lines) {
/* 82 */           graphics.drawString(font, line, 30, y, ARGB.white(alpha), false);
/* 83 */           Objects.requireNonNull(font); y += 9;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     graphics.renderFakeItem(display.getIcon(), 8, 8);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/AdvancementToast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */