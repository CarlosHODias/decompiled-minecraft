/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ScoreContents extends Record implements net.minecraft.network.chat.ComponentContents {
/*    */   private final Either<SelectorPattern, String> name;
/*    */   private final String objective;
/*    */   public static final MapCodec<ScoreContents> INNER_CODEC;
/*    */   
/* 26 */   public ScoreContents(Either<SelectorPattern, String> name, String objective) { this.name = name; this.objective = objective; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ScoreContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 26 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ScoreContents; } public Either<SelectorPattern, String> name() { return this.name; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ScoreContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ScoreContents;
/* 26 */     //   0	8	1	o	Ljava/lang/Object; } public String objective() { return this.objective; } static {
/* 27 */     INNER_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.either(SelectorPattern.CODEC, (Codec)Codec.STRING).fieldOf("name").forGetter(ScoreContents::name), (App)Codec.STRING.fieldOf("objective").forGetter(ScoreContents::objective)).apply((com.mojang.datafixers.kinds.Applicative)i, ScoreContents::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final MapCodec<ScoreContents> MAP_CODEC = INNER_CODEC.fieldOf("score");
/*    */ 
/*    */   
/*    */   public MapCodec<ScoreContents> codec() {
/* 36 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   private ScoreHolder findTargetName(CommandSourceStack source) throws CommandSyntaxException {
/* 40 */     Optional<SelectorPattern> selector = this.name.left();
/* 41 */     if (selector.isPresent()) {
/* 42 */       List<? extends Entity> entities = ((SelectorPattern)selector.get()).resolved().findEntities(source);
/* 43 */       if (!entities.isEmpty()) {
/* 44 */         if (entities.size() != 1) {
/* 45 */           throw net.minecraft.commands.arguments.EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*    */         }
/* 47 */         return (ScoreHolder)entities.getFirst();
/*    */       } 
/* 49 */       return ScoreHolder.forNameOnly(((SelectorPattern)selector.get()).pattern());
/*    */     } 
/* 51 */     return ScoreHolder.forNameOnly(this.name.right().orElseThrow());
/*    */   }
/*    */   
/*    */   private MutableComponent getScore(ScoreHolder name, CommandSourceStack source) {
/* 55 */     MinecraftServer server = source.getServer();
/* 56 */     if (server != null) {
/* 57 */       ServerScoreboard serverScoreboard = server.getScoreboard();
/* 58 */       Objective objective = serverScoreboard.getObjective(this.objective);
/*    */       
/* 60 */       if (objective != null) {
/* 61 */         ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(name, objective);
/* 62 */         if (scoreInfo != null) {
/* 63 */           return scoreInfo.formatValue(objective.numberFormatOrDefault((net.minecraft.network.chat.numbers.NumberFormat)net.minecraft.network.chat.numbers.StyledFormat.NO_STYLE));
/*    */         }
/*    */       } 
/*    */     } 
/* 67 */     return net.minecraft.network.chat.Component.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 72 */     if (source == null) {
/* 73 */       return net.minecraft.network.chat.Component.empty();
/*    */     }
/*    */     
/* 76 */     ScoreHolder scoreHolder = findTargetName(source);
/* 77 */     ScoreHolder scoreName = (entity != null && scoreHolder.equals(ScoreHolder.WILDCARD)) ? (ScoreHolder)entity : scoreHolder;
/* 78 */     return getScore(scoreName, source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "score{name='" + String.valueOf(this.name) + "', objective='" + this.objective + "'}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/ScoreContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */