/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TimelineTags;
/*    */ import net.minecraft.world.timeline.Timeline;
/*    */ import net.minecraft.world.timeline.Timelines;
/*    */ 
/*    */ public class TimelineTagsProvider extends KeyTagProvider<Timeline> {
/*    */   public TimelineTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.TIMELINE, lookupProvider);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(TimelineTags.UNIVERSAL).add(Timelines.VILLAGER_SCHEDULE);
/*    */     
/* 21 */     tag(TimelineTags.IN_OVERWORLD).addTag(TimelineTags.UNIVERSAL).add((Object[])new ResourceKey[] { Timelines.DAY, Timelines.MOON, Timelines.EARLY_GAME });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     tag(TimelineTags.IN_NETHER).addTag(TimelineTags.UNIVERSAL);
/* 28 */     tag(TimelineTags.IN_END).addTag(TimelineTags.UNIVERSAL);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/TimelineTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */