package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Stack;

public class Inquiry {
    private final int INQUIRY_ID;
    private final int INQUIRER_ID;
    private final int MISSING_PERSON_ID;
    private final LocalDate INQUIRY_DATE;
    private String infoProvided;
    private int lastKnownLocationId;

    public Inquiry(int inquiryId, int inquirerId, int missingPersonId, LocalDate dateOfInquiry, String infoProvided) {
        this.INQUIRY_ID = inquiryId;
        this.INQUIRER_ID = inquirerId;
        this.MISSING_PERSON_ID = missingPersonId;
        this.INQUIRY_DATE = LocalDate.now();
        this.infoProvided = infoProvided;
    }

    public int getInquiryId() {
        return this.INQUIRY_ID;
    }

    public int getInquirerid() {
        return this.INQUIRER_ID;
    }

    public int getMissingPersonId() {
        return this.MISSING_PERSON_ID;
    }

    public LocalDate getDateOfInquiry() {
        return this.INQUIRY_DATE;
    }

    public String getInfoProvided() {
        return this.infoProvided;
    }

    public void setInfoProvided(String infoProvided) {
        this.infoProvided = infoProvided;
    }

    public int getLastKnownLocationId() {
        return this.lastKnownLocationId;
    }

    public void setLastKnownLocationId(int locationId) {
        this.lastKnownLocationId = locationId;
    }
}

