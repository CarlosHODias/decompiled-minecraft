/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PlayerModelType
/*    */   implements StringRepresentable
/*    */ {
/* 14 */   SLIM("slim", "slim"),
/* 15 */   WIDE("wide", "default"); private static final Function<String, PlayerModelType> NAME_LOOKUP;
/*    */   public static final StreamCodec<ByteBuf, PlayerModelType> STREAM_CODEC;
/* 17 */   public static final Codec<PlayerModelType> CODEC = (Codec<PlayerModelType>)StringRepresentable.fromEnum(PlayerModelType::values); static {
/* 18 */     NAME_LOOKUP = StringRepresentable.createNameLookup((Object[])values(), e -> e.legacyServicesId);
/* 19 */     STREAM_CODEC = ByteBufCodecs.BOOL.map(slim -> slim ? SLIM : WIDE, type -> (type == SLIM));
/*    */   }
/*    */   private final String id;
/*    */   private final String legacyServicesId;
/*    */   
/*    */   PlayerModelType(String id, String legacyServicesId) {
/* 25 */     this.id = id;
/* 26 */     this.legacyServicesId = legacyServicesId;
/*    */   }
/*    */   
/*    */   public static PlayerModelType byLegacyServicesName(String name) {
/* 30 */     return Objects.<PlayerModelType>requireNonNullElse(NAME_LOOKUP.apply(name), WIDE);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 35 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/PlayerModelType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */