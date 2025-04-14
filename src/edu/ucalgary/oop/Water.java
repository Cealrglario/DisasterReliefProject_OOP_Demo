package edu.ucalgary.oop;

import java.time.LocalDateTime;

public class Water extends Supply {
    private boolean allocatedToVictim;
    private boolean isExpired = false;

    public Water(int supplyId, boolean allocatedToVictim) {
        super(supplyId, "Water");
        this.allocatedToVictim = allocatedToVictim;
    }

    public boolean getAllocatedToVictim() {
        return this.allocatedToVictim;
    }

    public void setAllocatedToVictim(boolean toVictim) {}

    public boolean getIsExpired() {
        return this.isExpired;
    }

    public void setIsExpired(boolean isExpired) {}

    public void checkExpiration(LocalDateTime timeAllocated) {}

    @Override
    public void displayDetails() {
        if (!isExpired) {
            System.out.println("Good for drinking, not yet expired.");
        } else {
            System.out.println("Expired.");
        }

        if (!super.getComments().isEmpty()) {
            System.out.println("Comments: " + super.getComments());
        }
    }
}

