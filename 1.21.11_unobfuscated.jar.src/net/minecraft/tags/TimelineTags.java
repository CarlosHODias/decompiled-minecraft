/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.timeline.Timeline;
/*    */ 
/*    */ public interface TimelineTags {
/*  8 */   public static final TagKey<Timeline> UNIVERSAL = create("universal");
/*  9 */   public static final TagKey<Timeline> IN_OVERWORLD = create("in_overworld");
/* 10 */   public static final TagKey<Timeline> IN_NETHER = create("in_nether");
/* 11 */   public static final TagKey<Timeline> IN_END = create("in_end");
/*    */   
/*    */   private static TagKey<Timeline> create(String name) {
/* 14 */     return TagKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TimelineTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */