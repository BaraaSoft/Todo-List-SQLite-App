package com.baraa.bsoft.taskstimer;

import java.io.Serializable;

/**
 * Created by baraa on 14/07/2017.
 */

public class Task implements Serializable {
    public static final long serialVersionUID = 20170714L;
    private int id;
    private final String name;
    private final String Description;
    private final int sortOrder;

    public Task(){
        this(-1,null,null,-1);
    }
    public Task(int id, String name, String description, int sortOrder) {
        this.id = id;
        this.name = name;
        Description = description;
        this.sortOrder = sortOrder;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setId(int id) {
        this.id = id;
    }
}
