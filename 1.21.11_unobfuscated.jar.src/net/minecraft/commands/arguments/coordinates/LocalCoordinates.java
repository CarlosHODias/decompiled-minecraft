/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ public final class LocalCoordinates extends Record implements Coordinates {
/*    */   private final double left;
/*    */   private final double up;
/*    */   private final double forwards;
/*    */   public static final char PREFIX_LOCAL_COORDINATE = '^';
/*    */   
/*  9 */   public LocalCoordinates(double left, double up, double forwards) { this.left = left; this.up = up; this.forwards = forwards; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates; } public double left() { return this.left; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/coordinates/LocalCoordinates;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public double up() { return this.up; } public double forwards() { return this.forwards; }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.world.phys.Vec3 getPosition(net.minecraft.commands.CommandSourceStack sender) {
/* 14 */     net.minecraft.world.phys.Vec3 source = sender.getAnchor().apply(sender);
/* 15 */     return net.minecraft.world.phys.Vec3.applyLocalCoordinatesToRotation(sender.getRotation(), new net.minecraft.world.phys.Vec3(this.left, this.up, this.forwards)).add(source.x, source.y, source.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.world.phys.Vec2 getRotation(net.minecraft.commands.CommandSourceStack sender) {
/* 20 */     return net.minecraft.world.phys.Vec2.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isXRelative() {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isYRelative() {
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isZRelative() {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public static LocalCoordinates parse(com.mojang.brigadier.StringReader reader) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
/* 39 */     int start = reader.getCursor();
/* 40 */     double left = readDouble(reader, start);
/* 41 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 42 */       reader.setCursor(start);
/* 43 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 45 */     reader.skip();
/* 46 */     double up = readDouble(reader, start);
/* 47 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 48 */       reader.setCursor(start);
/* 49 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 51 */     reader.skip();
/* 52 */     double forwards = readDouble(reader, start);
/* 53 */     return new LocalCoordinates(left, up, forwards);
/*    */   }
/*    */   
/*    */   private static double readDouble(com.mojang.brigadier.StringReader reader, int start) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
/* 57 */     if (!reader.canRead()) {
/* 58 */       throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(reader);
/*    */     }
/*    */     
/* 61 */     if (reader.peek() != '^') {
/* 62 */       reader.setCursor(start);
/* 63 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(reader);
/*    */     } 
/* 65 */     reader.skip();
/*    */     
/* 67 */     return (reader.canRead() && reader.peek() != ' ') ? reader.readDouble() : 0.0D;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/coordinates/LocalCoordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */