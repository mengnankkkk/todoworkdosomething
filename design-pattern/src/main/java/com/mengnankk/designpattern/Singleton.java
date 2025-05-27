package com.mengnankk.designpattern;

public class Singleton {
    private static volatile Singleton instance;

    private Singleton(){};

    public static Singleton getInstance(){
        if (instance==null){
            synchronized (Singleton.class){
                if (instance==null){
                    instance = new Singleton();//防止重排
                }
            }
        }
        return instance;
    }
}
