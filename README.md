# 出餐！出餐！/order up!

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-orange.svg)](https://openjfx.io/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

> 基于 JavaFX 17 开发的单人挑战小吃摊模拟经营游戏，借鉴《胡闹厨房》核心玩法）。

---

## 📖 项目简介 (Overview)

* **玩法核心**：玩家需要在限定时间内根据顾客订单，完成切菜、烹饪、装盘、出餐及洗碗等流程。
* **画面表现**：基于 JavaFX/ FXML 渲染的 2D 动效与 UI。
* **关卡机制**：支持两种不同小吃摊位地图与递增难度设计。

---

## ✨ 核心特性 (Features)

* 🍳 **丰富食材与烹饪链**：支持切菜，煮饭等多步骤加工。
* ⏱️ **订单与倒计时系统**：支持超时扣分、连击奖励及小费计算。
* 🎮 **多种操作方式**：支持单人键盘控制。
* 🎵 **音效与视效**：包含小吃滋滋声、报警声、出餐成功提示音。

---

## 🛠️ 技术栈 (Tech Stack)

| 模块 | 技术选型 | 说明 |
| :--- | :--- | :--- |
| **语言** | Java 17 | 使用现代 Java 语法标准 |
| **图形/UI** | JavaFX 17 + FXML | 界面展示与动画渲染 |
| **构建工具** | Maven | 依赖与构建管理 |
| **测试框架** | JUnit 5 | 核心逻辑单元测试 |

---

## 🚀 快速开始 (Getting Started)

### 环境准备

* **JDK**：17 或更高版本
* **Maven**：3.8.x 或更高版本

### 安装与运行

1. **克隆仓库**
   ```bash
   git clone [https://github.com/b1ush7/order-up.git](https://github.com/b1ush7/order-up.git)
   cd order-up
2. **运行**
    ```bash
    # Linux / macOS
    ./mvnw clean javafx:run

    # Windows
    .\mvnw.cmd clean javafx:run

---

## 🎮 游戏操作说明 (Controls)
* 动作	玩家 1 (Player 1)
* 移动	W A S D	
* 互动 / 抓取	J
* 切菜 / 烹饪	K
* 冲刺	LShift

---

## 📄 开源协议 (License)
本项目基于 MIT License 协议开源。