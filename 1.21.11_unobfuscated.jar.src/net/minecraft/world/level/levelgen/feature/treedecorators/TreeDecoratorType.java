/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class TreeDecoratorType<P extends TreeDecorator> {
/*  8 */   public static final TreeDecoratorType<TrunkVineDecorator> TRUNK_VINE = register("trunk_vine", TrunkVineDecorator.CODEC);
/*  9 */   public static final TreeDecoratorType<LeaveVineDecorator> LEAVE_VINE = register("leave_vine", LeaveVineDecorator.CODEC);
/* 10 */   public static final TreeDecoratorType<PaleMossDecorator> PALE_MOSS = register("pale_moss", PaleMossDecorator.CODEC);
/* 11 */   public static final TreeDecoratorType<CreakingHeartDecorator> CREAKING_HEART = register("creaking_heart", CreakingHeartDecorator.CODEC);
/* 12 */   public static final TreeDecoratorType<CocoaDecorator> COCOA = register("cocoa", CocoaDecorator.CODEC);
/* 13 */   public static final TreeDecoratorType<BeehiveDecorator> BEEHIVE = register("beehive", BeehiveDecorator.CODEC);
/* 14 */   public static final TreeDecoratorType<AlterGroundDecorator> ALTER_GROUND = register("alter_ground", AlterGroundDecorator.CODEC);
/* 15 */   public static final TreeDecoratorType<AttachedToLeavesDecorator> ATTACHED_TO_LEAVES = register("attached_to_leaves", AttachedToLeavesDecorator.CODEC);
/* 16 */   public static final TreeDecoratorType<PlaceOnGroundDecorator> PLACE_ON_GROUND = register("place_on_ground", PlaceOnGroundDecorator.CODEC);
/* 17 */   public static final TreeDecoratorType<AttachedToLogsDecorator> ATTACHED_TO_LOGS = register("attached_to_logs", AttachedToLogsDecorator.CODEC);
/*    */   
/*    */   private static <P extends TreeDecorator> TreeDecoratorType<P> register(String name, MapCodec<P> codec) {
/* 20 */     return (TreeDecoratorType<P>)Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, name, new TreeDecoratorType<>(codec));
/*    */   }
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private TreeDecoratorType(MapCodec<P> codec) {
/* 26 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 30 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/TreeDecoratorType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */