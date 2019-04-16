package model.Origin;

import java.io.Serializable;

/**
 *
 * This class stores all information about a
 * request that a client submits to get a Origin.
 *
 */
public class TransferRequest implements Serializable {

    private String originClientName; // asylumname.
    private int amount; // the ammount to borrow
    private String origin; // the original sender of a transferRequest

    public TransferRequest() {
        super();
        this.originClientName = "Arkham";
        this.amount = 0;
        this.origin = "";
    }

    public TransferRequest(String originClientName, int amount, String origin) {
        super();
        this.originClientName = originClientName;
        this.amount = amount;
        this.origin = origin;
    }

    public String getOriginClientName() {
        return originClientName;
    }

    public void setOriginClientName(String originClientName) {
        this.originClientName = originClientName;
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
        return "originClientName= " + originClientName + " amount= " + String.valueOf(amount) + " original =" + origin;
    }
}
