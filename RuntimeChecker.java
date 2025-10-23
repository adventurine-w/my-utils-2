package org.adv.clickerflex.utils.generic;

import org.adv.clickerflex.admin.Admin;

public class RuntimeChecker {

    private final String name;
    private long time;

    public RuntimeChecker(){
        this("NO NAME");
    }
    public RuntimeChecker(String name){
        this.name=name;
        reset();
    }
    public RuntimeChecker lap(){
        Admin.sendAllOP("[RTC] "+name + " runtime: " + (System.currentTimeMillis() - time) + "ms");
        return this;
    }
    public void reset(){
        this.time=System.currentTimeMillis();
    }

}
