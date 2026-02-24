/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapTypeArgument
/*    */   extends StringRepresentableArgument<Heightmap.Types> {
/*    */   static {
/* 14 */     LOWER_CASE_CODEC = (Codec<Heightmap.Types>)StringRepresentable.fromEnumWithMapping(HeightmapTypeArgument::keptTypes, s -> s.toLowerCase(Locale.ROOT));
/*    */   } private static final Codec<Heightmap.Types> LOWER_CASE_CODEC;
/*    */   private static Heightmap.Types[] keptTypes() {
/* 17 */     return (Heightmap.Types[])Arrays.<Heightmap.Types>stream(Heightmap.Types.values()).filter(Heightmap.Types::keepAfterWorldgen).toArray(x$0 -> new Heightmap.Types[x$0]);
/*    */   }
/*    */   
/*    */   private HeightmapTypeArgument() {
/* 21 */     super(LOWER_CASE_CODEC, HeightmapTypeArgument::keptTypes);
/*    */   }
/*    */   
/*    */   public static HeightmapTypeArgument heightmap() {
/* 25 */     return new HeightmapTypeArgument();
/*    */   }
/*    */   
/*    */   public static Heightmap.Types getHeightmap(CommandContext<CommandSourceStack> context, String name) {
/* 29 */     return (Heightmap.Types)context.getArgument(name, Heightmap.Types.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String convertId(String id) {
/* 34 */     return id.toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/HeightmapTypeArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */