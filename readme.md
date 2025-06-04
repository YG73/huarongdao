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

> ### **二、核心功能模块开发**
>> #### **1. 游戏界面模块**
>>> - **布局设计**  
>>>   - **主界面**（`activity_main.xml`）：  ✅（已完成，已包含棋盘、步数、按钮等核心区域）
>>>     - 游戏棋盘（使用`GridView`或自定义`ViewGroup`展示方块）。  
>>>     - 操作按钮（重置、撤销、提示、音效开关等）。  
>>>     - 计时/步数显示区域。  
>>>   - **方块样式**（`drawable/square.xml`）：  ✅（已完成，已添加基础方块样式square.xml）
>>>     - 定义选中方块与普通方块的背景色、边框等样式。  
>>> 
>>> - **自定义视图**  
>>>   - `PuzzleView`：继承`ViewGroup`，负责绘制棋盘网格，处理方块触摸事件。  ✅（已完成，已用HuaRongDaoView实现棋盘与方块交互、绘制等核心功能）
>>>   - `PuzzleBlock`：封装方块属性（位置、编号、是否可移动），提供绘制方法。  ✅（已完成，已用Block类实现方块属性与状态）
 
>> #### **2. 游戏逻辑模块**
>>> - **数据结构设计**  
>>>   - `PuzzleModel`类：  
>>>     - 存储棋盘状态（二维数组表示方块位置，`0`表示空白块）。  
>>>     - 记录空白块坐标、移动步数、游戏状态（进行中/胜利）。  
>>>     - 提供移动方块的方法（如`moveBlock(int direction)`），验证移动合法性。  
>>> 
>>> - **算法实现**  
>>>   - **BFS（广度优先搜索）求解**：  ✅（已完成，已实现BFSUtils用于最短路径提示/自动求解）
>>>     - 用于生成初始棋局或提示功能，搜索最短移动路径。  
>>>     - 参考网址中"华容道游戏是BFS算法框架中数字谜题的进阶问题"，实现状态队列与哈希表去重。  
>>>   - **随机生成合法棋局**：  ✅（已完成，Level.generateRandomLevel可生成有解初始局面）
>>>     - 通过随机移动空白块生成初始布局，确保棋局有解（可结合BFS验证）。  
>>>

>> #### **3. 交互与事件处理模块**
>>> - **触摸事件**  
>>>   - 在`PuzzleView`中重写`onTouchEvent`，识别点击方块的位置：  ✅（已完成，HuaRongDaoView已实现方块点击与滑动移动）
>>>     - 若点击可移动方块，尝试向空白块方向移动。  
>>>     - 更新`PuzzleModel`状态，并触发界面重绘。  
>>> 
>>> - **按钮事件**  
>>>   - 重置按钮：恢复初始棋局。  
>>>   - 撤销按钮：记录历史状态（使用栈结构），回退上一步操作。  
>>>   - 提示按钮：调用BFS算法获取下一步最优移动方向。  ✅（已完成，主界面已集成提示按钮并弹窗显示最优解）
>>>

>> #### **4. 资源与工具模块**
>>> - **图片与字符串资源**  
>>>   - 在`res/drawable`中准备方块图片（或使用纯色背景）。  
>>>   - 在`res/values/strings.xml`中定义文本（如"重置"、"步数："）。  ✅（已完成，所有界面文本已规范进strings.xml）
>>> 
>>> - **音效与动画**  
>>>   - 使用`MediaPlayer`播放移动音效（`res/raw/quickslide.wav`）。  ✅（已完成，移动方块时播放音效，动画可后续扩展）
>>>   - 添加方块移动动画（如`ObjectAnimator`
