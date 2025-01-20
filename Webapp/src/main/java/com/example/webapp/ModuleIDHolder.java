package com.example.webapp;

public class ModuleIDHolder {
    private static ModuleIDHolder instance;
    private int moduleID;

    private ModuleIDHolder() {}

    public static ModuleIDHolder getInstance() {
        if (instance == null) {
            instance = new ModuleIDHolder();
        }
        return instance;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }
    public void clearModuleID() {
        this.moduleID = -1;
    }
}
