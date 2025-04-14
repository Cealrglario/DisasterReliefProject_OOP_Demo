package edu.ucalgary.oop;

import java.time.LocalDate;

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

    public void setAllocatedToVictim(boolean toVictim) {
        this.allocatedToVictim = toVictim;
    }

    public boolean getIsExpired() {
        return this.isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public void checkExpiration(LocalDate dateAllocated) {
        LocalDate now = LocalDate.now();
        this.isExpired = !now.isBefore(dateAllocated.plusDays(1));
    }

    @Override
    public void displayDetails() {
        if (!isExpired) {
            System.out.println(languageManager.getTranslation("water_not_expired"));
        } else {
            System.out.println(languageManager.getTranslation("water_expired"));
        }

        if (super.getComments() != null) {
            System.out.println("Comments: " + super.getComments());
        }
    }
}

