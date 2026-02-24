/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import com.mojang.math.Divisor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class GridLayout
/*     */   extends AbstractLayout {
/*  12 */   private final List<LayoutElement> children = new ArrayList<>();
/*  13 */   private final List<CellInhabitant> cellInhabitants = new ArrayList<>();
/*  14 */   private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();
/*  15 */   private int rowSpacing = 0;
/*  16 */   private int columnSpacing = 0;
/*     */   
/*     */   public GridLayout() {
/*  19 */     this(0, 0);
/*     */   }
/*     */   
/*     */   public GridLayout(int x, int y) {
/*  23 */     super(x, y, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  28 */     super.arrangeElements();
/*     */     
/*  30 */     int maxRow = 0;
/*  31 */     int maxColumn = 0;
/*     */     
/*  33 */     for (CellInhabitant cellInhabitant : this.cellInhabitants) {
/*  34 */       maxRow = Math.max(cellInhabitant.getLastOccupiedRow(), maxRow);
/*  35 */       maxColumn = Math.max(cellInhabitant.getLastOccupiedColumn(), maxColumn);
/*     */     } 
/*     */     
/*  38 */     int[] maxColumnWidths = new int[maxColumn + 1];
/*  39 */     int[] maxRowHeights = new int[maxRow + 1];
/*     */     
/*  41 */     for (CellInhabitant cellInhabitant : this.cellInhabitants) {
/*  42 */       int cellInhabitantHeight = cellInhabitant.getHeight() - (cellInhabitant.occupiedRows - 1) * this.rowSpacing;
/*  43 */       Divisor heightDivisor = new Divisor(cellInhabitantHeight, cellInhabitant.occupiedRows);
/*  44 */       for (int i = cellInhabitant.row; i <= cellInhabitant.getLastOccupiedRow(); i++) {
/*  45 */         maxRowHeights[i] = Math.max(maxRowHeights[i], heightDivisor.nextInt());
/*     */       }
/*  47 */       int cellInhabitantWidth = cellInhabitant.getWidth() - (cellInhabitant.occupiedColumns - 1) * this.columnSpacing;
/*  48 */       Divisor widthDivisor = new Divisor(cellInhabitantWidth, cellInhabitant.occupiedColumns);
/*  49 */       for (int j = cellInhabitant.column; j <= cellInhabitant.getLastOccupiedColumn(); j++) {
/*  50 */         maxColumnWidths[j] = Math.max(maxColumnWidths[j], widthDivisor.nextInt());
/*     */       }
/*     */     } 
/*     */     
/*  54 */     int[] columnXOffsets = new int[maxColumn + 1];
/*  55 */     int[] rowYOffsets = new int[maxRow + 1];
/*     */     
/*  57 */     columnXOffsets[0] = 0;
/*  58 */     for (int column = 1; column <= maxColumn; column++) {
/*  59 */       columnXOffsets[column] = columnXOffsets[column - 1] + maxColumnWidths[column - 1] + this.columnSpacing;
/*     */     }
/*  61 */     rowYOffsets[0] = 0;
/*  62 */     for (int row = 1; row <= maxRow; row++) {
/*  63 */       rowYOffsets[row] = rowYOffsets[row - 1] + maxRowHeights[row - 1] + this.rowSpacing;
/*     */     }
/*     */     
/*  66 */     for (CellInhabitant cellInhabitant : this.cellInhabitants) {
/*  67 */       int availableWidth = 0;
/*  68 */       for (int i = cellInhabitant.column; i <= cellInhabitant.getLastOccupiedColumn(); i++) {
/*  69 */         availableWidth += maxColumnWidths[i];
/*     */       }
/*  71 */       availableWidth += this.columnSpacing * (cellInhabitant.occupiedColumns - 1);
/*  72 */       cellInhabitant.setX(getX() + columnXOffsets[cellInhabitant.column], availableWidth);
/*     */       
/*  74 */       int availableHeight = 0;
/*  75 */       for (int j = cellInhabitant.row; j <= cellInhabitant.getLastOccupiedRow(); j++) {
/*  76 */         availableHeight += maxRowHeights[j];
/*     */       }
/*  78 */       availableHeight += this.rowSpacing * (cellInhabitant.occupiedRows - 1);
/*  79 */       cellInhabitant.setY(getY() + rowYOffsets[cellInhabitant.row], availableHeight);
/*     */     } 
/*     */     
/*  82 */     this.width = columnXOffsets[maxColumn] + maxColumnWidths[maxColumn];
/*  83 */     this.height = rowYOffsets[maxRow] + maxRowHeights[maxRow];
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column) {
/*  87 */     return addChild(child, row, column, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, LayoutSettings cellSettings) {
/*  91 */     return addChild(child, row, column, 1, 1, cellSettings);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  95 */     return addChild(child, row, column, 1, 1, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns) {
/*  99 */     return addChild(child, row, column, rows, columns, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns, LayoutSettings cellSettings) {
/* 103 */     if (rows < 1) {
/* 104 */       throw new IllegalArgumentException("Occupied rows must be at least 1");
/*     */     }
/* 106 */     if (columns < 1) {
/* 107 */       throw new IllegalArgumentException("Occupied columns must be at least 1");
/*     */     }
/* 109 */     this.cellInhabitants.add(new CellInhabitant((LayoutElement)child, row, column, rows, columns, cellSettings));
/* 110 */     this.children.add((LayoutElement)child);
/* 111 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, int row, int column, int rows, int columns, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 115 */     return addChild(child, row, column, rows, columns, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public GridLayout columnSpacing(int columnSpacing) {
/* 119 */     this.columnSpacing = columnSpacing;
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public GridLayout rowSpacing(int rowSpacing) {
/* 124 */     this.rowSpacing = rowSpacing;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public GridLayout spacing(int spacing) {
/* 129 */     return columnSpacing(spacing).rowSpacing(spacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/* 134 */     this.children.forEach(layoutElementVisitor);
/*     */   }
/*     */   
/*     */   public LayoutSettings newCellSettings() {
/* 138 */     return this.defaultCellSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultCellSetting() {
/* 142 */     return this.defaultCellSettings;
/*     */   }
/*     */   
/*     */   public RowHelper createRowHelper(int columns) {
/* 146 */     return new RowHelper(columns);
/*     */   }
/*     */   
/*     */   private static class CellInhabitant extends AbstractLayout.AbstractChildWrapper {
/*     */     private final int row;
/*     */     private final int column;
/*     */     private final int occupiedRows;
/*     */     private final int occupiedColumns;
/*     */     
/*     */     private CellInhabitant(LayoutElement widget, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings cellSettings) {
/* 156 */       super(widget, cellSettings.getExposed());
/* 157 */       this.row = row;
/* 158 */       this.column = column;
/* 159 */       this.occupiedRows = occupiedRows;
/* 160 */       this.occupiedColumns = occupiedColumns;
/*     */     }
/*     */     
/*     */     public int getLastOccupiedRow() {
/* 164 */       return this.row + this.occupiedRows - 1;
/*     */     }
/*     */     
/*     */     public int getLastOccupiedColumn() {
/* 168 */       return this.column + this.occupiedColumns - 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final class RowHelper {
/*     */     private final int columns;
/*     */     private int index;
/*     */     
/*     */     private RowHelper(int columns) {
/* 177 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget) {
/* 181 */       return addChild(widget, 1);
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, int columnWidth) {
/* 185 */       return addChild(widget, columnWidth, defaultCellSetting());
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, LayoutSettings layoutSettings) {
/* 189 */       return addChild(widget, 1, layoutSettings);
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(T widget, int columnWidth, LayoutSettings layoutSettings) {
/* 193 */       int row = this.index / this.columns;
/* 194 */       int columnBegin = this.index % this.columns;
/*     */       
/* 196 */       if (columnBegin + columnWidth > this.columns) {
/* 197 */         row++;
/* 198 */         columnBegin = 0;
/* 199 */         this.index = Mth.roundToward(this.index, this.columns);
/*     */       } 
/* 201 */       this.index += columnWidth;
/*     */       
/* 203 */       return GridLayout.this.addChild(widget, row, columnBegin, 1, columnWidth, layoutSettings);
/*     */     }
/*     */     
/*     */     public GridLayout getGrid() {
/* 207 */       return GridLayout.this;
/*     */     }
/*     */     
/*     */     public LayoutSettings newCellSettings() {
/* 211 */       return GridLayout.this.newCellSettings();
/*     */     }
/*     */     
/*     */     public LayoutSettings defaultCellSetting() {
/* 215 */       return GridLayout.this.defaultCellSetting();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/GridLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */