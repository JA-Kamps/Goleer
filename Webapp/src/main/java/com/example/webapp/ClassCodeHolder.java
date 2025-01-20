package com.example.webapp;



public class ClassCodeHolder {
    private static ClassCodeHolder instance;
    private String classCode;

    private ClassCodeHolder() {}

    public static ClassCodeHolder getInstance() {
        if (instance == null) {
            instance = new ClassCodeHolder();
        }
        return instance;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    public void ClearCode() {
        this.classCode = null;
    }
}

