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
    private int currTaskID;

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
        this.currTaskID = 0;
    }


    /* Copy constructor
    * This constructor and the subsequent method are used to make
    * this Goal object a copy of another Goal object.
    */
    public Goal(Goal toCopy) {
        this.name = "Copy of " + toCopy.getName();
        this.singleSession = toCopy.getSingleSession();
        this.timeLimit = toCopy.timeLimit();
        copyChildTasks(toCopy.getChildTasks());
    }

    private void copyChildTasks(ArrayList<Task> toCopy){
        Iterator<Task> i = toCopy.iterator();
        while(i.hasNext()){
            this.childTasks.add(new Task(i.next(),++currTaskID));
        }
    }

    public void AddTask(Task toAdd){
        childTasks.add(toAdd);
    }
    public void AddTask(Task.TaskType taskType, float totalAmount){
        childTasks.add(new Task(taskType, totalAmount, ++currTaskID));
    }

    public void UpdateTasks(int amt){
        Iterator<Task> i = childTasks.iterator();
        while(i.hasNext()){
            Task childTask = i.next();
            if(!childTask.isComplete) {
                boolean taskComplete = childTask.addAmount(amt);
                if (taskComplete) {
                    //TODO: display completion message
                }
            }
        }
    }

    public void DeleteTask(int taskID){
        Iterator<Task> i = childTasks.iterator();
        while(i.hasNext()) {
            Task childTask = i.next();
            if(childTask.GetTaskID() == taskID){
                childTasks.delete(childTask);
                break;
            }
        }
    }
    /// Faster, but requires the full Task object
    public void DeleteTask(Task toDelete){
        childTasks.delete(toDelete);
    }

}
