package transferbroker;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controllers.receiveMessageController;
import controllers.sendMessageController;
import interfaces.IsendMessageInterface;
import messaging.requestreply.RequestReply;
import model.Broker.BrokerObject;
import model.destination.*;
import model.Origin.TransferReply;
import model.Origin.TransferRequest;


public class TransferCenter extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
	private JList<JListLine> list;
	private List<RequestReply<TransferRequest, String>> waitingForReply;
	private IsendMessageInterface sendMessageController;
	private receiveMessageController receivemessageReply;
	private receiveMessageController receivemessagerequest;
	private List<BrokerObject> registerReturns;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
					TransferCenter frame = new TransferCenter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public TransferCenter() {
		//set up receivemessagecontrollers
		receivemessageReply = new receiveMessageController("ReplyToCenter");
		receivemessagerequest = new receiveMessageController("MessageFromClinicClient");
		receivemessageReply.addObserver(this::update);
		receivemessagerequest.addObserver(this::update);
		sendMessageController = new sendMessageController();
		registerReturns = new ArrayList<>();
        waitingForReply = new ArrayList<>();
		setTitle("Transfer Center");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
		gbl_contentPane.rowHeights = new int[]{233, 23, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		list = new JList<JListLine>(listModel);
		scrollPane.setViewportView(list);		
	}
	
	 private JListLine getRequestReply(TransferRequest request){
	     
	     for (int i = 0; i < listModel.getSize(); i++){
	    	 JListLine rr =listModel.get(i);
	    	 if (rr.getTransferRequest() == request){
	    		 return rr;
	    	 }
	     }
	     
	     return null;
	   }
	
	public void add(TransferRequest transferRequest){
		listModel.addElement(new JListLine(transferRequest));
	}
	

	public void add(TransferRequest transferRequest, TransferQueryRequest bankRequest){
		JListLine rr = getRequestReply(transferRequest);
		if (rr!= null && bankRequest != null){
			rr.setBankRequest(bankRequest);
            list.repaint();
		}		
	}
	
	public void add(TransferRequest transferRequest, TransferQueryReply bankReply){
		JListLine rr = getRequestReply(transferRequest);
		if (rr!= null && bankReply != null){
			rr.setBankReply(bankReply);
            list.repaint();
		}		
	}

	private void addbrokerobject(String correlation, TransferRequest request) {
		TransferQueryRequest newrequest = new TransferQueryRequest(request.getAmount(), request.getOriginClientName());
		int count = 0;
		if (request.getAmount() <= 100000) // to Trenton
		{
			count++;
			sendMessageController.messageSomeOne(newrequest, correlation, "toTrenton");
		}
		if ((200000 < request.getAmount()) && (request.getAmount() < 300000) ) //to Bedlam
		{
			count++;
			sendMessageController.messageSomeOne(newrequest, correlation, "toBedlam");
		}
		if (request.getAmount() <= 250000 ) // to Topeka
		{
			count++;
			sendMessageController.messageSomeOne(newrequest, correlation, "toTopeka");
		}

		if (count != 0) {
			System.out.println("this is the count" + count);
			add(request);
			registerReturns.add(new BrokerObject(count, correlation));
			waitingForReply.add(new RequestReply<>(request, correlation));
		} else {
			sendMessageController.messageSomeOne(new RequestReply<>(request, new TransferReply(0, "Error, no Asylums meet criteria")), correlation, "ReplyToClient");
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		try {
			if (((ObjectMessage) arg).getObject() instanceof TransferQueryReply) {
				String correlation = ((ObjectMessage)arg).getJMSCorrelationID();
				System.out.println("Received message");

				for(BrokerObject object : registerReturns) {
					if(object.getCorrelation().equals(correlation)) {
					    System.out.println("kom je hier?");
						if(object.add((TransferQueryReply) ((ObjectMessage)arg).getObject()) == true){
							finalMessage(object.getReply(), correlation);
							break;
						}
						else{
							break;
						}

					}
				}


			} else if (((ObjectMessage) arg).getObject() instanceof TransferRequest) { // reply
				TransferRequest loanrequest = (TransferRequest)((ObjectMessage)arg).getObject();
				String messageid = ((ObjectMessage)arg).getJMSMessageID();
				addbrokerobject(messageid, loanrequest);
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void finalMessage(TransferQueryReply reply, String correlation){
		for (int i = 0; i < waitingForReply.size(); i++) {
			System.out.println("Checking message: " + i);
			System.out.println("Is this ok? " + waitingForReply.get(i).getReply() + " | " + correlation);
			if(waitingForReply.get(i).getReply().equals(correlation)){
				System.out.println("++ Adding message: " + i);
				add(waitingForReply.get(i).getRequest(),reply);
				TransferReply transferReply = new TransferReply(reply.getCapacity(), reply.getQuoteId());
				System.out.println("Sending:" + waitingForReply.get(i).getRequest() + "AND" + transferReply + "AND" + correlation);
				sendMessageController.messageSomeOne(new RequestReply<>(waitingForReply.get(i).getRequest(), transferReply), correlation, "ReplyToClient");

				break;
			}
		}
	}
}
