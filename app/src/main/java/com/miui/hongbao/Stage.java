package com.miui.hongbao;

/**
 * Created by ZhongyiTong on 9/30/15.
 * <p/>
 * 记录抢红包时对应的阶段
 */
public class Stage {
    /**
     * 单例设计
     */
    private static Stage stageInstance;

    /**
     * 阶段常量
     */
    public static final int FETCHING_STAGE = 0, OPENING_STAGE = 1, FETCHED_STAGE = 2, OPENED_STAGE = 3;

    /**
     * 当前阶段
     */
    private int currentStage = FETCHED_STAGE;

    /**
     * 阶段互斥，不允许多次回调进入同一阶段
     */
    public boolean mutex = false;

    /**
     * 单例设计，防止通过构造函数创建对象
     */
    private Stage() {
    }

    /**
     * 单例设计，惰性实例化
     *
     * @return 返回唯一的实例
     */
    public static Stage getInstance() {
        if (stageInstance == null) stageInstance = new Stage();
        return stageInstance;
    }

    /**
     * 记录接下来的阶段
     *
     * @param _stage
     */
    public void entering(int _stage) {
        stageInstance.currentStage = _stage;
        mutex = false;
    }

    /**
     * 记录当前的阶段
     */
    public int getCurrentStage() {
        return stageInstance.currentStage;
    }
}
