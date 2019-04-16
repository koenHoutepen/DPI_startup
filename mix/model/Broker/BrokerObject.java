package model.Broker;

import model.destination.TransferQueryReply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrokerObject {
    private int numberOut;
    private List<TransferQueryReply> replyList;
    private String Correlation;

    public BrokerObject(int nummer, String cor){
        replyList = new ArrayList<>();
        numberOut = nummer;
        Correlation = cor;

    }

    public boolean add(TransferQueryReply reply){
        replyList.add(reply);
        System.out.println("Expected replies: " + numberOut);
        System.out.println("Current replies: " + replyList.size());
        if(replyList.size() == numberOut){
            System.out.println("Returning true, sending to client after");
            return true;
        }
        else{
            System.out.println("Returning False, try again");
            return false;
        }
    }
    public String getCorrelation() {
        return Correlation;
    }
    public TransferQueryReply getReply(){
        TransferQueryReply finalReply = Collections.max(replyList, new TransferComparison());
        return finalReply;
    }
}
