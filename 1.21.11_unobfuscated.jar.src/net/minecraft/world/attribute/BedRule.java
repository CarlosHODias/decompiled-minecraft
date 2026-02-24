/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class BedRule extends Record {
/*    */   private final Rule canSleep;
/*    */   private final Rule canSetSpawn;
/*    */   private final boolean explodes;
/*    */   private final Optional<net.minecraft.network.chat.Component> errorMessage;
/*    */   
/* 13 */   public BedRule(Rule canSleep, Rule canSetSpawn, boolean explodes, Optional<net.minecraft.network.chat.Component> errorMessage) { this.canSleep = canSleep; this.canSetSpawn = canSetSpawn; this.explodes = explodes; this.errorMessage = errorMessage; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/BedRule;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule; } public Rule canSleep() { return this.canSleep; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/BedRule;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/BedRule;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/BedRule;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Rule canSetSpawn() { return this.canSetSpawn; } public boolean explodes() { return this.explodes; } public Optional<net.minecraft.network.chat.Component> errorMessage() { return this.errorMessage; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final BedRule CAN_SLEEP_WHEN_DARK = new BedRule(Rule.WHEN_DARK, Rule.ALWAYS, false, 
/*    */ 
/*    */ 
/*    */       
/* 23 */       (Optional)Optional.of(net.minecraft.network.chat.Component.translatable("block.minecraft.bed.no_sleep")));
/*    */   
/* 25 */   public static final BedRule EXPLODES = new BedRule(Rule.NEVER, Rule.NEVER, true, Optional.empty()); public static final com.mojang.serialization.Codec<BedRule> CODEC;
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Rule.CODEC.fieldOf("can_sleep").forGetter(BedRule::canSleep), (App)Rule.CODEC.fieldOf("can_set_spawn").forGetter(BedRule::canSetSpawn), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("explodes", false).forGetter(BedRule::explodes), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("error_message").forGetter(BedRule::errorMessage)).apply((com.mojang.datafixers.kinds.Applicative)i, BedRule::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canSleep(net.minecraft.world.level.Level level) {
/* 35 */     return this.canSleep.test(level);
/*    */   }
/*    */   
/*    */   public boolean canSetSpawn(net.minecraft.world.level.Level level) {
/* 39 */     return this.canSetSpawn.test(level);
/*    */   }
/*    */   
/*    */   public net.minecraft.world.entity.player.Player.BedSleepingProblem asProblem() {
/* 43 */     return new net.minecraft.world.entity.player.Player.BedSleepingProblem(this.errorMessage.orElse(null));
/*    */   }
/*    */   
/*    */   public enum Rule implements net.minecraft.util.StringRepresentable {
/* 47 */     ALWAYS("always"),
/*    */ 
/*    */     
/* 50 */     WHEN_DARK("when_dark"),
/* 51 */     NEVER("never");
/*    */ 
/*    */     
/* 54 */     public static final com.mojang.serialization.Codec<Rule> CODEC = (com.mojang.serialization.Codec<Rule>)net.minecraft.util.StringRepresentable.fromEnum(Rule::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     Rule(String name) {
/* 59 */       this.name = name;
/*    */     }
/*    */     
/*    */     public boolean test(net.minecraft.world.level.Level level) {
/* 63 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return false;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 72 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/BedRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */