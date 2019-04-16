package destinationclinics;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.jms.*;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controllers.receiveMessageController;
import controllers.sendMessageController;
import interfaces.IsendMessageInterface;
import model.destination.*;
import messaging.requestreply.RequestReply;

public class Bedlam extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private IsendMessageInterface messageInterface;
	private JPanel contentPane;
	private JTextField tfReply;
	private DefaultListModel<RequestReply<TransferQueryRequest, TransferQueryReply>> listModel = new DefaultListModel<RequestReply<TransferQueryRequest, TransferQueryReply>>();
	private List<RequestReply<TransferQueryRequest, String>> waitingForReply;
	private receiveMessageController receiveController;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
					Bedlam frame = new Bedlam();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Bedlam() {
        messageInterface = new sendMessageController();
        receiveController = new receiveMessageController("toBedlam");
		receiveController.addObserver(this::update);
		setTitle("Bedlam Asylum");

		waitingForReply = new ArrayList<>();
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
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);

		JList<RequestReply<TransferQueryRequest, TransferQueryReply>> list = new JList<RequestReply<TransferQueryRequest, TransferQueryReply>>(listModel);
		scrollPane.setViewportView(list);

		JLabel lblNewLabel = new JLabel("type reply");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		tfReply = new JTextField();
		GridBagConstraints gbc_tfReply = new GridBagConstraints();
		gbc_tfReply.gridwidth = 2;
		gbc_tfReply.insets = new Insets(0, 0, 0, 5);
		gbc_tfReply.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfReply.gridx = 1;
		gbc_tfReply.gridy = 1;
		contentPane.add(tfReply, gbc_tfReply);
		tfReply.setColumns(10);

		JButton btnSendReply = new JButton("send reply");
		btnSendReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RequestReply<TransferQueryRequest, TransferQueryReply> rr = list.getSelectedValue();
				int capacity = Integer.parseInt((tfReply.getText()));
				TransferQueryReply reply = new TransferQueryReply(capacity,"Bedlam");
				if (rr!= null && reply != null){
					rr.setReply(reply);
					list.repaint();

					for (int i = 0; i < waitingForReply.size(); i++) {
						System.out.println("Checking:" + i);
						System.out.println(waitingForReply.get(i).getRequest().getAmount() + " | " + rr.getRequest().getAmount());
						if(waitingForReply.get(i).getRequest() == rr.getRequest()){
							messageInterface.messageSomeOne(rr.getReply(), waitingForReply.get(i).getReply(), "ReplyToCenter");
							break;
						}
					}
				}
			}
		});
		GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
		gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSendReply.gridx = 4;
		gbc_btnSendReply.gridy = 1;
		contentPane.add(btnSendReply, gbc_btnSendReply);

	}

	@Override
	public void update(Observable o, Object msg) {
	    System.out.println("yoyo tijd");
		TransferQueryRequest transferQueryRequest;
		try {
			transferQueryRequest = (TransferQueryRequest) ((ObjectMessage) msg).getObject();
			String correlation = ((ObjectMessage) msg).getJMSCorrelationID();
			waitingForReply.add(new RequestReply<>(transferQueryRequest, correlation));
			listModel.addElement(new RequestReply<>(transferQueryRequest, new TransferQueryReply()));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
