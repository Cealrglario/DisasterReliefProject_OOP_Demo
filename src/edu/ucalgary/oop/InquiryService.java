package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public enum InquiryService {
    INSTANCE;

    private final InquiryAccess<Object> inquiryAccess = new InquiryAccess<>();

    public Inquiry getInquiryById(int inquiryId) throws SQLException {
        return inquiryAccess.getById(inquiryId);
    }

    public List<Inquiry> getAllInquiries() throws SQLException {
        return inquiryAccess.getAll();
    }

    public Inquiry addInquiry(int inquirerId, int missingPersonId, LocalDate dateOfInquiry, String infoProvided) throws SQLException {
        return inquiryAccess.addInquiry(inquirerId, missingPersonId, dateOfInquiry, infoProvided);
    }

    public boolean updateInquiryInfo(Inquiry inquiry, String newInfo) throws SQLException {
        inquiry.setInfoProvided(newInfo);
        return inquiryAccess.updateInfo("comments", newInfo, inquiry.getInquiryId());
    }

    public boolean updateLastKnownLocation(Inquiry inquiry, int locationId) throws SQLException {
        inquiry.setLastKnownLocationId(locationId);
        return inquiryAccess.updateInfo("location_id", locationId, inquiry.getInquiryId());
    }

    public boolean removeInquiry(Inquiry inquiry) throws SQLException {
        return inquiryAccess.removeInquiry(inquiry);
    }
}
