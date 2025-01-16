package com.example.webapp;

public class StudentHolder {
    private static StudentHolder instance;
    private int studentId;

    private StudentHolder() {}

    public static StudentHolder getInstance() {
        if (instance == null) {
            instance = new StudentHolder();
        }
        return instance;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public void ClearID() {
        this.studentId = 0;
    }
}
