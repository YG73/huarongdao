package com.example.huarongdao.model;

import java.util.*;

/**
 * 华容道BFS求解工具类
 * 用于搜索最短移动路径（提示/自动求解）
 */
public class BFSUtils {
    public static class State {
        public List<Block> blocks;
        public List<String> path; // 记录移动步骤
        public State(List<Block> blocks, List<String> path) {
            // 深拷贝blocks
            this.blocks = new ArrayList<>();
            for (Block b : blocks) {
                Block copy = new Block();
                copy.setId(b.getId());
                copy.setType(b.getType());
                copy.setWidth(b.getWidth());
                copy.setHeight(b.getHeight());
                copy.setRow(b.getRow());
                copy.setCol(b.getCol());
                copy.setTarget(b.isTarget());
                this.blocks.add(copy);
            }
            this.path = new ArrayList<>(path);
        }
        // 用于判重的唯一字符串
        public String encode() {
            StringBuilder sb = new StringBuilder();
            for (Block b : blocks) {
                sb.append(b.getId()).append(':').append(b.getRow()).append(',').append(b.getCol()).append(';');
            }
            return sb.toString();
        }
    }

    /**
     * BFS搜索最短解路径
     * @param initBlocks 初始方块列表
     * @param level 关卡信息（用于边界判定）
     * @return 最短移动路径（如 ["move function down", ...]），无解返回空列表
     */
    public static List<String> bfs(List<Block> initBlocks, Level level) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        State start = new State(initBlocks, new ArrayList<>());
        queue.offer(start);
        visited.add(start.encode());

        while (!queue.isEmpty()) {
            State curr = queue.poll();
            // 检查是否胜利
            if (isGameCompleted(curr.blocks, level)) {
                return curr.path;
            }
            // 遍历所有方块和方向
            for (Block block : curr.blocks) {
                for (Direction dir : Direction.values()) {
                    if (canMove(block, dir, curr.blocks, level)) {
                        // 生成新状态
                        List<Block> newBlocks = new ArrayList<>();
                        for (Block b : curr.blocks) {
                            Block copy = new Block();
                            copy.setId(b.getId());
                            copy.setType(b.getType());
                            copy.setWidth(b.getWidth());
                            copy.setHeight(b.getHeight());
                            copy.setRow(b.getRow());
                            copy.setCol(b.getCol());
                            copy.setTarget(b.isTarget());
                            newBlocks.add(copy);
                        }
                        // 移动block
                        for (Block b : newBlocks) {
                            if (b.getId().equals(block.getId())) {
                                switch (dir) {
                                    case UP: b.setRow(b.getRow() - 1); break;
                                    case DOWN: b.setRow(b.getRow() + 1); break;
                                    case LEFT: b.setCol(b.getCol() - 1); break;
                                    case RIGHT: b.setCol(b.getCol() + 1); break;
                                }
                                break;
                            }
                        }
                        List<String> newPath = new ArrayList<>(curr.path);
                        newPath.add("move " + block.getId() + " " + dir.name().toLowerCase());
                        State next = new State(newBlocks, newPath);
                        String code = next.encode();
                        if (!visited.contains(code)) {
                            visited.add(code);
                            queue.offer(next);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(); // 无解
    }

    // 判定是否胜利
    private static boolean isGameCompleted(List<Block> blocks, Level level) {
        for (Block block : blocks) {
            if (block.isTarget()) {
                return block.getRow() == level.getGridHeight() - 2 &&
                        block.getCol() == level.getGridWidth() / 2 - 1;
            }
        }
        return false;
    }

    // 判定block能否向dir移动
    public static boolean canMove(Block block, Direction dir, List<Block> blocks, Level level) {
        int newRow = block.getRow();
        int newCol = block.getCol();
        switch (dir) {
            case UP: newRow--; break;
            case DOWN: newRow++; break;
            case LEFT: newCol--; break;
            case RIGHT: newCol++; break;
        }
        // 边界判定
        if (newRow < 0 || newRow + block.getHeight() > level.getGridHeight() ||
            newCol < 0 || newCol + block.getWidth() > level.getGridWidth()) {
            return false;
        }
        // 与其他方块冲突判定
        for (Block other : blocks) {
            if (other == block) continue;
            if (!(newCol + block.getWidth() <= other.getCol() ||
                  newCol >= other.getCol() + other.getWidth() ||
                  newRow + block.getHeight() <= other.getRow() ||
                  newRow >= other.getRow() + other.getHeight())) {
                return false;
            }
        }
        return true;
    }
} 