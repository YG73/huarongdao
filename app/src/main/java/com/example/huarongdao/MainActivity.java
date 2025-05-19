package com.example.huarongdao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huarongdao.model.Block;
import com.example.huarongdao.model.BlockType;
import com.example.huarongdao.model.Level;


public class MainActivity extends AppCompatActivity implements HuaRongDaoView.GameListener {
    private HuaRongDaoView gameView;
    private EditText codeEditText;
    private TextView movesTextView;
    private CodeInterpreter codeInterpreter;
    private Level currentLevel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化视图组件
        gameView = findViewById(R.id.gameView);
        codeEditText = findViewById(R.id.codeEditText);
        movesTextView = findViewById(R.id.movesTextView);
        Button executeButton = findViewById(R.id.executeButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button hintButton = findViewById(R.id.hintButton);
        
        // 设置游戏监听器
        gameView.setGameListener(this);
        
        // 初始化代码解析器
        codeInterpreter = new CodeInterpreter(gameView);
        
        // 加载第一关
        loadLevel(1);
        
        // 设置执行按钮点击事件
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeCode();
            }
        });
        
        // 设置重置按钮点击事件
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLevel(currentLevel.getId());
            }
        });
        
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
            }
        });
    }
    
    private void loadLevel(int levelId) {
        // 这里应该从资源或数据库加载关卡数据
        // 为简化示例，我们直接创建一个关卡
        currentLevel = createSampleLevel(levelId);
        gameView.setLevel(currentLevel);
        movesTextView.setText("步数: 0");
        
        // 设置示例代码
        codeEditText.setText(getSampleCode(levelId));
    }
    
    private Level createSampleLevel(int levelId) {
        Level level = new Level();
        level.setId(levelId);
        level.setTitle("第" + levelId + "关");
        level.setDescription("移动方块使function到达出口");
        level.setDifficulty(levelId);
        level.setGridWidth(4);
        level.setGridHeight(5);
        
        // 创建方块
        java.util.List<Block> blocks = new java.util.ArrayList<>();
        
        // 目标方块 (function)
        Block functionBlock = new Block();
        functionBlock.setId("function");
        functionBlock.setType(BlockType.KEYWORD_FUNCTION);
        functionBlock.setWidth(2);
        functionBlock.setHeight(2);
        functionBlock.setRow(0);
        functionBlock.setCol(1);
        functionBlock.setTarget(true);
        blocks.add(functionBlock);
        
        // if方块
        Block ifBlock = new Block();
        ifBlock.setId("if");
        ifBlock.setType(BlockType.KEYWORD_IF);
        ifBlock.setWidth(2);
        ifBlock.setHeight(1);
        ifBlock.setRow(2);
        ifBlock.setCol(0);
        blocks.add(ifBlock);
        
        // else方块
        Block elseBlock = new Block();
        elseBlock.setId("else");
        elseBlock.setType(BlockType.KEYWORD_ELSE);
        elseBlock.setWidth(2);
        elseBlock.setHeight(1);
        elseBlock.setRow(2);
        elseBlock.setCol(2);
        blocks.add(elseBlock);
        
        // return方块
        Block returnBlock = new Block();
        returnBlock.setId("return");
        returnBlock.setType(BlockType.KEYWORD_RETURN);
        returnBlock.setWidth(1);
        returnBlock.setHeight(2);
        returnBlock.setRow(3);
        returnBlock.setCol(0);
        blocks.add(returnBlock);
        
        // var方块
        Block varBlock = new Block();
        varBlock.setId("var");
        varBlock.setType(BlockType.VARIABLE);
        varBlock.setWidth(1);
        varBlock.setHeight(2);
        varBlock.setRow(3);
        varBlock.setCol(3);
        blocks.add(varBlock);
        
        level.setBlocks(blocks);
        level.setSolutionSteps(12);
        
        return level;
    }
    
    private String getSampleCode(int levelId) {
        return "// 输入移动命令\n" +
               "// 格式: move [block_id] [direction]\n" +
               "// 示例:\n" +
               "// move function down\n" +
               "// move if left\n\n" +
               "move function down\n" +
               "move if left";
    }
    
    private void executeCode() {
        String code = codeEditText.getText().toString();
        String output = codeInterpreter.executeCode(code);
        
        // 显示执行结果
        Toast.makeText(this, "代码执行完成", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onMovesChanged(int moves) {
        movesTextView.setText("步数: " + moves);
    }
    
    @Override
    public void onGameCompleted(int moves) {
        // 显示完成对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("恭喜完成!")
               .setMessage("你用了 " + moves + " 步完成此关卡")
               .setPositiveButton("下一关", (dialog, which) -> {
                   loadLevel(currentLevel.getId() + 1);
               })
               .setNegativeButton("再玩一次", (dialog, which) -> {
                   loadLevel(currentLevel.getId());
               })
               .show();
    }
    
    private void showHint() {
        // 调用BFSUtils获取最优解路径
        java.util.List<com.example.huarongdao.model.Block> blocks = new java.util.ArrayList<>();
        for (com.example.huarongdao.model.Block b : gameView.getBlocks()) {
            com.example.huarongdao.model.Block copy = new com.example.huarongdao.model.Block();
            copy.setId(b.getId());
            copy.setType(b.getType());
            copy.setWidth(b.getWidth());
            copy.setHeight(b.getHeight());
            copy.setRow(b.getRow());
            copy.setCol(b.getCol());
            copy.setTarget(b.isTarget());
            blocks.add(copy);
        }
        java.util.List<String> path = com.example.huarongdao.model.BFSUtils.bfs(blocks, currentLevel);
        if (path.isEmpty()) {
            Toast.makeText(this, "无解或已完成！", Toast.LENGTH_SHORT).show();
        } else {
            String next = path.get(0);
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("下一步建议：\n" + next)
                .setPositiveButton("知道了", null)
                .show();
        }
    }
}    