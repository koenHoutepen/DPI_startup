package model.Origin;

import java.io.Serializable;

/**
 *
 * This class stores all information about a destination offer
 * as a response to a client Origin request.
 */
public class TransferReply implements Serializable {

        private double capacity; // the capacity that the destination offers
        private String clinicId; // the unique quote identification

    public TransferReply() {
        super();
        this.capacity = 0;
        this.clinicId = "";
    }
    public TransferReply(double capacity, String clinicId) {
        super();
        this.capacity = capacity;
        this.clinicId = clinicId;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getQuoteID() {
        return clinicId;
    }

    public void setQuoteID(String quoteID) {
        this.clinicId = quoteID;
    }
    
    @Override
    public String toString(){
        return " capacity= "+ String.valueOf(capacity) + " quoteID= "+ clinicId;
    }
}
