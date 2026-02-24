/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class FloatArgumentInfo
/*    */   implements ArgumentTypeInfo<FloatArgumentType, FloatArgumentInfo.Template> {
/*    */   public final class Template
/*    */     implements ArgumentTypeInfo.Template<FloatArgumentType> {
/*    */     private final float min;
/*    */     private final float max;
/*    */     
/*    */     private Template(float min, float max) {
/* 19 */       this.min = min;
/* 20 */       this.max = max;
/*    */     }
/*    */ 
/*    */     
/*    */     public FloatArgumentType instantiate(CommandBuildContext context) {
/* 25 */       return FloatArgumentType.floatArg(this.min, this.max);
/*    */     }
/*    */ 
/*    */     
/*    */     public ArgumentTypeInfo<FloatArgumentType, ?> type() {
/* 30 */       return FloatArgumentInfo.this;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 36 */     boolean hasMin = (template.min != -3.4028235E38F);
/* 37 */     boolean hasMax = (template.max != Float.MAX_VALUE);
/* 38 */     out.writeByte(ArgumentUtils.createNumberFlags(hasMin, hasMax));
/* 39 */     if (hasMin) {
/* 40 */       out.writeFloat(template.min);
/*    */     }
/* 42 */     if (hasMax) {
/* 43 */       out.writeFloat(template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 49 */     byte flags = in.readByte();
/* 50 */     float min = ArgumentUtils.numberHasMin(flags) ? in.readFloat() : -3.4028235E38F;
/* 51 */     float max = ArgumentUtils.numberHasMax(flags) ? in.readFloat() : Float.MAX_VALUE;
/* 52 */     return new Template(min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 57 */     if (template.min != -3.4028235E38F) {
/* 58 */       out.addProperty("min", template.min);
/*    */     }
/* 60 */     if (template.max != Float.MAX_VALUE) {
/* 61 */       out.addProperty("max", template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template unpack(FloatArgumentType argument) {
/* 67 */     return new Template(argument.getMinimum(), argument.getMaximum());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/synchronization/brigadier/FloatArgumentInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */