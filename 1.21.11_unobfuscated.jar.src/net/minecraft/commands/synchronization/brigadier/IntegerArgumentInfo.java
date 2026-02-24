/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class IntegerArgumentInfo
/*    */   implements ArgumentTypeInfo<IntegerArgumentType, IntegerArgumentInfo.Template> {
/*    */   public final class Template
/*    */     implements ArgumentTypeInfo.Template<IntegerArgumentType> {
/*    */     private final int min;
/*    */     private final int max;
/*    */     
/*    */     private Template(int min, int max) {
/* 19 */       this.min = min;
/* 20 */       this.max = max;
/*    */     }
/*    */ 
/*    */     
/*    */     public IntegerArgumentType instantiate(CommandBuildContext context) {
/* 25 */       return IntegerArgumentType.integer(this.min, this.max);
/*    */     }
/*    */ 
/*    */     
/*    */     public ArgumentTypeInfo<IntegerArgumentType, ?> type() {
/* 30 */       return IntegerArgumentInfo.this;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 36 */     boolean hasMin = (template.min != Integer.MIN_VALUE);
/* 37 */     boolean hasMax = (template.max != Integer.MAX_VALUE);
/* 38 */     out.writeByte(ArgumentUtils.createNumberFlags(hasMin, hasMax));
/* 39 */     if (hasMin) {
/* 40 */       out.writeInt(template.min);
/*    */     }
/* 42 */     if (hasMax) {
/* 43 */       out.writeInt(template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 49 */     byte flags = in.readByte();
/* 50 */     int min = ArgumentUtils.numberHasMin(flags) ? in.readInt() : Integer.MIN_VALUE;
/* 51 */     int max = ArgumentUtils.numberHasMax(flags) ? in.readInt() : Integer.MAX_VALUE;
/* 52 */     return new Template(min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 57 */     if (template.min != Integer.MIN_VALUE) {
/* 58 */       out.addProperty("min", template.min);
/*    */     }
/* 60 */     if (template.max != Integer.MAX_VALUE) {
/* 61 */       out.addProperty("max", template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template unpack(IntegerArgumentType argument) {
/* 67 */     return new Template(argument.getMinimum(), argument.getMaximum());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/synchronization/brigadier/IntegerArgumentInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */