/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleCategory;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class EditGameRulesScreen extends Screen {
/*  42 */   private static final Component TITLE = (Component)Component.translatable("editGamerule.title");
/*     */   
/*     */   private static final int SPACING = 8;
/*     */   
/*  46 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   private final Consumer<Optional<GameRules>> exitCallback;
/*  48 */   private final Set<RuleEntry> invalidEntries = Sets.newHashSet();
/*     */   
/*     */   private final GameRules gameRules;
/*     */   
/*     */   private RuleList ruleList;
/*     */   private Button doneButton;
/*     */   
/*     */   public EditGameRulesScreen(GameRules gameRules, Consumer<Optional<GameRules>> exitCallback) {
/*  56 */     super(TITLE);
/*  57 */     this.gameRules = gameRules;
/*  58 */     this.exitCallback = exitCallback;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  63 */     this.layout.addTitleHeader(TITLE, this.font);
/*     */     
/*  65 */     this.ruleList = (RuleList)this.layout.addToContents((LayoutElement)new RuleList(this.gameRules));
/*     */     
/*  67 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  68 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> this.exitCallback.accept(Optional.of(this.gameRules))).build());
/*  69 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
/*  70 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  71 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  76 */     this.layout.arrangeElements();
/*  77 */     if (this.ruleList != null) {
/*  78 */       this.ruleList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  84 */     this.exitCallback.accept(Optional.empty());
/*     */   }
/*     */   
/*     */   private void updateDoneButton() {
/*  88 */     if (this.doneButton != null) {
/*  89 */       this.doneButton.active = this.invalidEntries.isEmpty();
/*     */     }
/*     */   }
/*     */   
/*     */   private void markInvalid(RuleEntry invalidEntry) {
/*  94 */     this.invalidEntries.add(invalidEntry);
/*  95 */     updateDoneButton();
/*     */   }
/*     */   
/*     */   private void clearInvalid(RuleEntry invalidEntry) {
/*  99 */     this.invalidEntries.remove(invalidEntry);
/* 100 */     updateDoneButton();
/*     */   }
/*     */   
/*     */   public static abstract class RuleEntry extends ContainerObjectSelectionList.Entry<RuleEntry> {
/*     */     private final List<FormattedCharSequence> tooltip;
/*     */     
/*     */     public RuleEntry(List<FormattedCharSequence> tooltip) {
/* 107 */       this.tooltip = tooltip;
/*     */     }
/*     */   }
/*     */   
/*     */   public class CategoryRuleEntry extends RuleEntry {
/*     */     private final Component label;
/*     */     
/*     */     public CategoryRuleEntry(Component label) {
/* 115 */       super(null);
/* 116 */       this.label = label;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 121 */       graphics.drawCenteredString(EditGameRulesScreen.this.minecraft.font, this.label, getContentXMiddle(), getContentY() + 5, -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 126 */       return (List<? extends GuiEventListener>)ImmutableList.of();
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables()
/*     */     {
/* 131 */       return (List<? extends NarratableEntry>)ImmutableList.of(new NarratableEntry()
/*     */           {
/*     */             public NarratableEntry.NarrationPriority narrationPriority() {
/* 134 */               return NarratableEntry.NarrationPriority.HOVERED;
/*     */             }
/*     */             
/*     */             public void updateNarration(NarrationElementOutput output)
/*     */             {
/* 139 */               output.add(NarratedElementType.TITLE, EditGameRulesScreen.CategoryRuleEntry.this.label); } }); } } class null implements NarratableEntry { public void updateNarration(NarrationElementOutput output) { output.add(NarratedElementType.TITLE, EditGameRulesScreen.CategoryRuleEntry.this.label); }
/*     */ 
/*     */ 
/*     */     
/*     */     public NarratableEntry.NarrationPriority narrationPriority() {
/*     */       return NarratableEntry.NarrationPriority.HOVERED;
/*     */     } }
/*     */ 
/*     */   
/*     */   public abstract class GameRuleEntry
/*     */     extends RuleEntry
/*     */   {
/*     */     private final List<FormattedCharSequence> label;
/* 152 */     protected final List<AbstractWidget> children = com.google.common.collect.Lists.newArrayList();
/*     */     
/*     */     public GameRuleEntry(List<FormattedCharSequence> tooltip, Component label) {
/* 155 */       super(tooltip);
/* 156 */       this.label = EditGameRulesScreen.this.minecraft.font.split((FormattedText)label, 175);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 161 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 166 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     protected void renderLabel(GuiGraphics graphics, int rowTop, int rowLeft) {
/* 170 */       if (this.label.size() == 1) {
/* 171 */         graphics.drawString(EditGameRulesScreen.this.minecraft.font, this.label.get(0), rowLeft, rowTop + 5, -1);
/* 172 */       } else if (this.label.size() >= 2) {
/* 173 */         graphics.drawString(EditGameRulesScreen.this.minecraft.font, this.label.get(0), rowLeft, rowTop, -1);
/* 174 */         graphics.drawString(EditGameRulesScreen.this.minecraft.font, this.label.get(1), rowLeft, rowTop + 10, -1);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class BooleanRuleEntry extends GameRuleEntry {
/*     */     private final CycleButton<Boolean> checkbox;
/*     */     
/*     */     public BooleanRuleEntry(Component name, List<FormattedCharSequence> tooltip, String narration, GameRule<Boolean> gameRule) {
/* 183 */       super(tooltip, name);
/* 184 */       this
/*     */ 
/*     */         
/* 187 */         .checkbox = CycleButton.onOffBuilder((Boolean)EditGameRulesScreen.this.gameRules.get(gameRule)).displayOnlyValue().withCustomNarration(button -> button.createDefaultNarrationMessage().append("\n").append(narration)).create(10, 5, 44, 20, name, (button, newValue) -> EditGameRulesScreen.this.gameRules.set(gameRule, newValue, null));
/* 188 */       this.children.add(this.checkbox);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 193 */       renderLabel(graphics, getContentY(), getContentX());
/* 194 */       this.checkbox.setX(getContentRight() - 45);
/* 195 */       this.checkbox.setY(getContentY());
/* 196 */       this.checkbox.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public class IntegerRuleEntry extends GameRuleEntry {
/*     */     private final EditBox input;
/*     */     
/*     */     public IntegerRuleEntry(Component label, List<FormattedCharSequence> tooltip, String narration, GameRule<Integer> gameRule) {
/* 204 */       super(tooltip, label);
/*     */       
/* 206 */       this.input = new EditBox(EditGameRulesScreen.this.minecraft.font, 10, 5, 44, 20, (Component)label.copy().append("\n").append(narration).append("\n"));
/* 207 */       this.input.setValue(EditGameRulesScreen.this.gameRules.getAsString(gameRule));
/* 208 */       this.input.setResponder(v -> {
/*     */             DataResult<Integer> value = gameRule.deserialize(gameRule);
/*     */             if (value.isSuccess()) {
/*     */               this.input.setTextColor(-2039584);
/*     */               EditGameRulesScreen.this.clearInvalid(this);
/*     */               EditGameRulesScreen.this.gameRules.set(gameRule, value.getOrThrow(), null);
/*     */             } else {
/*     */               this.input.setTextColor(-65536);
/*     */               EditGameRulesScreen.this.markInvalid(this);
/*     */             } 
/*     */           });
/* 219 */       this.children.add(this.input);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 224 */       renderLabel(graphics, getContentY(), getContentX());
/* 225 */       this.input.setX(getContentRight() - 45);
/* 226 */       this.input.setY(getContentY());
/* 227 */       this.input.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public class RuleList extends ContainerObjectSelectionList<RuleEntry> {
/*     */     private static final int ITEM_HEIGHT = 24;
/*     */     
/*     */     public RuleList(GameRules gameRules) {
/* 235 */       super(Minecraft.getInstance(), EditGameRulesScreen.this.width, EditGameRulesScreen.this.layout.getContentHeight(), EditGameRulesScreen.this.layout.getHeaderHeight(), 24);
/*     */       
/* 237 */       final Map<GameRuleCategory, Map<GameRule<?>, EditGameRulesScreen.RuleEntry>> entries = Maps.newHashMap();
/*     */       
/* 239 */       gameRules.visitGameRuleTypes(new GameRuleTypeVisitor()
/*     */           {
/*     */             public void visitBoolean(GameRule<Boolean> gameRule) {
/* 242 */               addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new EditGameRulesScreen.BooleanRuleEntry(x$0, x$1, x$2, x$3));
/*     */             }
/*     */ 
/*     */             
/*     */             public void visitInteger(GameRule<Integer> gameRule) {
/* 247 */               addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new EditGameRulesScreen.IntegerRuleEntry(x$0, x$1, x$2, x$3));
/*     */             } private <T> void addEntry(GameRule<T> gameRule, EditGameRulesScreen.EntryFactory<T> factory) {
/*     */               ImmutableList immutableList;
/*     */               String narration;
/* 251 */               MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/* 252 */               MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/*     */               
/* 254 */               MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/* 255 */               String descriptionKey = gameRule.getDescriptionId() + ".description";
/*     */ 
/*     */ 
/*     */               
/* 259 */               if (I18n.exists(descriptionKey)) {
/* 260 */                 ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/* 261 */                 MutableComponent mutableComponent = Component.translatable(descriptionKey);
/* 262 */                 java.util.Objects.requireNonNull(result); EditGameRulesScreen.this.font.split((FormattedText)mutableComponent, 150).forEach(result::add);
/* 263 */                 immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/* 264 */                 narration = mutableComponent.getString() + "\n" + mutableComponent.getString();
/*     */               } else {
/* 266 */                 immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/* 267 */                 narration = mutableComponent3.getString();
/*     */               } 
/*     */               
/* 270 */               ((Map<GameRule<T>, EditGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */             }
/*     */           });
/*     */       
/* 274 */       entries.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRuleCategory::getDescriptionId))).forEach(e -> {
/*     */             addEntry((AbstractSelectionList.Entry)new EditGameRulesScreen.CategoryRuleEntry((Component)((GameRuleCategory)e.getKey()).label().withStyle(new ChatFormatting[] { ChatFormatting.BOLD, ChatFormatting.YELLOW })));
/*     */             ((Map)e.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRule::getDescriptionId))).forEach(());
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 282 */       super.renderWidget(graphics, mouseX, mouseY, a);
/* 283 */       EditGameRulesScreen.RuleEntry hovered = (EditGameRulesScreen.RuleEntry)getHovered();
/* 284 */       if (hovered != null && hovered.tooltip != null)
/* 285 */         graphics.setTooltipForNextFrame(hovered.tooltip, mouseX, mouseY); 
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements GameRuleTypeVisitor {
/*     */     public void visitBoolean(GameRule<Boolean> gameRule) {
/*     */       addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new EditGameRulesScreen.BooleanRuleEntry(x$0, x$1, x$2, x$3));
/*     */     }
/*     */     
/*     */     public void visitInteger(GameRule<Integer> gameRule) {
/*     */       addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new EditGameRulesScreen.IntegerRuleEntry(x$0, x$1, x$2, x$3));
/*     */     }
/*     */     
/*     */     private <T> void addEntry(GameRule<T> gameRule, EditGameRulesScreen.EntryFactory<T> factory) {
/*     */       ImmutableList immutableList;
/*     */       String narration;
/*     */       MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/*     */       MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/*     */       MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/*     */       String descriptionKey = gameRule.getDescriptionId() + ".description";
/*     */       if (I18n.exists(descriptionKey)) {
/*     */         ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/*     */         MutableComponent mutableComponent = Component.translatable(descriptionKey);
/*     */         java.util.Objects.requireNonNull(result);
/*     */         EditGameRulesScreen.this.font.split((FormattedText)mutableComponent, 150).forEach(result::add);
/*     */         immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/*     */         narration = mutableComponent.getString() + "\n" + mutableComponent.getString();
/*     */       } else {
/*     */         immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/*     */         narration = mutableComponent3.getString();
/*     */       } 
/*     */       ((Map<GameRule<T>, EditGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EntryFactory<T> {
/*     */     EditGameRulesScreen.RuleEntry create(Component param1Component, List<FormattedCharSequence> param1List, String param1String, GameRule<T> param1GameRule);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/EditGameRulesScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */