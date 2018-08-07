package GUI;


import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Client.Blockchain;
import Client.MainStartScreen;
import Client.StringUtil;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Insets;
import javax.swing.JTextArea;
import java.awt.Scrollbar;
import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;

public class Dev extends JFrame {

	private JPanel contentPane;
	private JTextField txtWalletbpublic;
	private JLabel lblBlockchain;
	private JTextArea txtrBlockchain;
	private JButton btnCheckBlockchain;
	private JButton btnChangBlockChain;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dev frame = new Dev();
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
	public Dev() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 770, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel lblWalletBPublic = new JLabel("Wallet B Public");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblWalletBPublic, 22, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblWalletBPublic, 0, SpringLayout.WEST, contentPane);
		contentPane.add(lblWalletBPublic);

		txtWalletbpublic = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtWalletbpublic, 19, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtWalletbpublic, 19, SpringLayout.EAST, lblWalletBPublic);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtWalletbpublic, 660, SpringLayout.EAST, lblWalletBPublic);
		txtWalletbpublic.setText(StringUtil.publicKeyToString(MainStartScreen.walletB.publicKey));
		contentPane.add(txtWalletbpublic);
		txtWalletbpublic.setColumns(10);

		lblBlockchain = new JLabel("Blockchain");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblBlockchain, 6, SpringLayout.SOUTH, lblWalletBPublic);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblBlockchain, 0, SpringLayout.WEST, lblWalletBPublic);
		contentPane.add(lblBlockchain);

		txtrBlockchain = new JTextArea();
		JScrollPane sp = new JScrollPane(txtrBlockchain);
		sl_contentPane.putConstraint(SpringLayout.NORTH, sp, 40, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, sp, 49, SpringLayout.EAST, lblBlockchain);
		sl_contentPane.putConstraint(SpringLayout.EAST, sp, -15, SpringLayout.EAST, contentPane);
		//sp.setBounds(10,60,780,500);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		txtrBlockchain.setText(MainStartScreen.blockchainJson);
		txtrBlockchain.setEditable(false);
		contentPane.add(sp);


		btnCheckBlockchain = new JButton("Check Blockchain");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnCheckBlockchain, 358, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, sp, -6, SpringLayout.NORTH, btnCheckBlockchain);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnCheckBlockchain, 97, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnCheckBlockchain, -495, SpringLayout.EAST, contentPane);
		btnCheckBlockchain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Blockchain.isChainValid()) {
					Component frame = null;
					// default title and icon
					JOptionPane.showMessageDialog(frame, "Blockchain is valid!");
				}else {
					Component frame = null;
					// default title and icon
					JOptionPane.showMessageDialog(frame, "Blockchain is invalid! Please check!");
				}
			}
		});
		contentPane.add(btnCheckBlockchain);

		btnChangBlockChain = new JButton("Change Block Chain");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnChangBlockChain, 0, SpringLayout.NORTH, btnCheckBlockchain);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnChangBlockChain, 6, SpringLayout.EAST, btnCheckBlockchain);
		btnChangBlockChain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		contentPane.add(btnChangBlockChain);
		

	}
}
