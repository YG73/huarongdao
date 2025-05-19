package com.example.huarongdao.model;

public class Block {
    private String id;
    private BlockType type;
    private int width;
    private int height;
    private int row;
    private int col;
    private boolean isTarget;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BlockType getType() { return type; }
    public void setType(BlockType type) { this.type = type; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public boolean isTarget() { return isTarget; }
    public void setTarget(boolean target) { isTarget = target; }
}
