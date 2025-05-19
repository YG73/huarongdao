package com.example.huarongdao.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Arrays;

public class Level {
    private int id;
    private String title;
    private String description;
    private int difficulty;
    private int gridWidth;
    private int gridHeight;
    private List<Block> blocks;
    private int solutionSteps;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public int getGridWidth() { return gridWidth; }
    public void setGridWidth(int gridWidth) { this.gridWidth = gridWidth; }

    public int getGridHeight() { return gridHeight; }
    public void setGridHeight(int gridHeight) { this.gridHeight = gridHeight; }

    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }

    public int getSolutionSteps() { return solutionSteps; }
    public void setSolutionSteps(int solutionSteps) { this.solutionSteps = solutionSteps; }

    // 随机生成有解棋局
    public static Level generateRandomLevel(int width, int height, int steps) {
        // 以标准初始局为基础
        Level base = new Level();
        base.setId(999);
        base.setTitle("随机关卡");
        base.setDescription("随机生成的有解棋局");
        base.setDifficulty(1);
        base.setGridWidth(width);
        base.setGridHeight(height);
        List<Block> blocks = new ArrayList<>();
        // 目标方块
        Block functionBlock = new Block();
        functionBlock.setId("function");
        functionBlock.setType(BlockType.KEYWORD_FUNCTION);
        functionBlock.setWidth(2);
        functionBlock.setHeight(2);
        functionBlock.setRow(0);
        functionBlock.setCol(1);
        functionBlock.setTarget(true);
        blocks.add(functionBlock);
        // 其它方块（可根据实际需求扩展）
        Block ifBlock = new Block();
        ifBlock.setId("if");
        ifBlock.setType(BlockType.KEYWORD_IF);
        ifBlock.setWidth(2);
        ifBlock.setHeight(1);
        ifBlock.setRow(2);
        ifBlock.setCol(0);
        blocks.add(ifBlock);
        Block elseBlock = new Block();
        elseBlock.setId("else");
        elseBlock.setType(BlockType.KEYWORD_ELSE);
        elseBlock.setWidth(2);
        elseBlock.setHeight(1);
        elseBlock.setRow(2);
        elseBlock.setCol(2);
        blocks.add(elseBlock);
        Block returnBlock = new Block();
        returnBlock.setId("return");
        returnBlock.setType(BlockType.KEYWORD_RETURN);
        returnBlock.setWidth(1);
        returnBlock.setHeight(2);
        returnBlock.setRow(3);
        returnBlock.setCol(0);
        blocks.add(returnBlock);
        Block varBlock = new Block();
        varBlock.setId("var");
        varBlock.setType(BlockType.VARIABLE);
        varBlock.setWidth(1);
        varBlock.setHeight(2);
        varBlock.setRow(3);
        varBlock.setCol(3);
        blocks.add(varBlock);
        base.setBlocks(blocks);
        base.setSolutionSteps(0);

        // 随机合法移动steps步
        Random rand = new Random();
        for (int i = 0; i < steps; i++) {
            List<Block> movable = new ArrayList<>(blocks);
            Collections.shuffle(movable, rand);
            boolean moved = false;
            for (Block b : movable) {
                List<Direction> dirs = Arrays.asList(Direction.values());
                Collections.shuffle(dirs, rand);
                for (Direction dir : dirs) {
                    if (BFSUtils.canMove(b, dir, blocks, base)) {
                        // 反向移动（保证有解）
                        switch (dir) {
                            case UP: b.setRow(b.getRow() + 1); break;
                            case DOWN: b.setRow(b.getRow() - 1); break;
                            case LEFT: b.setCol(b.getCol() + 1); break;
                            case RIGHT: b.setCol(b.getCol() - 1); break;
                        }
                        moved = true;
                        break;
                    }
                }
                if (moved) break;
            }
        }
        return base;
    }
}