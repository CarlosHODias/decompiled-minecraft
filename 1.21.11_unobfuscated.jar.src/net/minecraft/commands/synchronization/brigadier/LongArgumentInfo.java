/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.LongArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class LongArgumentInfo
/*    */   implements ArgumentTypeInfo<LongArgumentType, LongArgumentInfo.Template> {
/*    */   public final class Template
/*    */     implements ArgumentTypeInfo.Template<LongArgumentType> {
/*    */     private final long min;
/*    */     private final long max;
/*    */     
/*    */     private Template(long min, long max) {
/* 19 */       this.min = min;
/* 20 */       this.max = max;
/*    */     }
/*    */ 
/*    */     
/*    */     public LongArgumentType instantiate(CommandBuildContext context) {
/* 25 */       return LongArgumentType.longArg(this.min, this.max);
/*    */     }
/*    */ 
/*    */     
/*    */     public ArgumentTypeInfo<LongArgumentType, ?> type() {
/* 30 */       return LongArgumentInfo.this;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 36 */     boolean hasMin = (template.min != Long.MIN_VALUE);
/* 37 */     boolean hasMax = (template.max != Long.MAX_VALUE);
/* 38 */     out.writeByte(ArgumentUtils.createNumberFlags(hasMin, hasMax));
/* 39 */     if (hasMin) {
/* 40 */       out.writeLong(template.min);
/*    */     }
/* 42 */     if (hasMax) {
/* 43 */       out.writeLong(template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 49 */     byte flags = in.readByte();
/* 50 */     long min = ArgumentUtils.numberHasMin(flags) ? in.readLong() : Long.MIN_VALUE;
/* 51 */     long max = ArgumentUtils.numberHasMax(flags) ? in.readLong() : Long.MAX_VALUE;
/* 52 */     return new Template(min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 57 */     if (template.min != Long.MIN_VALUE) {
/* 58 */       out.addProperty("min", template.min);
/*    */     }
/* 60 */     if (template.max != Long.MAX_VALUE) {
/* 61 */       out.addProperty("max", template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template unpack(LongArgumentType argument) {
/* 67 */     return new Template(argument.getMinimum(), argument.getMaximum());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/synchronization/brigadier/LongArgumentInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */