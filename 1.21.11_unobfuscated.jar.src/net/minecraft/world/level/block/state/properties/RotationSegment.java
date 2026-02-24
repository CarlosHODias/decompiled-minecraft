/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.SegmentedAnglePrecision;
/*    */ 
/*    */ public class RotationSegment
/*    */ {
/*  9 */   private static final SegmentedAnglePrecision SEGMENTED_ANGLE16 = new SegmentedAnglePrecision(4);
/*    */   
/* 11 */   private static final int MAX_SEGMENT_INDEX = SEGMENTED_ANGLE16.getMask();
/*    */   
/*    */   private static final int NORTH_0 = 0;
/*    */   private static final int EAST_90 = 4;
/*    */   private static final int SOUTH_180 = 8;
/*    */   private static final int WEST_270 = 12;
/*    */   
/*    */   public static int getMaxSegmentIndex() {
/* 19 */     return MAX_SEGMENT_INDEX;
/*    */   }
/*    */   
/*    */   public static int convertToSegment(Direction direction) {
/* 23 */     return SEGMENTED_ANGLE16.fromDirection(direction);
/*    */   }
/*    */   
/*    */   public static int convertToSegment(float rotDegrees) {
/* 27 */     return SEGMENTED_ANGLE16.fromDegrees(rotDegrees);
/*    */   }
/*    */   
/*    */   public static Optional<Direction> convertToDirection(int segment) {
/* 31 */     switch (segment) { case 0: 
/*    */       case 4: 
/*    */       case 8: 
/*    */       case 12: 
/*    */       default:
/* 36 */         break; }  Direction result = null;
/*    */ 
/*    */     
/* 39 */     return Optional.ofNullable(result);
/*    */   }
/*    */   
/*    */   public static float convertToDegrees(int segment) {
/* 43 */     return SEGMENTED_ANGLE16.toDegrees(segment);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/RotationSegment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */