package edu.ucalgary.oop;

import java.time.LocalDateTime;

public class Water extends Supply {
    private boolean allocatedToVictim;
    private boolean isExpired = false;

    public Water(int supplyId, boolean allocatedToVictim) {
        super(supplyId);
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

    public void checkExpiration(LocalDateTime currentTime) {}
}

