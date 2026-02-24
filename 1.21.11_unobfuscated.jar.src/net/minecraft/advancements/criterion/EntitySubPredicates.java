/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class EntitySubPredicates {
/*  8 */   public static final MapCodec<LightningBoltPredicate> LIGHTNING = register("lightning", LightningBoltPredicate.CODEC);
/*  9 */   public static final MapCodec<FishingHookPredicate> FISHING_HOOK = register("fishing_hook", FishingHookPredicate.CODEC);
/* 10 */   public static final MapCodec<PlayerPredicate> PLAYER = register("player", PlayerPredicate.CODEC);
/* 11 */   public static final MapCodec<SlimePredicate> SLIME = register("slime", SlimePredicate.CODEC);
/* 12 */   public static final MapCodec<RaiderPredicate> RAIDER = register("raider", RaiderPredicate.CODEC);
/* 13 */   public static final MapCodec<SheepPredicate> SHEEP = register("sheep", SheepPredicate.CODEC);
/*    */   
/*    */   private static <T extends EntitySubPredicate> MapCodec<T> register(String id, MapCodec<T> value) {
/* 16 */     return (MapCodec<T>)Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, id, value);
/*    */   }
/*    */   
/*    */   public static MapCodec<? extends EntitySubPredicate> bootstrap(Registry<MapCodec<? extends EntitySubPredicate>> registry) {
/* 20 */     return (MapCodec)LIGHTNING;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/EntitySubPredicates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */