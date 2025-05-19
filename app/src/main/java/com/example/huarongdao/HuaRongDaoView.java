package com.example.huarongdao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import com.example.huarongdao.model.Block;
import com.example.huarongdao.model.BlockType;
import com.example.huarongdao.model.Direction;
import com.example.huarongdao.model.Level;

import java.util.ArrayList;
import java.util.List;

public class HuaRongDaoView extends View {
    private static final int CELL_SIZE = 100; // 每个格子的大小
    private static final int PADDING = 10; // 内边距
    
    private List<Block> blocks = new ArrayList<>();
    private Level currentLevel;
    private Block selectedBlock = null;
    private float startX, startY;
    private Paint gridPaint, blockPaint, textPaint;
    private GameListener gameListener;
    private int moves = 0;
    
    public HuaRongDaoView(Context context) {
        super(context);
        init();
    }
    public List<Block> getBlocks() {
        return blocks;
    }
    
    public HuaRongDaoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        // 初始化画笔
        gridPaint = new Paint();
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(2);
        
        blockPaint = new Paint();
        blockPaint.setStyle(Paint.Style.FILL);
        
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
    
    public void setLevel(Level level) {
        this.currentLevel = level;
        this.blocks = new ArrayList<>(level.getBlocks());
        this.moves = 0;
        invalidate();
    }
    
    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (currentLevel == null) return;
        
        // 绘制网格
        drawGrid(canvas);
        
        // 绘制方块
        drawBlocks(canvas);
        
