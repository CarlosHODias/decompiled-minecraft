/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.resources.sounds.Sound;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WeighedSoundEvents
/*    */   implements Weighted<Sound>
/*    */ {
/* 16 */   private final List<Weighted<Sound>> list = Lists.newArrayList();
/*    */   private final Component subtitle;
/*    */   
/*    */   public WeighedSoundEvents(Identifier location, String subtitle) {
/* 20 */     if (SharedConstants.DEBUG_SUBTITLES) {
/* 21 */       MutableComponent components = Component.literal(location.getPath());
/* 22 */       if ("FOR THE DEBUG!".equals(subtitle)) {
/* 23 */         components = components.append((Component)Component.literal(" missing").withStyle(ChatFormatting.RED));
/*    */       }
/* 25 */       this.subtitle = (Component)components;
/*    */     } else {
/* 27 */       this.subtitle = (subtitle == null) ? null : (Component)Component.translatable(subtitle);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWeight() {
/* 33 */     int sum = 0;
/* 34 */     for (Weighted<Sound> sound : this.list) {
/* 35 */       sum += sound.getWeight();
/*    */     }
/* 37 */     return sum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Sound getSound(RandomSource random) {
/* 42 */     int weight = getWeight();
/*    */     
/* 44 */     if (this.list.isEmpty() || weight == 0) {
/* 45 */       return SoundManager.EMPTY_SOUND;
/*    */     }
/*    */     
/* 48 */     int index = random.nextInt(weight);
/* 49 */     for (Weighted<Sound> weighted : this.list) {
/* 50 */       index -= weighted.getWeight();
/*    */       
/* 52 */       if (index < 0) {
/* 53 */         return weighted.getSound(random);
/*    */       }
/*    */     } 
/*    */     
/* 57 */     return SoundManager.EMPTY_SOUND;
/*    */   }
/*    */   
/*    */   public void addSound(Weighted<Sound> sound) {
/* 61 */     this.list.add(sound);
/*    */   }
/*    */   
/*    */   public Component getSubtitle() {
/* 65 */     return this.subtitle;
/*    */   }
/*    */ 
/*    */   
/*    */   public void preloadIfRequired(SoundEngine soundEngine) {
/* 70 */     for (Weighted<Sound> weighted : this.list)
/* 71 */       weighted.preloadIfRequired(soundEngine); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/WeighedSoundEvents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */