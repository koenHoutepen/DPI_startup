package model.destination;

import java.io.Serializable;

/**
 * This class stores information about the destination reply
 *  to a Origin request of the specific client
 * 
 */
public class TransferQueryReply implements Serializable {

    private double capacity; // the Origin capacity
    private String clinicId; // the unique quote Id
    
    public TransferQueryReply() {
        this.capacity = 0;
        this.clinicId = "";
    }
    
    public TransferQueryReply(double capacity, String quoteId) {
        this.capacity = capacity;
        this.clinicId = quoteId;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getQuoteId() {
        return clinicId;
    }

    public void setQuoteId(String quoteId) {
        this.clinicId = quoteId;
    }

    public String toString() {
        return "Clinic= " + this.clinicId + " capacity= " + this.capacity;
    }
}
