/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class Time extends NeedleDirectionHelper implements RangeSelectItemModelProperty {
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("wobble", true).forGetter(NeedleDirectionHelper::wobble), (App)TimeSource.CODEC.fieldOf("source").forGetter(())).apply((Applicative)i, Time::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<Time> MAP_CODEC;
/*    */   private final TimeSource source;
/* 21 */   private final RandomSource randomSource = RandomSource.create();
/*    */   
/*    */   private final NeedleDirectionHelper.Wobbler wobbler;
/*    */   
/*    */   public Time(boolean wooble, TimeSource source) {
/* 26 */     super(wooble);
/* 27 */     this.source = source;
/* 28 */     this.wobbler = newWobbler(0.9F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float calculate(ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner) {
/* 33 */     float targetRotation = this.source.get(level, itemStack, owner, this.randomSource);
/*    */     
/* 35 */     long gameTime = level.getGameTime();
/* 36 */     if (this.wobbler.shouldUpdate(gameTime)) {
/* 37 */       this.wobbler.update(gameTime, targetRotation);
/*    */     }
/* 39 */     return this.wobbler.rotation();
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<Time> type() {
/* 44 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public enum TimeSource implements StringRepresentable {
/* 48 */     RANDOM("random")
/*    */     {
/*    */       public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 51 */         return random.nextFloat();
/*    */       }
/*    */     },
/* 54 */     DAYTIME("daytime")
/*    */     {
/*    */       public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 57 */         return (Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, owner.position()) / 360.0F;
/*    */       }
/*    */     },
/* 60 */     MOON_PHASE("moon_phase")
/*    */     {
/*    */       public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 63 */         return ((net.minecraft.world.level.MoonPhase)level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, owner.position())).index() / net.minecraft.world.level.MoonPhase.COUNT;
/*    */       }
/*    */     };
/*    */ 
/*    */     
/* 68 */     public static final Codec<TimeSource> CODEC = (Codec<TimeSource>)StringRepresentable.fromEnum(TimeSource::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     TimeSource(String name) {
/* 73 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 78 */       return this.name;
/*    */     }
/*    */     
/*    */     abstract float get(ClientLevel param1ClientLevel, ItemStack param1ItemStack, ItemOwner param1ItemOwner, RandomSource param1RandomSource);
/*    */   }
/*    */   
/*    */   enum null {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/*    */       return random.nextFloat();
/*    */     }
/*    */   }
/*    */   
/*    */   enum null {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/*    */       return (Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, owner.position()) / 360.0F;
/*    */     }
/*    */   }
/*    */   
/*    */   enum null {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/*    */       return ((net.minecraft.world.level.MoonPhase)level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, owner.position())).index() / net.minecraft.world.level.MoonPhase.COUNT;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/Time.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */