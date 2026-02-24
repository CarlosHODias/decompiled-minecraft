/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnvironmentAttribute<Value>
/*     */ {
/*     */   private final AttributeType<Value> type;
/*     */   private final Value defaultValue;
/*     */   private final AttributeRange<Value> valueRange;
/*     */   private final boolean isSyncable;
/*     */   private final boolean isPositional;
/*     */   private final boolean isSpatiallyInterpolated;
/*     */   
/*     */   private EnvironmentAttribute(AttributeType<Value> type, Value defaultValue, AttributeRange<Value> valueRange, boolean isSyncable, boolean isPositional, boolean isSpatiallyInterpolated) {
/*  26 */     this.type = type;
/*  27 */     this.defaultValue = defaultValue;
/*  28 */     this.valueRange = valueRange;
/*  29 */     this.isSyncable = isSyncable;
/*  30 */     this.isPositional = isPositional;
/*  31 */     this.isSpatiallyInterpolated = isSpatiallyInterpolated;
/*     */   }
/*     */   
/*     */   public static <Value> Builder<Value> builder(AttributeType<Value> type) {
/*  35 */     return new Builder<>(type);
/*     */   }
/*     */   
/*     */   public AttributeType<Value> type() {
/*  39 */     return this.type;
/*     */   }
/*     */   
/*     */   public Value defaultValue() {
/*  43 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public Codec<Value> valueCodec() {
/*  47 */     Objects.requireNonNull(this.valueRange); return this.type.valueCodec().validate(this.valueRange::validate);
/*     */   }
/*     */   
/*     */   public Value sanitizeValue(Value value) {
/*  51 */     return this.valueRange.sanitize(value);
/*     */   }
/*     */   
/*     */   public boolean isSyncable() {
/*  55 */     return this.isSyncable;
/*     */   }
/*     */   
/*     */   public boolean isPositional() {
/*  59 */     return this.isPositional;
/*     */   }
/*     */   
/*     */   public boolean isSpatiallyInterpolated() {
/*  63 */     return this.isSpatiallyInterpolated;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     return Util.getRegisteredName(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, this);
/*     */   }
/*     */   
/*     */   public static class Builder<Value> {
/*     */     private final AttributeType<Value> type;
/*     */     private Value defaultValue;
/*  74 */     private AttributeRange<Value> valueRange = AttributeRange.any();
/*     */     private boolean isSyncable = false;
/*     */     private boolean isPositional = true;
/*     */     private boolean isSpatiallyInterpolated = false;
/*     */     
/*     */     public Builder(AttributeType<Value> type) {
/*  80 */       this.type = type;
/*     */     }
/*     */     
/*     */     public Builder<Value> defaultValue(Value defaultValue) {
/*  84 */       this.defaultValue = defaultValue;
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> valueRange(AttributeRange<Value> valueRange) {
/*  89 */       this.valueRange = valueRange;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> syncable() {
/*  94 */       this.isSyncable = true;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> notPositional() {
/*  99 */       this.isPositional = false;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> spatiallyInterpolated() {
/* 104 */       this.isSpatiallyInterpolated = true;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public EnvironmentAttribute<Value> build() {
/* 109 */       return new EnvironmentAttribute<>(this.type, 
/*     */           
/* 111 */           Objects.requireNonNull(this.defaultValue, "Missing default value"), this.valueRange, this.isSyncable, this.isPositional, this.isSpatiallyInterpolated);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/EnvironmentAttribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */