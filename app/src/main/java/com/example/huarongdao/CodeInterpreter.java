package com.example.huarongdao;

import com.example.huarongdao.model.Block;
import com.example.huarongdao.model.Direction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeInterpreter {
    private HuaRongDaoView gameView;
    
    public CodeInterpreter(HuaRongDaoView gameView) {
        this.gameView = gameView;
    }
    
    // 解析并执行代码
    public String executeCode(String code) {
        StringBuilder output = new StringBuilder();
        
        // 分割代码行
        String[] lines = code.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            
            // 跳过空行和注释
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }
            
            // 解析移动命令
            String result = parseMoveCommand(line);
            if (result != null) {
                output.append(result).append("\n");
            } else {
                output.append("无法解析命令: ").append(line).append("\n");
            }
        }
        
        return output.toString();
    }
    
    // 解析移动命令
    private String parseMoveCommand(String command) {
        // 正则表达式匹配命令格式: move [block_id] [direction]
        Pattern pattern = Pattern.compile("move\\s+(\\w+)\\s+(up|down|left|right)");
        Matcher matcher = pattern.matcher(command);
        
        if (matcher.find()) {
            String blockId = matcher.group(1);
            String directionStr = matcher.group(2);
            
            // 将方向字符串转换为Direction枚举
            Direction direction = null;
            switch (directionStr) {
                case "up":
                    direction = Direction.UP;
                    break;
                case "down":
                    direction = Direction.DOWN;
                    break;
                case "left":
                    direction = Direction.LEFT;
                    break;
                case "right":
                    direction = Direction.RIGHT;
                    break;
            }
            
            if (direction != null) {
                // 查找对应的方块
                for (Block block : gameView.getBlocks()) {
                    if (block.getId().equalsIgnoreCase(blockId)) {
                        gameView.moveBlock(block, direction);
                        return "执行: " + command;
                    }
                }
                
                return "错误: 未找到方块 " + blockId;
            }
        }
        
        return null;
    }
}    