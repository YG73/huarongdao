package com.example.huarongdao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
    private MediaPlayer bgmPlayer;
    private MediaPlayer succeedPlayer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 首次进入主界面弹窗
        SharedPreferences prefs = getSharedPreferences("huarongdao_prefs", MODE_PRIVATE);
        boolean firstEnterGame = prefs.getBoolean("first_enter_game", true);
        if (firstEnterGame) {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.welcome_title))
                .setMessage(getString(R.string.welcome_message))
                .setPositiveButton(getString(R.string.got_it), null)
                .show();
            prefs.edit().putBoolean("first_enter_game", false).apply();
        }
        
        // 初始化视图组件
        gameView = findViewById(R.id.gameView);
        codeEditText = findViewById(R.id.codeEditText);
        movesTextView = findViewById(R.id.movesTextView);
        Button executeButton = findViewById(R.id.executeButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button hintButton = findViewById(R.id.hintButton);
        Button rankingButton = findViewById(R.id.rankingButton);
        
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
        
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });
        
        // 启动背景音乐
        bgmPlayer = MediaPlayer.create(this, R.raw.backgroud_music);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
        }
        if (succeedPlayer != null) {
            succeedPlayer.release();
            succeedPlayer = null;
        }
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
        level.setDescription("不同布局的关卡");
        level.setDifficulty(levelId);
        level.setGridWidth(4);
        level.setGridHeight(5);

        java.util.List<Block> blocks = new java.util.ArrayList<>();

        if (levelId == 1) {
            // 第一关布局
            Block functionBlock = new Block();
            functionBlock.setId("function");
            functionBlock.setType(BlockType.KEYWORD_FUNCTION);
            functionBlock.setWidth(2);
            functionBlock.setHeight(2);
            functionBlock.setRow(0);
            functionBlock.setCol(1);
            functionBlock.setTarget(true);
            blocks.add(functionBlock);

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
        } else if (levelId == 2) {
            // 第二关布局（修正：无重叠）
            Block functionBlock = new Block();
            functionBlock.setId("function");
            functionBlock.setType(BlockType.KEYWORD_FUNCTION);
            functionBlock.setWidth(2);
            functionBlock.setHeight(2);
            functionBlock.setRow(0);
            functionBlock.setCol(0);
            functionBlock.setTarget(true);
            blocks.add(functionBlock);

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
        } else if (levelId == 3) {
            // 第三关布局
            Block functionBlock = new Block();
            functionBlock.setId("function");
            functionBlock.setType(BlockType.KEYWORD_FUNCTION);
            functionBlock.setWidth(2);
            functionBlock.setHeight(2);
            functionBlock.setRow(2);
            functionBlock.setCol(2);
            functionBlock.setTarget(true);
            blocks.add(functionBlock);

            Block ifBlock = new Block();
            ifBlock.setId("if");
            ifBlock.setType(BlockType.KEYWORD_IF);
            ifBlock.setWidth(2);
            ifBlock.setHeight(1);
            ifBlock.setRow(0);
            ifBlock.setCol(0);
            blocks.add(ifBlock);

            Block elseBlock = new Block();
            elseBlock.setId("else");
            elseBlock.setType(BlockType.KEYWORD_ELSE);
            elseBlock.setWidth(2);
            elseBlock.setHeight(1);
            elseBlock.setRow(4);
            elseBlock.setCol(0);
            blocks.add(elseBlock);

            Block returnBlock = new Block();
            returnBlock.setId("return");
            returnBlock.setType(BlockType.KEYWORD_RETURN);
            returnBlock.setWidth(1);
            returnBlock.setHeight(2);
            returnBlock.setRow(0);
            returnBlock.setCol(3);
            blocks.add(returnBlock);

            Block varBlock = new Block();
            varBlock.setId("var");
            varBlock.setType(BlockType.VARIABLE);
            varBlock.setWidth(1);
            varBlock.setHeight(2);
            varBlock.setRow(2);
            varBlock.setCol(0);
            blocks.add(varBlock);
        } else {
            // 默认用第一关布局
            Block functionBlock = new Block();
            functionBlock.setId("function");
            functionBlock.setType(BlockType.KEYWORD_FUNCTION);
            functionBlock.setWidth(2);
            functionBlock.setHeight(2);
            functionBlock.setRow(0);
            functionBlock.setCol(1);
            functionBlock.setTarget(true);
            blocks.add(functionBlock);

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
        }
        level.setBlocks(blocks);
        level.setSolutionSteps(12);
        return level;
    }
    
    private String getSampleCode(int levelId) {
        return "// 输入移动命令\n" +
               "// 格式: move [block_letter] [direction]\n" +
               "// 示例:\n" +
               "// move D down\n" +
               "// move A left\n\n" +
               "move D down\n" +
               "move A left";
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
        // 记录每关最佳步数
        SharedPreferences rankingPrefs = getSharedPreferences("huarongdao_ranking", MODE_PRIVATE);
        int bestSteps = rankingPrefs.getInt("level_" + currentLevel.getId() + "_best_steps", Integer.MAX_VALUE);
        if (moves < bestSteps) {
            rankingPrefs.edit()
                .putInt("level_" + currentLevel.getId() + "_best_steps", moves)
                .apply();
            // 可选：弹窗提示刷新最佳成绩
        }
        // 停止背景音乐，播放通关音效
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
        succeedPlayer = MediaPlayer.create(this, R.raw.succeed);
        succeedPlayer.setOnCompletionListener(mp -> {
            mp.release();
            succeedPlayer = null;
        });
        succeedPlayer.start();
        // 显示完成对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("恭喜完成!")
               .setMessage("你用了 " + moves + " 步完成此关卡")
               .setPositiveButton("下一关", (dialog, which) -> {
                   // 暂停通关音效，重新播放背景音乐
                   if (succeedPlayer != null && succeedPlayer.isPlaying()) {
                       succeedPlayer.stop();
                       succeedPlayer.release();
                       succeedPlayer = null;
                   }
                   if (bgmPlayer != null) {
                       bgmPlayer.release();
                   }
                   bgmPlayer = MediaPlayer.create(this, R.raw.backgroud_music);
                   bgmPlayer.setLooping(true);
                   bgmPlayer.start();
                   loadLevel(currentLevel.getId() + 1);
               })
               .setNegativeButton("再玩一次", (dialog, which) -> {
                   // 暂停通关音效，重新播放背景音乐
                   if (succeedPlayer != null && succeedPlayer.isPlaying()) {
                       succeedPlayer.stop();
                       succeedPlayer.release();
                       succeedPlayer = null;
                   }
                   if (bgmPlayer != null) {
                       bgmPlayer.release();
                   }
                   bgmPlayer = MediaPlayer.create(this, R.raw.backgroud_music);
                   bgmPlayer.setLooping(true);
                   bgmPlayer.start();
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
            // 替换id为字母
            String display = convertHintToLetter(next);
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("下一步建议：\n" + display)
                .setPositiveButton("知道了", null)
                .show();
        }
    }

    // 将提示命令中的id映射为字母
    private String convertHintToLetter(String hint) {
        // 例如: move function down -> move D down
        String[] parts = hint.split(" ");
        if (parts.length == 3 && parts[0].equals("move")) {
            return "移动 " + getBlockLetterById(parts[1]) + " 向 " + getDirectionCN(parts[2]);
        }
        return hint;
    }
    private String getBlockLetterById(String id) {
        switch (id) {
            case "if": return "A";
            case "else": return "B";
            case "while": return "C";
            case "function": return "D";
            case "return": return "E";
            case "var": return "F";
            case "const": return "G";
            case "comment": return "H";
            default: return id;
        }
    }
    private String getDirectionCN(String dir) {
        switch (dir) {
            case "up": return "上";
            case "down": return "下";
            case "left": return "左";
            case "right": return "右";
            default: return dir;
        }
    }
}    