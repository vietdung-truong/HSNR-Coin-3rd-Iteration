package GUI;


import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.*;


import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.security.Security;
import java.awt.event.ActionEvent;

public class Send extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldValue;
	private JTextField textFieldmessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Send frame = new Send();
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
	public Send() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Public Adress vom Empf\u00E4nger");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		JTextArea textAreaRecipient = new JTextArea();
		GridBagConstraints gbc_textAreaRecipient = new GridBagConstraints();
		gbc_textAreaRecipient.gridheight = 2;
		gbc_textAreaRecipient.gridwidth = 2;
		gbc_textAreaRecipient.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaRecipient.fill = GridBagConstraints.BOTH;
		gbc_textAreaRecipient.gridx = 3;
		gbc_textAreaRecipient.gridy = 1;
		contentPane.add(textAreaRecipient, gbc_textAreaRecipient);

		JLabel lblNewLabel_1 = new JLabel("\u00DCberweisungsbetrag:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		textFieldValue = new JTextField();
		GridBagConstraints gbc_textFieldValue = new GridBagConstraints();
		gbc_textFieldValue.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldValue.gridx = 3;
		gbc_textFieldValue.gridy = 3;
		contentPane.add(textFieldValue, gbc_textFieldValue);
		textFieldValue.setColumns(10);

		JLabel lblVerwendungszweck = new JLabel("Verwendungszweck:");
		GridBagConstraints gbc_lblVerwendungszweck = new GridBagConstraints();
		gbc_lblVerwendungszweck.insets = new Insets(0, 0, 5, 5);
		gbc_lblVerwendungszweck.gridx = 1;
		gbc_lblVerwendungszweck.gridy = 5;
		contentPane.add(lblVerwendungszweck, gbc_lblVerwendungszweck);

		textFieldmessage = new JTextField();
		GridBagConstraints gbc_textFieldmessage = new GridBagConstraints();
		gbc_textFieldmessage.gridwidth = 2;
		gbc_textFieldmessage.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldmessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldmessage.gridx = 3;
		gbc_textFieldmessage.gridy = 5;
		contentPane.add(textFieldmessage, gbc_textFieldmessage);
		textFieldmessage.setColumns(10);

		JButton buttonSenden = new JButton("Senden");
		buttonSenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Transaction tempTransaction = null;
				try {
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
					String recipientString = textAreaRecipient.getText();
					Wallet tempWallet = new Wallet();

					float value = Float.valueOf(textFieldValue.getText());

					tempWallet.setPublicKey(StringUtil.stringToPublicKey(recipientString));

					
					Block tempBlock = new Block(Blockchain.blockchain.get(Blockchain.blockchain.size()-1).hash);
					tempBlock.addTransaction(MainStartScreen.walletA.sendFunds(tempWallet.publicKey, value));
					Blockchain.addBlock(tempBlock);
					Blockchain.isChainValid();
					
				} catch (Exception e1) {
					Component frame = null;
					JOptionPane.showMessageDialog(frame, "Überprüfen Sie bitte die Eingaben", "Falsche Formatierung",
							JOptionPane.ERROR_MESSAGE);
				}
				

				//Blockchain.blocklessPool.add(tempTransaction);
				Component frame = null;
				JOptionPane.showMessageDialog(frame,
					    "Transaktion wurde erfolgreich durchgeführt, gemint und ins Blockchain hinzugefügt",
					    "Erfolg!",
					    JOptionPane.PLAIN_MESSAGE);

			}
		});
		GridBagConstraints gbc_buttonSenden = new GridBagConstraints();
		gbc_buttonSenden.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSenden.gridx = 3;
		gbc_buttonSenden.gridy = 7;
		contentPane.add(buttonSenden, gbc_buttonSenden);

		JButton buttonStorno = new JButton("Felder l\u00F6schen");
		buttonStorno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldValue.setText("");
				textFieldmessage.setText("");
				textAreaRecipient.setText("");
			}
		});
		GridBagConstraints gbc_buttonStorno = new GridBagConstraints();
		gbc_buttonStorno.insets = new Insets(0, 0, 5, 5);
		gbc_buttonStorno.gridx = 4;
		gbc_buttonStorno.gridy = 7;
		contentPane.add(buttonStorno, gbc_buttonStorno);
	}

}
