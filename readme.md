# 《华容道》

## 作者信息

- 作者：余夏蓉
- 学号：42327021

**项目概述：**

本项目参考了 https://labuladong.online/algo/intro/game/#div_huarong-road 中关于华容道游戏内容，使用 Java 语言在 Android Studio 中开发了一个华容道游戏小项目。以下是项目的框架性任务清单和已完成模块：

---

## 技术栈与主要依赖

- **开发语言:** Java
- **开发环境:** Android Studio
- **UI框架:** Android SDK, AndroidX Libraries (appcompat, material, activity, constraintlayout)
- **核心算法:** BFS (广度优先搜索)
- **主要依赖:**
  - androidx.appcompat
  - com.google.android.material
  - androidx.activity
  - androidx.constraintlayout
  - junit (测试)
  - androidx.test.ext.junit (测试)
  - androidx.test.espresso.core (测试)

---

## 项目模块与开发进度

### **一、项目初始化与环境配置** ✅ (已完成)

1.  **创建Android Studio项目**
    *   选择`Empty Activity`模板，命名为`HuaRongRoad`，配置最低API级别（如Android 5.0/API 21）。
    *   确保项目使用Java语言，配置`build.gradle`依赖（如AndroidX库）。

2.  **目录结构规划**
    *   `app/src/main/java/com/huarongdao`：存放代码逻辑。
    *   `app/src/main/res`：存放资源文件（图片、布局、字符串等）。
    *   `app/src/test`：单元测试（可选）。

### **二、核心功能模块开发**

#### **1. 游戏界面模块**

*   **布局设计**
    *   **主界面**（`activity_main.xml`）： ✅（已完成，已包含棋盘、步数、按钮等核心区域，并适配移动端）
        *   游戏棋盘（使用`HuaRongDaoView`展示方块）。
        *   操作按钮（重置、撤销、提示、音效开关等）。
        *   计时/步数显示区域。
    *   **方块样式**（`drawable/square.xml`）： ✅（已完成，已添加基础方块样式）
        *   定义方块背景色、边框等样式。
    *   **按钮样式**（`drawable/btn_common.xml`）： ✅（已完成，已添加统一按钮样式）

*   **自定义视图**
    *   `HuaRongDaoView`：继承`View`，负责绘制棋盘网格，处理方块触摸事件，方块文字自适应大小。 ✅（已完成，已实现棋盘与方块交互、绘制、文字自适应等核心功能）

#### **2. 游戏逻辑模块**

*   **数据结构设计**
    *   `model`包：包含 `Block` (方块属性)、`Level` (关卡数据)、`Direction` (移动方向)、`BlockType` (方块类型) 等类。 ✅（已完成，基础数据结构已定义）
    *   `PuzzleModel`类 (待完善：棋盘状态存储与管理)。

*   **算法实现**
    *   `BFSUtils`：实现 BFS 算法，用于搜索最短移动路径（提示/自动求解）。 ✅（已完成，已实现BFSUtils用于最短路径提示/自动求解）
    *   **随机生成合法棋局**：通过随机移动生成有解初始布局。 ✅（已完成，Level.generateRandomLevel可生成有解初始局面）

#### **3. 交互与事件处理模块**

*   **触摸事件**
    *   在`HuaRongDaoView`中处理触摸事件，识别点击方块并尝试移动。 ✅（已完成，HuaRongDaoView已实现方块点击与滑动移动）

*   **按钮事件**
    *   重置按钮：恢复初始棋局。
    *   撤销按钮：记录历史状态（使用栈结构），回退上一步操作。
    *   提示按钮：调用 BFS 算法获取下一步最优移动方向，弹窗显示提示。 ✅（已完成，主界面已集成提示按钮并弹窗显示最优解）

*   **代码执行**
    *   `CodeInterpreter`：解析并执行输入框的移动命令，支持字母标识。 ✅（已完成，已实现命令解析与执行，支持字母标识）

#### **4. 资源与工具模块**

*   **图片与字符串资源**
    *   `res/drawable`：方块图片/背景、按钮背景。
    *   `res/values/strings.xml`：定义所有界面文本。 ✅（已完成，所有界面文本已规范进strings.xml）
    *   `res/values/styles.xml`：定义按钮等控件样式。 ✅（已完成，已添加通用按钮样式）

*   **音效与动画**
    *   使用`MediaPlayer`播放移动音效（`res/raw/quickslide.wav`）。 ✅（已完成，移动方块时播放音效，动画可后续扩展）
    *   添加方块移动动画（如`ObjectAnimator`）。

*   **工具类**
    *   `ArrayUtils`：数组克隆、二维数组转一维等工具方法。

### **三、测试与优化**

1.  **功能测试**
    *   验证所有方块可正确移动，空白块边界处理无误。
    *   测试 BFS 提示功能是否返回最短路径。
    *   检查计时/步数统计是否准确。

2.  **性能优化**
    *   避免在主线程执行复杂计算（如 BFS），使用`AsyncTask`或`Coroutine`。
    *   优化界面重绘逻辑，减少`onDraw`方法中的耗时操作。

3.  **兼容性测试**
    *   在不同屏幕尺寸和 API 版本的设备上运行，确保布局自适应。

### **四、项目扩展（可选）**

1.  **难度模式**：实现简单/困难模式（不同初始棋局复杂度）。
2.  **存档功能**：使用`SharedPreferences`或数据库存储当前进度。
3.  **在线排行榜**：对接 Firebase 或本地文件，记录最佳成绩。
4.  **主题切换**：支持日间/夜间模式，更换界面配色。

### **五、代码规范与注释**

*   在关键类和方法添加 JavaDoc 注释，说明功能、参数、返回值。
*   遵循 Android 开发规范，如使用`ViewBinding`替代`findViewById`。
*   示例注释：

```java
/**
 * 华容道游戏核心模型，管理棋盘状态与移动逻辑
 */
public class PuzzleModel {
    private int[][] board; // 棋盘状态，0表示空白块
    private int blankX, blankY; // 空白块坐标

    /**
     * 尝试向指定方向移动方块
     * @param direction 移动方向（上、下、左、右，对应常量）
     * @return 是否移动成功
     */
    public boolean moveBlock(int direction) { ... }
}
```

>