        // 绘制出口
        drawExit(canvas);
    }
    
    private void drawGrid(Canvas canvas) {
        int width = currentLevel.getGridWidth();
        int height = currentLevel.getGridHeight();
        
        // 绘制垂直线
        for (int i = 0; i <= width; i++) {
            canvas.drawLine(
                PADDING + i * CELL_SIZE, 
                PADDING, 
                PADDING + i * CELL_SIZE, 
                PADDING + height * CELL_SIZE, 
                gridPaint
            );
        }
        
        // 绘制水平线
        for (int i = 0; i <= height; i++) {
            canvas.drawLine(
                PADDING, 
                PADDING + i * CELL_SIZE, 
                PADDING + width * CELL_SIZE, 
                PADDING + i * CELL_SIZE, 
                gridPaint
            );
        }
    }
    
    private void drawBlocks(Canvas canvas) {
        for (Block block : blocks) {
            // 设置方块颜色
            int color = getBlockColor(block.getType());
            blockPaint.setColor(color);
            
            // 绘制方块
            float left = PADDING + block.getCol() * CELL_SIZE;
            float top = PADDING + block.getRow() * CELL_SIZE;
            float right = left + block.getWidth() * CELL_SIZE;
            float bottom = top + block.getHeight() * CELL_SIZE;
            
            canvas.drawRect(left, top, right, bottom, blockPaint);
            
            // 绘制方块边框
            blockPaint.setColor(Color.BLACK);
            blockPaint.setStyle(Paint.Style.STROKE);
            blockPaint.setStrokeWidth(2);
            canvas.drawRect(left, top, right, bottom, blockPaint);
            blockPaint.setStyle(Paint.Style.FILL);
            
            // 绘制方块文本
            String text = getBlockText(block.getType());
            float textX = left + (right - left) / 2;
            float textY = top + (bottom - top) / 2 + 16; // 16是为了让文本居中
            canvas.drawText(text, textX, textY, textPaint);
        }
    }
    
    private void drawExit(Canvas canvas) {
        int width = currentLevel.getGridWidth();
        int height = currentLevel.getGridHeight();
        
        // 出口位置在底部中央
        float left = PADDING + (width/2 - 1) * CELL_SIZE;
        float top = PADDING + (height - 2) * CELL_SIZE;
        float right = left + 2 * CELL_SIZE;
        float bottom = top + 2 * CELL_SIZE;
        
        blockPaint.setColor(Color.YELLOW & 0x77FFFFFF); // 半透明黄色
        canvas.drawRect(left, top, right, bottom, blockPaint);
    }
    
    private int getBlockColor(BlockType type) {
        switch (type) {
            case KEYWORD_IF: return Color.rgb(66, 133, 244); // 蓝色
            case KEYWORD_ELSE: return Color.rgb(234, 67, 53); // 红色
            case KEYWORD_WHILE: return Color.rgb(251, 188, 5); // 黄色
            case KEYWORD_FUNCTION: return Color.rgb(52, 168, 83); // 绿色
            case KEYWORD_RETURN: return Color.rgb(142, 36, 170); // 紫色
            case VARIABLE: return Color.rgb(0, 172, 193); // 青色
            case CONSTANT: return Color.rgb(255, 152, 0); // 橙色
            case COMMENT: return Color.rgb(117, 117, 117); // 灰色
            default: return Color.GRAY;
        }
    }
    
    private String getBlockText(BlockType type) {
        switch (type) {
            case KEYWORD_IF: return "if";
            case KEYWORD_ELSE: return "else";
            case KEYWORD_WHILE: return "while";
            case KEYWORD_FUNCTION: return "function";
            case KEYWORD_RETURN: return "return";
            case VARIABLE: return "var x";
            case CONSTANT: return "const PI";
            case COMMENT: return "// comment";
            default: return "";
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                selectedBlock = getBlockAt(x, y);
                return true;
                
            case MotionEvent.ACTION_UP:
                if (selectedBlock != null) {
                    float dx = x - startX;
                    float dy = y - startY;
                    
                    // 确定移动方向
                    Direction direction = getDirection(dx, dy);
                    
                    if (direction != null) {
                        moveBlock(selectedBlock, direction);
                    }
                    
                    selectedBlock = null;
                }
                return true;
        }
        
        return super.onTouchEvent(event);
    }
    
    private Block getBlockAt(float x, float y) {
        // 检查是否点击了某个方块
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            float left = PADDING + block.getCol() * CELL_SIZE;
            float top = PADDING + block.getRow() * CELL_SIZE;
            float right = left + block.getWidth() * CELL_SIZE;
            float bottom = top + block.getHeight() * CELL_SIZE;
            
            if (x >= left && x <= right && y >= top && y <= bottom) {
                return block;
            }
        }
        
        return null;
    }
    
    private Direction getDirection(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            // 水平移动
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            // 垂直移动
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    
    // 移动方块的公共方法，也可以被代码解析器调用
    public void moveBlock(Block block, Direction direction) {
        int newRow = block.getRow();
        int newCol = block.getCol();
        
        switch (direction) {
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
        }
        
        if (isValidMove(block, newRow, newCol)) {
            // 更新方块位置
            block.setRow(newRow);
            block.setCol(newCol);
            moves++;
            
            // 播放音效
            playMoveSound();
            
            // 通知Activity步数更新
            if (gameListener != null) {
                gameListener.onMovesChanged(moves);
            }
            
            // 检查游戏是否完成
            if (isGameCompleted()) {
                if (gameListener != null) {
                    gameListener.onGameCompleted(moves);
                }
            }
            
            invalidate(); // 重绘视图
        }
    }
    
    private boolean isValidMove(Block block, int newRow, int newCol) {
        // 检查是否超出边界
        if (newRow < 0 || newRow + block.getHeight() > currentLevel.getGridHeight() ||
            newCol < 0 || newCol + block.getWidth() > currentLevel.getGridWidth()) {
            return false;
        }
        
        // 检查是否与其他方块冲突
        for (Block other : blocks) {
            if (other == block) continue;
            
            if (isOverlapping(block, other, newRow, newCol)) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isOverlapping(Block block1, Block block2, int newRow, int newCol) {
        return !(block1.getCol() + block1.getWidth() <= block2.getCol() ||
                newCol >= block2.getCol() + block2.getWidth() ||
                block1.getRow() + block1.getHeight() <= block2.getRow() ||
                newRow >= block2.getRow() + block2.getHeight());
    }
    
    private boolean isGameCompleted() {
        // 检查目标方块是否到达出口
        for (Block block : blocks) {
            if (block.isTarget()) {
                return block.getRow() == currentLevel.getGridHeight() - 2 &&
                       block.getCol() == currentLevel.getGridWidth() / 2 - 1;
            }
        }
        return false;
    }
    
    // 播放方块移动音效
    private void playMoveSound() {
        try {
            android.media.MediaPlayer player = android.media.MediaPlayer.create(getContext(), R.raw.move_sound);
            if (player != null) {
                player.setOnCompletionListener(mp -> mp.release());
                player.start();
            }
        } catch (Exception e) {
            // 忽略音效异常
        }
    }
    
    public interface GameListener {
        void onMovesChanged(int moves);
        void onGameCompleted(int moves);
    }
}    