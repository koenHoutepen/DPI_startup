package model.Broker;

import model.destination.TransferQueryReply;

import java.util.Comparator;

public class TransferComparison implements Comparator<TransferQueryReply> {
    @Override
    public int compare(TransferQueryReply a, TransferQueryReply b) {
        if (a.getCapacity() < b.getCapacity())
            return -1; // highest value first
        if (a.getCapacity() == b.getCapacity())
            return 0;
        return 1;
    }
}
