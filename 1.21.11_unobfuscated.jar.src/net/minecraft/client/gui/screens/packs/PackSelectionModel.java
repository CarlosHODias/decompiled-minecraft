/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackCompatibility;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ 
/*     */ 
/*     */ public class PackSelectionModel
/*     */ {
/*     */   private final PackRepository repository;
/*     */   private final List<Pack> selected;
/*     */   private final List<Pack> unselected;
/*     */   private final Function<Pack, Identifier> iconGetter;
/*     */   private final Consumer<EntryBase> onListChanged;
/*     */   private final Consumer<PackRepository> output;
/*     */   
/*     */   public PackSelectionModel(Consumer<EntryBase> onListChanged, Function<Pack, Identifier> iconGetter, PackRepository repository, Consumer<PackRepository> output) {
/*  31 */     this.onListChanged = onListChanged;
/*  32 */     this.iconGetter = iconGetter;
/*  33 */     this.repository = repository;
/*  34 */     this.selected = Lists.newArrayList(repository.getSelectedPacks());
/*     */     
/*  36 */     Collections.reverse(this.selected);
/*  37 */     this.unselected = Lists.newArrayList(repository.getAvailablePacks());
/*  38 */     this.unselected.removeAll(this.selected);
/*  39 */     this.output = output;
/*     */   }
/*     */   
/*     */   public Stream<Entry> getUnselected() {
/*  43 */     return this.unselected.stream().map(x$0 -> new UnselectedPackEntry(x$0));
/*     */   }
/*     */   
/*     */   public Stream<Entry> getSelected() {
/*  47 */     return this.selected.stream().map(x$0 -> new SelectedPackEntry(x$0));
/*     */   }
/*     */   
/*     */   private void updateRepoSelectedList() {
/*  51 */     this.repository.setSelected((Collection)Lists.reverse(this.selected).stream().map(Pack::getId).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   public void commit() {
/*  55 */     updateRepoSelectedList();
/*  56 */     this.output.accept(this.repository);
/*     */   }
/*     */   
/*     */   public void findNewPacks() {
/*  60 */     this.repository.reload();
/*     */     
/*  62 */     this.selected.retainAll(this.repository.getAvailablePacks());
/*     */     
/*  64 */     this.unselected.clear();
/*  65 */     this.unselected.addAll(this.repository.getAvailablePacks());
/*  66 */     this.unselected.removeAll(this.selected);
/*     */   }
/*     */   
/*     */   public static interface Entry {
/*     */     Identifier getIconTexture();
/*     */     
/*     */     PackCompatibility getCompatibility();
/*     */     
/*     */     String getId();
/*     */     
/*     */     Component getTitle();
/*     */     
/*     */     Component getDescription();
/*     */     
/*     */     PackSource getPackSource();
/*     */     
/*     */     default Component getExtendedDescription() {
/*  83 */       return getPackSource().decorate(getDescription());
/*     */     }
/*     */     
/*     */     boolean isFixedPosition();
/*     */     
/*     */     boolean isRequired();
/*     */     
/*     */     void select();
/*     */     
/*     */     void unselect();
/*     */     
/*     */     void moveUp();
/*     */     
/*     */     void moveDown();
/*     */     
/*     */     boolean isSelected();
/*     */     
/*     */     default boolean canSelect() {
/* 101 */       return !isSelected();
/*     */     }
/*     */     
/*     */     default boolean canUnselect() {
/* 105 */       return (isSelected() && !isRequired());
/*     */     }
/*     */     
/*     */     boolean canMoveUp();
/*     */     
/*     */     boolean canMoveDown();
/*     */   }
/*     */   
/*     */   public abstract class EntryBase implements Entry {
/*     */     private final Pack pack;
/*     */     
/*     */     public EntryBase(Pack pack) {
/* 117 */       this.pack = pack;
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract List<Pack> getSelfList();
/*     */     
/*     */     protected abstract List<Pack> getOtherList();
/*     */     
/*     */     public Identifier getIconTexture() {
/* 126 */       return PackSelectionModel.this.iconGetter.apply(this.pack);
/*     */     }
/*     */ 
/*     */     
/*     */     public PackCompatibility getCompatibility() {
/* 131 */       return this.pack.getCompatibility();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 136 */       return this.pack.getId();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getTitle() {
/* 141 */       return this.pack.getTitle();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getDescription() {
/* 146 */       return this.pack.getDescription();
/*     */     }
/*     */ 
/*     */     
/*     */     public PackSource getPackSource() {
/* 151 */       return this.pack.getPackSource();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFixedPosition() {
/* 156 */       return this.pack.isFixedPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRequired() {
/* 161 */       return this.pack.isRequired();
/*     */     }
/*     */     
/*     */     protected void toggleSelection() {
/* 165 */       getSelfList().remove(this.pack);
/* 166 */       this.pack.getDefaultPosition().insert(getOtherList(), this.pack, Pack::selectionConfig, true);
/* 167 */       PackSelectionModel.this.onListChanged.accept(this);
/* 168 */       PackSelectionModel.this.updateRepoSelectedList();
/* 169 */       updateHighContrastOptionInstance();
/*     */     }
/*     */     
/*     */     private void updateHighContrastOptionInstance() {
/* 173 */       if (this.pack.getId().equals("high_contrast")) {
/* 174 */         OptionInstance<Boolean> highContrastMode = (Minecraft.getInstance()).options.highContrast();
/* 175 */         highContrastMode.set(!((Boolean)highContrastMode.get()));
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void move(int direction) {
/* 180 */       List<Pack> list = getSelfList();
/* 181 */       int currentPos = list.indexOf(this.pack);
/* 182 */       list.remove(currentPos);
/* 183 */       list.add(currentPos + direction, this.pack);
/* 184 */       PackSelectionModel.this.onListChanged.accept(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canMoveUp() {
/* 189 */       List<Pack> list = getSelfList();
/* 190 */       int index = list.indexOf(this.pack);
/* 191 */       return (index > 0 && !((Pack)list.get(index - 1)).isFixedPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public void moveUp() {
/* 196 */       move(-1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canMoveDown() {
/* 201 */       List<Pack> list = getSelfList();
/* 202 */       int index = list.indexOf(this.pack);
/* 203 */       return (index >= 0 && index < list.size() - 1 && !((Pack)list.get(index + 1)).isFixedPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public void moveDown() {
/* 208 */       move(1);
/*     */     }
/*     */   }
/*     */   
/*     */   private class SelectedPackEntry extends EntryBase {
/*     */     public SelectedPackEntry(Pack pack) {
/* 214 */       super(pack);
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Pack> getSelfList() {
/* 219 */       return PackSelectionModel.this.selected;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Pack> getOtherList() {
/* 224 */       return PackSelectionModel.this.unselected;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSelected() {
/* 229 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void select() {}
/*     */ 
/*     */     
/*     */     public void unselect() {
/* 238 */       toggleSelection();
/*     */     }
/*     */   }
/*     */   
/*     */   private class UnselectedPackEntry extends EntryBase {
/*     */     public UnselectedPackEntry(Pack pack) {
/* 244 */       super(pack);
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Pack> getSelfList() {
/* 249 */       return PackSelectionModel.this.unselected;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Pack> getOtherList() {
/* 254 */       return PackSelectionModel.this.selected;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSelected() {
/* 259 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void select() {
/* 264 */       toggleSelection();
/*     */     }
/*     */     
/*     */     public void unselect() {}
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/packs/PackSelectionModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */