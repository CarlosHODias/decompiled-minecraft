/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ public abstract class Team
/*     */ {
/*     */   public boolean isAlliedTo(Team other) {
/*  19 */     if (other == null) {
/*  20 */       return false;
/*     */     }
/*  22 */     if (this == other) {
/*  23 */       return true;
/*     */     }
/*  25 */     return false;
/*     */   }
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract MutableComponent getFormattedName(Component paramComponent);
/*     */   
/*     */   public abstract boolean canSeeFriendlyInvisibles();
/*     */   
/*     */   public abstract boolean isAllowFriendlyFire();
/*     */   
/*     */   public abstract Visibility getNameTagVisibility();
/*     */   
/*     */   public abstract ChatFormatting getColor();
/*     */   
/*     */   public abstract Collection<String> getPlayers();
/*     */   
/*     */   public abstract Visibility getDeathMessageVisibility();
/*     */   
/*     */   public abstract CollisionRule getCollisionRule();
/*     */   
/*     */   public enum Visibility implements StringRepresentable {
/*  47 */     ALWAYS("always", 0),
/*  48 */     NEVER("never", 1),
/*  49 */     HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/*  50 */     HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3); private static final IntFunction<Visibility> BY_ID;
/*     */     public static final StreamCodec<ByteBuf, Visibility> STREAM_CODEC;
/*  52 */     public static final Codec<Visibility> CODEC = (Codec<Visibility>)StringRepresentable.fromEnum(Visibility::values);
/*     */     static {
/*  54 */       BY_ID = ByIdMap.continuous(v -> v.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  55 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
/*     */     }
/*     */     public final String name;
/*     */     public final int id;
/*     */     
/*     */     Visibility(String name, int id) {
/*  61 */       this.name = name;
/*  62 */       this.id = id;
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/*  66 */       return (Component)Component.translatable("team.visibility." + this.name);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  71 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CollisionRule implements StringRepresentable {
/*  76 */     ALWAYS("always", 0),
/*  77 */     NEVER("never", 1),
/*  78 */     PUSH_OTHER_TEAMS("pushOtherTeams", 2),
/*  79 */     PUSH_OWN_TEAM("pushOwnTeam", 3);
/*     */     private static final IntFunction<CollisionRule> BY_ID;
/*  81 */     public static final Codec<CollisionRule> CODEC = (Codec<CollisionRule>)StringRepresentable.fromEnum(CollisionRule::values); public static final StreamCodec<ByteBuf, CollisionRule> STREAM_CODEC;
/*     */     static {
/*  83 */       BY_ID = ByIdMap.continuous(r -> r.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  84 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*     */     }
/*     */     public final String name;
/*     */     public final int id;
/*     */     
/*     */     CollisionRule(String name, int id) {
/*  90 */       this.name = name;
/*  91 */       this.id = id;
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/*  95 */       return (Component)Component.translatable("team.collision." + this.name);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 100 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/Team.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */