package com.xn.interfacetest.Enum;


public enum ExecuteStatusEnum {
    EXECUTED("已执行", 0),EXECUTING("执行中", 1),UN_EXECUTE("未执行", 2),QUIT("取消",3),WAITING("等待中",4),FAILED("执行失败",5);
    // 成员变量
    private String name;
    private int id;

    // 构造方法
    private ExecuteStatusEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // 普通方法
    public static String getName(int id) {
        for (ExecuteStatusEnum c : ExecuteStatusEnum.values()) {
            if (c.getId() == id) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id; 
    }

    public void setId(int id) {
        this.id = id;
    }
}
