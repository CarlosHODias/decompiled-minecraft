/*    */ package net.minecraft.client;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum AttackIndicatorStatus {
/*    */   private static final IntFunction<AttackIndicatorStatus> BY_ID;
/* 10 */   OFF(0, "options.off"),
/* 11 */   CROSSHAIR(1, "options.attack.crosshair"),
/* 12 */   HOTBAR(2, "options.attack.hotbar"); public static final Codec<AttackIndicatorStatus> LEGACY_CODEC;
/*    */   
/*    */   static {
/* 15 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 16 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, s -> s.id);
/*    */   }
/*    */   private final int id;
/*    */   private final Component caption;
/*    */   
/*    */   AttackIndicatorStatus(int id, String key) {
/* 22 */     this.id = id;
/* 23 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 27 */     return this.caption;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/AttackIndicatorStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */