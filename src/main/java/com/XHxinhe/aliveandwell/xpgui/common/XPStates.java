//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

// 定义包名，属于模组的通用功能包
package com.XHxinhe.aliveandwell.xpgui.common;

// 导入Minecraft的玩家实体类
import net.minecraft.entity.player.PlayerEntity;
// 导入NBT复合标签类，用于数据存储
import net.minecraft.nbt.NbtCompound;

// 定义经验值状态管理类
public class XPStates {
    // 存储关联的玩家实体，使用final确保不可变更
    public final PlayerEntity playerEntity;
    // 私有变量，设置最大经验值上限为3000
    private int MAX_XP = 3000;
    // 公共变量，存储当前经验值
    public int xp;
    // 布尔变量，可能用于标记某种状态（代码中未使用）
    public boolean plus10;

    // 构造函数，接收玩家实体作为参数
    public XPStates(PlayerEntity playerEntity) {
        // 将传入的玩家实体赋值给成员变量
        this.playerEntity = playerEntity;
    }

    // 获取玩家实体的公共方法
    public PlayerEntity getPlayerEntity() {
        // 返回存储的玩家实体
        return this.playerEntity;
    }

    // 从NBT标签中读取数据的方法
    public void readNbt(NbtCompound tag) {
        // 从NBT标签中读取"XPBox"键对应的整数值，赋给xp变量
        this.xp = tag.getInt("XPBox");
        // 检查NBT标签中是否包含"MaxXP"键
        if (tag.contains("MaxXP")) {
            // 如果存在，读取"MaxXP"键对应的整数值，更新最大经验值
            this.MAX_XP = tag.getInt("MaxXP");
        }
    }

    // 将数据写入NBT标签的方法
    public void writeNbt(NbtCompound tag) {
        // 将当前经验值以"XPBox"为键写入NBT标签
        tag.putInt("XPBox", this.xp);
        // 将最大经验值以"MaxXP"为键写入NBT标签
        tag.putInt("MaxXP", this.MAX_XP);
    }

    // 获取当前经验值的公共方法
    public int getXp() {
        // 返回当前经验值
        return this.xp;
    }

    // 设置经验值的方法
    public void setXpBox(int b) {
        // 将传入的值赋给经验值变量
        this.xp = b;
    }

    // 添加经验值到经验盒的方法
    public void addXPBox(int add) {
        // 打印操作前玩家的总经验值
        System.out.println("Player experience before: " + this.playerEntity.totalExperience);
        // 检查添加后是否超过最大经验值限制
        if (this.xp + add <= this.MAX_XP) {
            // 检查玩家当前总经验是否足够扣除
            if (this.playerEntity.totalExperience >= add) {
                // 从玩家总经验中扣除相应数量（负数表示扣除）
                this.playerEntity.addExperience(-add);
                // 将扣除的经验添加到经验盒中
                this.xp += add;
            }
        } else { // 如果添加后会超过最大值
            // 计算达到最大值所需的经验数量
            int needed = this.MAX_XP - this.xp;
            // 检查玩家经验是否足够达到最大值
            if (this.playerEntity.totalExperience >= needed) {
                // 扣除达到最大值所需的经验
                this.playerEntity.addExperience(-needed);
                // 将经验盒设置为最大值
                this.xp = this.MAX_XP;
            }
        }
        // 打印操作后玩家的总经验值
        System.out.println("Player experience after: " + this.playerEntity.totalExperience);
    }

    // 检查是否可以进行经验值操作的方法
    public boolean canPlus(int plus) {
        // 如果操作值小于等于0
        if (plus <= 0) {
            // 如果是负数（取出经验）
            if (plus < 0) {
                // 检查经验盒中的经验是否足够取出
                return this.xp >= -plus;
            } else { // 如果是0
                // 返回false，0值操作无意义
                return false;
            }
        } else { // 如果是正数（存入经验）
            // 检查玩家经验是否足够且经验盒是否有空间
            return this.playerEntity.totalExperience >= plus && this.xp <= this.MAX_XP - plus;
        }
    }

    // 检查是否可以将所有玩家经验转入经验盒
    public boolean canPlusTotal() {
        // 检查玩家有经验且经验盒有足够空间容纳所有经验
        return this.playerEntity.totalExperience > 0 && this.GetMaxXp() - this.xp >= this.playerEntity.totalExperience;
    }

    // 获取最大经验值的方法
    public int GetMaxXp() {
        // 返回最大经验值
        return this.MAX_XP;
    }

    // 设置最大经验值的方法
    public void SetMaxXp(int maxXP) {
        // 更新最大经验值
        this.MAX_XP = maxXP;
    }

    // 静态方法，根据玩家获取对应的XPStates实例
    public static XPStates getForPlayer(PlayerEntity player) {
        // 将玩家强制转换为PlayerStatsManagerAccess接口，并获取玩家状态管理器
        return ((PlayerStatsManagerAccess)player).getPlayerStatsManager();
    }
}