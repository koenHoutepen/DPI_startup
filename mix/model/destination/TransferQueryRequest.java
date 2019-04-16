package model.destination;

import java.io.Serializable;

/**
 *
 * This class stores all information about an request from a destination to offer
 * a Origin to a specific client.
 */
public class TransferQueryRequest implements Serializable {

    private int amount; // the requested Origin amount
    private String origin; // the requested Origin period

    public TransferQueryRequest() {
        super();
        this.amount = 0;
        this.origin = "";
    }

    public TransferQueryRequest(int amount, String origin) {
        super();
        this.amount = amount;
        this.origin = origin;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " origin = " + origin;
    }
}
