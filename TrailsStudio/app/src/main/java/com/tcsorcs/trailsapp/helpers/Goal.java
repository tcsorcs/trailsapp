package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Mike on 8/10/2015.
 */
public class Goal {
    private int id;
    private String name;
    private boolean singleSession;
    private float timeLimit;
    private ArrayList<Task> childTasks = new ArrayList();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSingleSession() {
        return singleSession;
    }

    public float getTimeLimit() {
        return timeLimit;
    }

    public ArrayList<Task> getChildTasks() {
        return childTasks;
    }

    public Goal(String name, boolean singleSession, float timeLimit) {
        this.name = name;
        this.singleSession = singleSession;
        this.timeLimit = timeLimit;
    }

    public Goal(Goal toCopy) {
        this.name = "Copy of " + toCopy.getName();
        this.singleSession = toCopy.getSingleSession();
        this.timeLimit = toCopy.timeLimit();
        copyChildTasks(toCopy.getChildTasks());
    }

    private void copyChildTasks(ArrayList<Task> toCopy){
        Iterator<Task> i = toCopy.iterator();
        while(i.hasNext()){
            this.childTasks.add(new Task(i.next()));
        }
    }

    public void AddTask(Task toAdd){
        childTasks.add(toAdd);
    }

}
