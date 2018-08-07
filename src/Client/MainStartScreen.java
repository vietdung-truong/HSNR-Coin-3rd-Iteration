package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

import com.google.gson.GsonBuilder;

import GUI.Dev;
import GUI.Empfangen;
import GUI.Portfolio;
import GUI.Send;


import javax.swing.JButton;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainStartScreen {

	private JFrame frame;
	static int thisPort;
	static String thisIP;
	
	
	public static ArrayList<PrintWriter> outputList;
	public static ArrayList<BufferedReader> inputList;
	public static HashMap<String,TransactionOutput> BlocklessTransactionPool = new HashMap<String,TransactionOutput>(); //listet alle outputs für die Miners
	public static String tailHash;
	public static Blockchain blockchainobject;
	public static int fixedCount = 0;
	
	public static Wallet walletA;
	public static Wallet walletB;
	public static Wallet walletC;
	public static Wallet coinBase;
	public static Transaction genesisTransaction;
	
	public static JTextField txtCoin;
	private JTextField txtPeers;
	
	public static String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(Blockchain.blockchain);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		System.out.println(blockchainJson);
		outputList = new ArrayList<>();
		inputList = new ArrayList<>();
		tailHash = "";
		blockchainobject = new Blockchain();
		walletA = new Wallet();
		walletB = new Wallet();
		walletC = new Wallet();
		coinBase = new Wallet();
		
		Blockchain.initiateblocklessPool();
		
		System.out.println("WalletB Public Adress:");
		System.out.println(StringUtil.publicKeyToString(walletB.publicKey));
		
		genesisTransaction = new Transaction(coinBase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinBase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionID = "0"; // manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionID)); // manually add the Transactions Output
		Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).ID, genesisTransaction.outputs.get(0));
		Blockchain.isChainValid();
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		Blockchain.addBlock(genesis); 
		
		Block block1 = new Block(genesis.hash);
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		Blockchain.addBlock(block1);
		Blockchain.isChainValid();

		Block block2 = new Block(block1.hash);
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 10f));
		Blockchain.addBlock(block2);
		Blockchain.isChainValid();

		Block block3 = new Block(block2.hash);
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
		Blockchain.addBlock(block3);
		Blockchain.isChainValid();
		
		blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(Blockchain.blocklessPool);
		//System.out.println(blockchainJson);
		

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainStartScreen window = new MainStartScreen();
					window.frame.setVisible(true);
					ServerSocketStart sst = new ServerSocketStart();
					sst.start();
					UpdateThread ut = new UpdateThread();
					ut.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainStartScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 390);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnVerlauf = new JButton("Portfolio");
		btnVerlauf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Portfolio portfolio = new Portfolio();
				portfolio.setVisible(true);
			}
			
		});
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, btnVerlauf, 30, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnVerlauf, 49, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);
		frame.getContentPane().add(btnVerlauf);
		
		JLabel lblKontostand = new JLabel("Kontostand:");
		springLayout.putConstraint(SpringLayout.NORTH, lblKontostand, 4, SpringLayout.NORTH, btnVerlauf);
		frame.getContentPane().add(lblKontostand);
		
		txtCoin = new JTextField(Float.toString(walletA.getBalance()));
		springLayout.putConstraint(SpringLayout.EAST, lblKontostand, -20, SpringLayout.WEST, txtCoin);
		springLayout.putConstraint(SpringLayout.NORTH, txtCoin, 31, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, txtCoin, 482, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtCoin, 517, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(txtCoin);
		txtCoin.setColumns(10);
		
		JLabel lblCoins = new JLabel();
		springLayout.putConstraint(SpringLayout.NORTH, lblCoins, 34, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblCoins, 522, SpringLayout.WEST, frame.getContentPane());
		lblCoins.setText("Coins");
		frame.getContentPane().add(lblCoins);
		
		JButton btnSenden = new JButton("Coin senden");
		btnSenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Send portfolio = new Send();
					portfolio.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSenden, 88, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnSenden, 40, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnSenden);
		
		JLabel lblConnectedwith = new JLabel("Wir sind gerade verbunden mit:");
		springLayout.putConstraint(SpringLayout.NORTH, lblConnectedwith, 4, SpringLayout.NORTH, btnSenden);
		springLayout.putConstraint(SpringLayout.EAST, lblConnectedwith, 0, SpringLayout.EAST, lblKontostand);
		frame.getContentPane().add(lblConnectedwith);
		
		txtPeers = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, txtPeers, 89, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, txtPeers, 482, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtPeers, 517, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(txtPeers);
		txtPeers.setColumns(10);
		
		JLabel lblPeers = new JLabel("Peers");
		springLayout.putConstraint(SpringLayout.NORTH, lblPeers, 92, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblPeers, 522, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblPeers);
		
		JButton btnCoinEmpfangen = new JButton("Coin empfangen");
		btnCoinEmpfangen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Empfangen empfangen = new Empfangen();
				empfangen.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnCoinEmpfangen, 146, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnCoinEmpfangen, 30, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnCoinEmpfangen);
		
		JLabel lblDieLetzteTransaktion = new JLabel("Die letzte Transaktion:");
		springLayout.putConstraint(SpringLayout.NORTH, lblDieLetzteTransaktion, 4, SpringLayout.NORTH, btnCoinEmpfangen);
		springLayout.putConstraint(SpringLayout.EAST, lblDieLetzteTransaktion, 0, SpringLayout.EAST, lblKontostand);
		frame.getContentPane().add(lblDieLetzteTransaktion);
		
		JButton button = new JButton("Mining Mode");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MiningThread mnt = new MiningThread();
				mnt.start();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, button, 204, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, button, 40, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(button);
		
		JButton btnDev = new JButton("Dev");
		btnDev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Dev dev = new Dev();
				dev.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnDev, 292, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnDev, 60, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnDev);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenu mnYourAccount = new JMenu("Your Account");
		mnMenu.add(mnYourAccount);
	}

	public JTextField getTxtCoin() {
		return txtCoin;
	}

	public JTextField getTxtPeers() {
		return txtPeers;
	}

	public void setTxtPeers(JTextField txtPeers) {
		this.txtPeers = txtPeers;
	}
	
	

}
