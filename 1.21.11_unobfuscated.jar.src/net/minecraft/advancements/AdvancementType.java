/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum AdvancementType implements StringRepresentable {
/* 11 */   TASK("task", ChatFormatting.GREEN),
/* 12 */   CHALLENGE("challenge", ChatFormatting.DARK_PURPLE),
/* 13 */   GOAL("goal", ChatFormatting.GREEN);
/*    */ 
/*    */   
/* 16 */   public static final Codec<AdvancementType> CODEC = (Codec<AdvancementType>)StringRepresentable.fromEnum(AdvancementType::values);
/*    */   
/*    */   private final String name;
/*    */   private final ChatFormatting chatColor;
/*    */   private final Component displayName;
/*    */   
/*    */   AdvancementType(String name, ChatFormatting chatColor) {
/* 23 */     this.name = name;
/* 24 */     this.chatColor = chatColor;
/* 25 */     this.displayName = (Component)Component.translatable("advancements.toast." + name);
/*    */   }
/*    */   
/*    */   public ChatFormatting getChatColor() {
/* 29 */     return this.chatColor;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 33 */     return this.displayName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public MutableComponent createAnnouncement(AdvancementHolder holder, ServerPlayer player) {
/* 42 */     return Component.translatable("chat.type.advancement." + this.name, new Object[] { player.getDisplayName(), Advancement.name(holder) });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */