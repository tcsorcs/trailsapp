package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Mike on 8/10/2015.
 */
public class Task {

    private TaskType taskType;
    private float currAmount;
    private float totalAmount;
    private boolean complete;

    // The types of tasks that can be tracked by Task objects.
    public static enum TaskType{
        DISTANCE, PACE, TIME;
    }

    // Tasks should be immutable once created, so no setters are provided.
    // Only access point is addAmount().
    public TaskType getTaskType() {
        return taskType;
    }

    public float getCurrAmount() {
        return currAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public boolean isComplete() {
        return complete;
    }

    /// Standard constructor
    /// Args:
    ///  o taskType:TaskType - The type of Task this object will hold
    ///  o totalAmount:float - The totalAmount of distance/pace/time for 100% completion
    public Task(TaskType taskType, float totalAmount) {
        this.taskType = taskType;
        this.currAmount = 0.0f;
        this.totalAmount = totalAmount;
        this.complete = false;
    }

    /// Copy constructor
    /// Args:
    ///  o toCopy:Task - Task object to copy info from
    public Task(Task toCopy){
        this.taskType = toCopy.getTaskType();
        this.currAmount = 0.0f;
        this.totalAmount = toCopy.getTotalAmount();
        this.complete = false;
    }

    /// Add this heartbeat's amount to the current amount, check if task is complete
    /// Args:
    ///  o amt:float - Amount to add to the current amount
    /// Return:
    ///  o Whether this task is now complete
    public boolean addAmount(float amt){
        currAmount += amt;
        if(currAmount >= totalAmount)
            complete=true;

        return complete;
    }
}
