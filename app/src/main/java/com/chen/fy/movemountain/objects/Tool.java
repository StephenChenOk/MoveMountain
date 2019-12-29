package com.chen.fy.movemountain.objects;


public class Tool {

    private String name;

    public Tool(String name) {
        this.name = name;
    }

    /**
     * 获取此工具一次可以移动的权值
     */
    public int getWeight() {
        switch (name) {
            case "手工":
                return 4;
            case "铲子":
                return 16;
            case "运泥车":
                return 64;
            case "挖掘机":
                return 256;
            default:
                return 0;
        }
    }
}
