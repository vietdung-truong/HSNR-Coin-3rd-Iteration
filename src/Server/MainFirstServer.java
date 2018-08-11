package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.GsonBuilder;

import Client.Block;
import Client.Blockchain;
import Client.PeerThread;
import Client.StringUtil;
import Client.Transaction;
import Client.TransactionOutput;
import Client.Wallet;
import Client.shutDownThread;


public class MainFirstServer {

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

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(Blockchain.blockchain);
		System.out.println(blockchainJson);
		outputList = new ArrayList<>();
		inputList = new ArrayList<>();
		tailHash = "";
		blockchainobject = new Blockchain();
		walletA = new Wallet();
		walletB = new Wallet();
		walletC = new Wallet();
		coinBase = new Wallet();
		genesisTransaction = new Transaction(coinBase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinBase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionID = "0"; // manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionID)); // manually add the Transactions Output
		Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).ID, genesisTransaction.outputs.get(0));
		Blockchain.isChainValid();
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		Blockchain.addBlock(genesis); 

		// TESTING
		
		String testprivateA, testprivateB;
		
		
		testprivateA = StringUtil.privateKeyToWIF(walletA.privateKey);
		
		System.out.println("Wallet A data:");
		System.out.println(StringUtil.publicKeyToString(walletA.publicKey));
		System.out.println(testprivateA);
		
		System.out.println("Wallet C data:");
		System.out.println(StringUtil.publicKeyToString(walletC.publicKey));
		System.out.println(StringUtil.privateKeyToWIF(walletC.privateKey));
		
		System.out.println("Now changing");
		walletC.setPrivateKey(StringUtil.stringToPrivateKey(testprivateA));		

		System.out.println(StringUtil.privateKeyToWIF(walletA.privateKey));
		System.out.println(StringUtil.privateKeyToWIF(walletC.privateKey));
		System.out.println("Is private a = c?");
		System.out.println(walletC.privateKey == walletA.privateKey);
		System.out.println(walletC.privateKey.equals(walletA.privateKey));
		
		System.out.println("now changing publics");
		walletC.refreshKeyPair();
		walletA.refreshKeyPair();
		
		System.out.println(StringUtil.publicKeyToString(walletA.publicKey));
		System.out.println(StringUtil.publicKeyToString(walletC.publicKey));
		
		System.out.println("Is public a = c?");
		System.out.println(walletA.publicKey == walletC.publicKey);
		System.out.println(walletA.publicKey.equals(walletC.publicKey));
		
		
		/*walletB.setPrivateKey(StringUtil.stringToPrivateKey(testPrivate));
		boolean test = (walletA.privateKey == walletB.privateKey);
		System.out.println(testPrivate);
		System.out.println(StringUtil.privateKeyToString(walletA.privateKey));
		System.out.println(test);
		String testPublic = StringUtil.publicKeyToString(walletA.publicKey);
		System.out.println("\n\n" +testPublic + "\n" + testPrivate);
		
		walletA.setPrivateKey(StringUtil.stringToPrivateKey(testPrivate));
		walletA.setPublicKey(StringUtil.privateKeyToPublicKey(walletA.privateKey));
		System.out.println("\n\n" +testPublic + "\n" + testPrivate);*/
		
		//System.out.println("\n\n" + StringUtil.getStringFromKey(walletA.publicKey) + "\n" + StringUtil.getStringFromKey(walletA.privateKey));
		
		
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\n1. WalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		Blockchain.addBlock(block1);
		Blockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block2 = new Block(block1.hash);
		System.out.println("\n2. WalletA is Attempting to send funds (50) to WalletB...");
		System.out.println("The privateKeytoString and StringtoPrivateKy was tested. The test is success if the result correct");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 50f));
		Blockchain.addBlock(block2);
		Blockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block3 = new Block(block2.hash);
		System.out.println("\n3. WalletB is Attempting to send funds (20) to WalletA...");
		System.out.println("Testing to change privateKey to public");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
		Blockchain.addBlock(block3);
		Blockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block4 = new Block(block3.hash);
		System.out.println("\n4. WalletA is Attempting to send funds (20) to WalletB...");
		block4.addTransaction(walletA.sendFunds(walletB.publicKey, 20f));
		Blockchain.addBlock(block4);
		Blockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block5 = new Block(block4.hash);
		System.out.println("\n5. WalletA is Attempting to send funds (20) to WalletB...");
		block5.addTransaction(walletA.sendFunds(walletB.publicKey, 20f));
		Blockchain.addBlock(block5);
		Blockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		acceptAndConnect();
	}

	private static void acceptAndConnect() {
		// TODO Auto-generated method stub
		try (ServerSocket serverSocket = new ServerSocket(1234)) {
			Runtime.getRuntime().addShutdownHook(new shutDownThread(outputList));
			while (true) {
				PeerThread pt = new PeerThread(serverSocket.accept());
				pt.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("The port is already in use! Close all the other 'first servers'.");
		}

	}

}
