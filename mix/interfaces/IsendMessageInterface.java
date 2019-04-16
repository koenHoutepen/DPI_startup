package interfaces;

import messaging.requestreply.RequestReply;
import model.destination.TransferQueryReply;
import model.destination.TransferQueryRequest;
import model.Origin.TransferRequest;

public interface IsendMessageInterface {
    void messageSomeOne(RequestReply reply, String correlation, String Queue);
    void messageSomeOne(TransferRequest request, String Queue);
    void messageSomeOne(TransferQueryRequest request, String correlation, String Queue);
    void messageSomeOne(TransferQueryReply request, String correlation, String Queue);
}
