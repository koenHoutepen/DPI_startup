package transferbroker;

import model.Origin.*;
import model.destination.*;

/**
 * This class represents one line in the JList in Loan Broker.
 * This class stores all objects that belong to one TransferRequest:
 *    - TransferRequest,
 *    - TransferQueryRequest, and
 *    - TransferQueryReply.
 *  Use objects of this class to add them to the JList.
 *    
 * @author 884294
 *
 */
class JListLine {
	
	private TransferRequest transferRequest;
	private TransferQueryRequest bankRequest;
	private TransferQueryReply bankReply;

	public JListLine(TransferRequest transferRequest) {
		this.setTransferRequest(transferRequest);
	}

	public TransferRequest getTransferRequest() {
		return transferRequest;
	}

	public void setTransferRequest(TransferRequest transferRequest) {
		this.transferRequest = transferRequest;
	}

	public TransferQueryRequest getBankRequest() {
		return bankRequest;
	}

	public void setBankRequest(TransferQueryRequest bankRequest) {
		this.bankRequest = bankRequest;
	}

	public TransferQueryReply getBankReply() {
		return bankReply;
	}

	public void setBankReply(TransferQueryReply bankReply) {
		this.bankReply = bankReply;
	}

	@Override
	public String toString() {
		return transferRequest.toString() + " || " + ((bankReply != null) ? bankReply.toString() : "waiting for reply...");
	}

}
