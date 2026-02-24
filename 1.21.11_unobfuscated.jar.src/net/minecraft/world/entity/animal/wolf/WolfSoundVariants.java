/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WolfSoundVariants {
/*    */   public enum SoundSet {
/* 14 */     CLASSIC("classic", ""),
/* 15 */     PUGLIN("puglin", "_puglin"),
/* 16 */     SAD("sad", "_sad"),
/* 17 */     ANGRY("angry", "_angry"),
/* 18 */     GRUMPY("grumpy", "_grumpy"),
/* 19 */     BIG("big", "_big"),
/* 20 */     CUTE("cute", "_cute");
/*    */     
/*    */     private final String identifier;
/*    */     private final String soundEventSuffix;
/*    */     
/*    */     SoundSet(String identifier, String suffix) {
/* 26 */       this.identifier = identifier;
/* 27 */       this.soundEventSuffix = suffix;
/*    */     }
/*    */     
/*    */     public String getIdentifier() {
/* 31 */       return this.identifier;
/*    */     }
/*    */     
/*    */     public String getSoundEventSuffix() {
/* 35 */       return this.soundEventSuffix;
/*    */     }
/*    */   }
/*    */   
/* 39 */   public static final ResourceKey<WolfSoundVariant> CLASSIC = createKey(SoundSet.CLASSIC);
/* 40 */   public static final ResourceKey<WolfSoundVariant> PUGLIN = createKey(SoundSet.PUGLIN);
/* 41 */   public static final ResourceKey<WolfSoundVariant> SAD = createKey(SoundSet.SAD);
/* 42 */   public static final ResourceKey<WolfSoundVariant> ANGRY = createKey(SoundSet.ANGRY);
/* 43 */   public static final ResourceKey<WolfSoundVariant> GRUMPY = createKey(SoundSet.GRUMPY);
/* 44 */   public static final ResourceKey<WolfSoundVariant> BIG = createKey(SoundSet.BIG);
/* 45 */   public static final ResourceKey<WolfSoundVariant> CUTE = createKey(SoundSet.CUTE);
/*    */   
/*    */   private static ResourceKey<WolfSoundVariant> createKey(SoundSet wolfSoundVariant) {
/* 48 */     return ResourceKey.create(Registries.WOLF_SOUND_VARIANT, Identifier.withDefaultNamespace(wolfSoundVariant.getIdentifier()));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<WolfSoundVariant> context) {
/* 52 */     register(context, CLASSIC, SoundSet.CLASSIC);
/* 53 */     register(context, PUGLIN, SoundSet.PUGLIN);
/* 54 */     register(context, SAD, SoundSet.SAD);
/* 55 */     register(context, ANGRY, SoundSet.ANGRY);
/* 56 */     register(context, GRUMPY, SoundSet.GRUMPY);
/* 57 */     register(context, BIG, SoundSet.BIG);
/* 58 */     register(context, CUTE, SoundSet.CUTE);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfSoundVariant> context, ResourceKey<WolfSoundVariant> key, SoundSet wolfSoundVariant) {
/* 62 */     context.register(key, SoundEvents.WOLF_SOUNDS.get(wolfSoundVariant));
/*    */   }
/*    */   
/*    */   public static Holder<WolfSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
/* 66 */     return registryAccess.lookupOrThrow(Registries.WOLF_SOUND_VARIANT).getRandom(random).orElseThrow();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/wolf/WolfSoundVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */