package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.GsonBuilder;


public class MainFirstServer {

	public static ArrayList<PrintWriter> outputList;
	public static ArrayList<BufferedReader> inputList;
	public static HashMap<String,ServerTransactionOutput> BlocklessTransactionPool = new HashMap<String,ServerTransactionOutput>(); //listet alle outputs für die Miners
	public static String tailHash;
	public static ServerBlockchain blockchainobject;
	public static int fixedCount = 0;
	
	public static ServerWallet walletA;
	public static ServerWallet walletB;
	public static ServerWallet walletC;
	public static ServerWallet coinBase;
	public static ServerTransaction genesisTransaction;

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(ServerBlockchain.blockchain);
		System.out.println(blockchainJson);
		outputList = new ArrayList<>();
		inputList = new ArrayList<>();
		tailHash = "";
		blockchainobject = new ServerBlockchain();
		walletA = new ServerWallet();
		walletB = new ServerWallet();
		walletC = new ServerWallet();
		coinBase = new ServerWallet();
		genesisTransaction = new ServerTransaction(coinBase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinBase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionID = "0"; // manually set the transaction id
		genesisTransaction.outputs.add(new ServerTransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionID)); // manually add the Transactions Output
		ServerBlockchain.UTXOs.put(genesisTransaction.outputs.get(0).ID, genesisTransaction.outputs.get(0));
		ServerBlockchain.isChainValid();
		System.out.println("Creating and Mining Genesis block... ");
		ServerBlock genesis = new ServerBlock("0");
		genesis.addTransaction(genesisTransaction);
		ServerBlockchain.addBlock(genesis); 

		// TESTING
		
		String testprivateA, testprivateB;
		
		
		testprivateA = ServerStringUtil.privateKeyToWIF(walletA.privateKey);
		
		System.out.println("Wallet A data:");
		System.out.println(ServerStringUtil.publicKeyToString(walletA.publicKey));
		System.out.println(testprivateA);
		
		System.out.println("Wallet C data:");
		System.out.println(ServerStringUtil.publicKeyToString(walletC.publicKey));
		System.out.println(ServerStringUtil.privateKeyToWIF(walletC.privateKey));
		
		System.out.println("Now changing");
		walletC.setPrivateKey(ServerStringUtil.stringToPrivateKey(testprivateA));		

		System.out.println(ServerStringUtil.privateKeyToWIF(walletA.privateKey));
		System.out.println(ServerStringUtil.privateKeyToWIF(walletC.privateKey));
		System.out.println("Is private a = c?");
		System.out.println(walletC.privateKey == walletA.privateKey);
		System.out.println(walletC.privateKey.equals(walletA.privateKey));
		
		System.out.println("now changing publics");
		walletC.refreshKeyPair();
		walletA.refreshKeyPair();
		
		System.out.println(ServerStringUtil.publicKeyToString(walletA.publicKey));
		System.out.println(ServerStringUtil.publicKeyToString(walletC.publicKey));
		
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
		
		
		ServerBlock block1 = new ServerBlock(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\n1. WalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		ServerBlockchain.addBlock(block1);
		ServerBlockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		ServerBlock block2 = new ServerBlock(block1.hash);
		System.out.println("\n2. WalletA is Attempting to send funds (50) to WalletB...");
		System.out.println("The privateKeytoString and StringtoPrivateKy was tested. The test is success if the result correct");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 50f));
		ServerBlockchain.addBlock(block2);
		ServerBlockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		ServerBlock block3 = new ServerBlock(block2.hash);
		System.out.println("\n3. WalletB is Attempting to send funds (20) to WalletA...");
		System.out.println("Testing to change privateKey to public");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
		ServerBlockchain.addBlock(block3);
		ServerBlockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		ServerBlock block4 = new ServerBlock(block3.hash);
		System.out.println("\n4. WalletA is Attempting to send funds (20) to WalletB...");
		block4.addTransaction(walletA.sendFunds(walletB.publicKey, 20f));
		ServerBlockchain.addBlock(block4);
		ServerBlockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		ServerBlock block5 = new ServerBlock(block4.hash);
		System.out.println("\n5. WalletA is Attempting to send funds (20) to WalletB...");
		block5.addTransaction(walletA.sendFunds(walletB.publicKey, 20f));
		ServerBlockchain.addBlock(block5);
		ServerBlockchain.isChainValid();
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		acceptAndConnect();
	}

	private static void acceptAndConnect() {
		// TODO Auto-generated method stub
		try (ServerSocket serverSocket = new ServerSocket(1234)) {
			Runtime.getRuntime().addShutdownHook(new ServershutDownThread(outputList));
			System.out.println("Serversocket opened. Waiting for incoming connection");
			while (true) {
				ServerPeerThread pt = new ServerPeerThread(serverSocket.accept());
				pt.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("The port is already in use! Close all the other 'first servers'.");
		}

	}

}
