package edu.ucalgary.oop;

import java.util.Stack;

public abstract class Supply {
    private final int SUPPLY_ID;
    private Stack<String> comments;

    public Supply(int supplyId) {
        this.SUPPLY_ID = supplyId;
    }

    public int getSupplyId() {
        return this.SUPPLY_ID;
    }

    public Stack<String> getComments() {
        return this.comments;
    }

    public void setComments(Stack<String> comments) {
        this.comments = comments;
    }

    public void addComment(String comment) {
        this.comments.push(comment);
    }

    public void undoAddComment() {
        this.comments.pop();
    }
}
