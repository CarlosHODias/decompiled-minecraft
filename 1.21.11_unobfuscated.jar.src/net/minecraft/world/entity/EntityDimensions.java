/*    */ package net.minecraft.world.entity;public final class EntityDimensions extends Record { private final float width; private final float height;
/*    */   private final float eyeHeight;
/*    */   private final EntityAttachments attachments;
/*    */   private final boolean fixed;
/*    */   
/*  6 */   public EntityDimensions(float width, float height, float eyeHeight, EntityAttachments attachments, boolean fixed) { this.width = width; this.height = height; this.eyeHeight = eyeHeight; this.attachments = attachments; this.fixed = fixed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EntityDimensions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/world/entity/EntityDimensions; } public float width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EntityDimensions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EntityDimensions; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EntityDimensions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/EntityDimensions;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public float height() { return this.height; } public float eyeHeight() { return this.eyeHeight; } public EntityAttachments attachments() { return this.attachments; } public boolean fixed() { return this.fixed; }
/*    */    private EntityDimensions(float width, float height, boolean fixed) {
/*  8 */     this(width, height, defaultEyeHeight(height), EntityAttachments.createDefault(width, height), fixed);
/*    */   }
/*    */   
/*    */   private static float defaultEyeHeight(float height) {
/* 12 */     return height * 0.85F;
/*    */   }
/*    */   
/*    */   public net.minecraft.world.phys.AABB makeBoundingBox(net.minecraft.world.phys.Vec3 pos) {
/* 16 */     return makeBoundingBox(pos.x, pos.y, pos.z);
/*    */   }
/*    */   
/*    */   public net.minecraft.world.phys.AABB makeBoundingBox(double x, double y, double z) {
/* 20 */     float w = this.width / 2.0F;
/* 21 */     float h = this.height;
/* 22 */     return new net.minecraft.world.phys.AABB(x - w, y, z - w, x + w, y + h, z + w);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float scaleFactor) {
/* 26 */     return scale(scaleFactor, scaleFactor);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float widthScaleFactor, float heightScaleFactor) {
/* 30 */     if (this.fixed || (widthScaleFactor == 1.0F && heightScaleFactor == 1.0F)) {
/* 31 */       return this;
/*    */     }
/* 33 */     return new EntityDimensions(this.width * widthScaleFactor, this.height * heightScaleFactor, this.eyeHeight * heightScaleFactor, 
/*    */ 
/*    */ 
/*    */         
/* 37 */         this.attachments.scale(widthScaleFactor, heightScaleFactor, widthScaleFactor), false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static EntityDimensions scalable(float width, float height) {
/* 44 */     return new EntityDimensions(width, height, false);
/*    */   }
/*    */   
/*    */   public static EntityDimensions fixed(float width, float height) {
/* 48 */     return new EntityDimensions(width, height, true);
/*    */   }
/*    */   
/*    */   public EntityDimensions withEyeHeight(float eyeHeight) {
/* 52 */     return new EntityDimensions(this.width, this.height, eyeHeight, this.attachments, this.fixed);
/*    */   }
/*    */   
/*    */   public EntityDimensions withAttachments(EntityAttachments.Builder attachments) {
/* 56 */     return new EntityDimensions(this.width, this.height, this.eyeHeight, attachments.build(this.width, this.height), this.fixed);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntityDimensions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */