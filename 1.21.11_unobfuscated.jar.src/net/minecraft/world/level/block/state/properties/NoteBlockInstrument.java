/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public enum NoteBlockInstrument implements net.minecraft.util.StringRepresentable {
/*    */   private final String name;
/*    */   private final Holder<SoundEvent> soundEvent;
/*  9 */   HARP("harp", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_HARP, Type.BASE_BLOCK),
/* 10 */   BASEDRUM("basedrum", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_BASEDRUM, Type.BASE_BLOCK),
/* 11 */   SNARE("snare", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_SNARE, Type.BASE_BLOCK),
/* 12 */   HAT("hat", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_HAT, Type.BASE_BLOCK),
/* 13 */   BASS("bass", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_BASS, Type.BASE_BLOCK),
/* 14 */   FLUTE("flute", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_FLUTE, Type.BASE_BLOCK),
/* 15 */   BELL("bell", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_BELL, Type.BASE_BLOCK),
/* 16 */   GUITAR("guitar", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_GUITAR, Type.BASE_BLOCK),
/* 17 */   CHIME("chime", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_CHIME, Type.BASE_BLOCK),
/* 18 */   XYLOPHONE("xylophone", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_XYLOPHONE, Type.BASE_BLOCK),
/* 19 */   IRON_XYLOPHONE("iron_xylophone", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, Type.BASE_BLOCK),
/* 20 */   COW_BELL("cow_bell", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_COW_BELL, Type.BASE_BLOCK),
/* 21 */   DIDGERIDOO("didgeridoo", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_DIDGERIDOO, Type.BASE_BLOCK),
/* 22 */   BIT("bit", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_BIT, Type.BASE_BLOCK),
/* 23 */   BANJO("banjo", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_BANJO, Type.BASE_BLOCK),
/* 24 */   PLING("pling", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_PLING, Type.BASE_BLOCK),
/* 25 */   ZOMBIE("zombie", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_ZOMBIE, Type.MOB_HEAD),
/* 26 */   SKELETON("skeleton", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_SKELETON, Type.MOB_HEAD),
/* 27 */   CREEPER("creeper", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_CREEPER, Type.MOB_HEAD),
/* 28 */   DRAGON("dragon", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_ENDER_DRAGON, Type.MOB_HEAD),
/* 29 */   WITHER_SKELETON("wither_skeleton", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_WITHER_SKELETON, Type.MOB_HEAD),
/* 30 */   PIGLIN("piglin", (Holder<SoundEvent>)SoundEvents.NOTE_BLOCK_IMITATE_PIGLIN, Type.MOB_HEAD),
/* 31 */   CUSTOM_HEAD("custom_head", (Holder<SoundEvent>)SoundEvents.UI_BUTTON_CLICK, Type.CUSTOM);
/*    */   private final Type type;
/*    */   
/*    */   private enum Type {
/* 35 */     BASE_BLOCK,
/* 36 */     MOB_HEAD,
/* 37 */     CUSTOM;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   NoteBlockInstrument(String name, Holder<SoundEvent> soundEvent, Type type) {
/* 45 */     this.name = name;
/* 46 */     this.soundEvent = soundEvent;
/* 47 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> getSoundEvent() {
/* 56 */     return this.soundEvent;
/*    */   }
/*    */   
/*    */   public boolean isTunable() {
/* 60 */     return (this.type == Type.BASE_BLOCK);
/*    */   }
/*    */   
/*    */   public boolean hasCustomSound() {
/* 64 */     return (this.type == Type.CUSTOM);
/*    */   }
/*    */   
/*    */   public boolean worksAboveNoteBlock() {
/* 68 */     return (this.type != Type.BASE_BLOCK);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/NoteBlockInstrument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */