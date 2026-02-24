/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum HumanoidArm
/*    */   implements StringRepresentable {
/* 14 */   LEFT(0, "left", "options.mainHand.left"),
/* 15 */   RIGHT(1, "right", "options.mainHand.right");
/*    */ 
/*    */   
/* 18 */   public static final Codec<HumanoidArm> CODEC = (Codec<HumanoidArm>)StringRepresentable.fromEnum(HumanoidArm::values); private static final IntFunction<HumanoidArm> BY_ID; public static final StreamCodec<ByteBuf, HumanoidArm> STREAM_CODEC; static {
/* 19 */     BY_ID = ByIdMap.continuous(a -> a.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, a -> a.id);
/*    */   }
/*    */   private final int id;
/*    */   private final String name;
/*    */   private final Component caption;
/*    */   
/*    */   HumanoidArm(int id, String name, String translationKey) {
/* 27 */     this.id = id;
/* 28 */     this.name = name;
/* 29 */     this.caption = (Component)Component.translatable(translationKey);
/*    */   }
/*    */   
/*    */   public HumanoidArm getOpposite() {
/* 33 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*    */       
/* 35 */       LEFT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component caption() {
/* 40 */     return this.caption;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/HumanoidArm.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */