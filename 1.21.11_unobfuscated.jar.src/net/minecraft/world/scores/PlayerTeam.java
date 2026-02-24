/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class PlayerTeam extends Team {
/*     */   private static final int BIT_FRIENDLY_FIRE = 0;
/*     */   private static final int BIT_SEE_INVISIBLES = 1;
/*     */   private final Scoreboard scoreboard;
/*     */   private final String name;
/*  27 */   private final Set<String> players = Sets.newHashSet();
/*     */   private Component displayName;
/*  29 */   private Component playerPrefix = CommonComponents.EMPTY;
/*  30 */   private Component playerSuffix = CommonComponents.EMPTY;
/*     */   private boolean allowFriendlyFire = true;
/*     */   private boolean seeFriendlyInvisibles = true;
/*  33 */   private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
/*  34 */   private Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;
/*  35 */   private ChatFormatting color = ChatFormatting.RESET;
/*  36 */   private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;
/*     */   private final Style displayNameStyle;
/*     */   
/*     */   public PlayerTeam(Scoreboard scoreboard, String name) {
/*  40 */     this.scoreboard = scoreboard;
/*  41 */     this.name = name;
/*  42 */     this.displayName = (Component)Component.literal(name);
/*     */     
/*  44 */     this
/*     */       
/*  46 */       .displayNameStyle = Style.EMPTY.withInsertion(name).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal(name)));
/*     */   }
/*     */   
/*     */   public Packed pack() {
/*  50 */     return new Packed(this.name, 
/*     */         
/*  52 */         Optional.of(this.displayName), 
/*  53 */         (this.color != ChatFormatting.RESET) ? Optional.<ChatFormatting>of(this.color) : Optional.<ChatFormatting>empty(), this.allowFriendlyFire, this.seeFriendlyInvisibles, this.playerPrefix, this.playerSuffix, this.nameTagVisibility, this.deathMessageVisibility, this.collisionRule, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         List.copyOf(this.players));
/*     */   }
/*     */ 
/*     */   
/*     */   public Scoreboard getScoreboard() {
/*  66 */     return this.scoreboard;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  71 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  75 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public MutableComponent getFormattedDisplayName() {
/*  79 */     MutableComponent result = ComponentUtils.wrapInSquareBrackets((Component)this.displayName.copy().withStyle(this.displayNameStyle));
/*     */     
/*  81 */     ChatFormatting color = getColor();
/*  82 */     if (color != ChatFormatting.RESET) {
/*  83 */       result.withStyle(color);
/*     */     }
/*     */     
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   public void setDisplayName(Component displayName) {
/*  90 */     if (displayName == null) {
/*  91 */       throw new IllegalArgumentException("Name cannot be null");
/*     */     }
/*  93 */     this.displayName = displayName;
/*  94 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setPlayerPrefix(Component playerPrefix) {
/*  98 */     this.playerPrefix = (playerPrefix == null) ? CommonComponents.EMPTY : playerPrefix;
/*  99 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerPrefix() {
/* 103 */     return this.playerPrefix;
/*     */   }
/*     */   
/*     */   public void setPlayerSuffix(Component playerSuffix) {
/* 107 */     this.playerSuffix = (playerSuffix == null) ? CommonComponents.EMPTY : playerSuffix;
/* 108 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerSuffix() {
/* 112 */     return this.playerSuffix;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getPlayers() {
/* 117 */     return this.players;
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent getFormattedName(Component teamMemberName) {
/* 122 */     MutableComponent result = Component.empty().append(this.playerPrefix).append(teamMemberName).append(this.playerSuffix);
/*     */     
/* 124 */     ChatFormatting color = getColor();
/* 125 */     if (color != ChatFormatting.RESET) {
/* 126 */       result.withStyle(color);
/*     */     }
/*     */     
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   public static MutableComponent formatNameForTeam(Team team, Component name) {
/* 133 */     if (team == null) {
/* 134 */       return name.copy();
/*     */     }
/* 136 */     return team.getFormattedName(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowFriendlyFire() {
/* 141 */     return this.allowFriendlyFire;
/*     */   }
/*     */   
/*     */   public void setAllowFriendlyFire(boolean allowFriendlyFire) {
/* 145 */     this.allowFriendlyFire = allowFriendlyFire;
/* 146 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSeeFriendlyInvisibles() {
/* 151 */     return this.seeFriendlyInvisibles;
/*     */   }
/*     */   
/*     */   public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
/* 155 */     this.seeFriendlyInvisibles = seeFriendlyInvisibles;
/* 156 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.Visibility getNameTagVisibility() {
/* 161 */     return this.nameTagVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.Visibility getDeathMessageVisibility() {
/* 166 */     return this.deathMessageVisibility;
/*     */   }
/*     */   
/*     */   public void setNameTagVisibility(Team.Visibility visibility) {
/* 170 */     this.nameTagVisibility = visibility;
/* 171 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setDeathMessageVisibility(Team.Visibility visibility) {
/* 175 */     this.deathMessageVisibility = visibility;
/* 176 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.CollisionRule getCollisionRule() {
/* 181 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public void setCollisionRule(Team.CollisionRule collisionRule) {
/* 185 */     this.collisionRule = collisionRule;
/* 186 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public int packOptions() {
/* 190 */     int result = 0;
/*     */     
/* 192 */     if (isAllowFriendlyFire()) {
/* 193 */       result |= 0x1;
/*     */     }
/* 195 */     if (canSeeFriendlyInvisibles()) {
/* 196 */       result |= 0x2;
/*     */     }
/*     */     
/* 199 */     return result;
/*     */   }
/*     */   
/*     */   public void unpackOptions(int options) {
/* 203 */     setAllowFriendlyFire(((options & 0x1) > 0));
/* 204 */     setSeeFriendlyInvisibles(((options & 0x2) > 0));
/*     */   }
/*     */   
/*     */   public void setColor(ChatFormatting color) {
/* 208 */     this.color = color;
/* 209 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatFormatting getColor() {
/* 214 */     return this.color;
/*     */   }
/*     */   public static final class Packed extends Record { private final String name; private final Optional<Component> displayName; private final Optional<ChatFormatting> color; private final boolean allowFriendlyFire; private final boolean seeFriendlyInvisibles; private final Component memberNamePrefix; private final Component memberNameSuffix; private final Team.Visibility nameTagVisibility; private final Team.Visibility deathMessageVisibility; private final Team.CollisionRule collisionRule; private final List<String> players; public static final Codec<Packed> CODEC;
/* 217 */     public Packed(String name, Optional<Component> displayName, Optional<ChatFormatting> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Team.Visibility nameTagVisibility, Team.Visibility deathMessageVisibility, Team.CollisionRule collisionRule, List<String> players) { this.name = name; this.displayName = displayName; this.color = color; this.allowFriendlyFire = allowFriendlyFire; this.seeFriendlyInvisibles = seeFriendlyInvisibles; this.memberNamePrefix = memberNamePrefix; this.memberNameSuffix = memberNameSuffix; this.nameTagVisibility = nameTagVisibility; this.deathMessageVisibility = deathMessageVisibility; this.collisionRule = collisionRule; this.players = players; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 217 */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerTeam$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/* 217 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Component> displayName() { return this.displayName; } public Optional<ChatFormatting> color() { return this.color; } public boolean allowFriendlyFire() { return this.allowFriendlyFire; } public boolean seeFriendlyInvisibles() { return this.seeFriendlyInvisibles; } public Component memberNamePrefix() { return this.memberNamePrefix; } public Component memberNameSuffix() { return this.memberNameSuffix; } public Team.Visibility nameTagVisibility() { return this.nameTagVisibility; } public Team.Visibility deathMessageVisibility() { return this.deathMessageVisibility; } public Team.CollisionRule collisionRule() { return this.collisionRule; } public List<String> players() { return this.players; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 230 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("Name").forGetter(Packed::name), (App)ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(Packed::displayName), (App)ChatFormatting.COLOR_CODEC.optionalFieldOf("TeamColor").forGetter(Packed::color), (App)Codec.BOOL.optionalFieldOf("AllowFriendlyFire", true).forGetter(Packed::allowFriendlyFire), (App)Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", true).forGetter(Packed::seeFriendlyInvisibles), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix), (App)Team.Visibility.CODEC.optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility), (App)Team.Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility), (App)Team.CollisionRule.CODEC.optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule), (App)Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players)).apply((Applicative)i, Packed::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/PlayerTeam.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */