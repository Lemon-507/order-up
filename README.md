# 出餐！出餐！/order up!

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-orange.svg)](https://openjfx.io/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

> 基于 JavaFX 17 开发的单人挑战小吃摊模拟经营游戏，借鉴《胡闹厨房》的核心玩法。

---

## 📖 项目简介 (Overview)

* **当前玩法**：玩家在限定时间内取材、加工、装盘并在出餐口完成订单。
* **画面表现**：基于 JavaFX/ FXML 渲染的 2D 动效与 UI。
* **游戏节奏**：逻辑层以固定 60 Hz 更新。

---

## ✨ 核心特性 (Features)

* 🥕 **食材加工与物品交互**：支持拾取、放下、切鱼、煮饭、装盘和桌面放置。
* ⏱️ **倒计时与订单规则**：游戏内同时显示 3 张订单、各自剩余时间和分数，并支持出餐提交与 3 个空盘独立回收。
* 🎮 **操作方式**：支持单人键盘控制。
* 🎵 **音频**：开始菜单支持循环背景音乐。

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
   git clone https://github.com/b1ush7/order-up.git
   cd order-up
   ```
2. **运行**
    ```bash
    # Linux / macOS
    ./mvnw clean javafx:run

    # Windows
    .\mvnw.cmd clean javafx:run
    ```

---

## 🎮 游戏操作说明 (Controls)

| 动作 | 按键 |
| :--- | :--- |
| 移动 | W A S D |
| 互动 / 拾取 / 放下 | E |
| 切鱼 | 面对切菜板持续按住 E |
| 暂停 / 继续 | Esc |

---

## 📄 开源协议 (License)
本项目基于 MIT License 协议开源。
