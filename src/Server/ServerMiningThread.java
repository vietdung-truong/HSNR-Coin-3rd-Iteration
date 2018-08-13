package Server;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ServerMiningThread extends Thread{
	

	@SuppressWarnings("null")
	public void run() {
		while(true) {
			if(!ServerBlockchain.blocklessPool.isEmpty()) {
				ServerBlock tempBlock = new ServerBlock(ServerBlockchain.blockchain.get(ServerBlockchain.blockchain.size()-1).hash);
				for (int i = 0; i < 10; i++) {
					ServerTransaction tempTransaction = ServerBlockchain.blocklessPool.get(i);
					tempTransaction = new ServerTransaction(MainFirstServer.coinBase.publicKey, MainFirstServer.coinBase.publicKey, 100f, null);
					tempTransaction.generateSignature(MainFirstServer.coinBase.privateKey); // manually sign the genesis transaction
					tempTransaction.transactionID = "0"; // manually set the transaction id
					tempTransaction.outputs.add(new ServerTransactionOutput(tempTransaction.recipient, tempTransaction.value,
							tempTransaction.transactionID)); // manually add the Transactions Output
					ServerBlockchain.UTXOs.put(tempTransaction.outputs.get(0).ID, tempTransaction.outputs.get(0));
					tempBlock.addTransaction(tempTransaction);
					//Setting parameter
					if(ServerBlockchain.blocklessPool.isEmpty()) break;
				}
				ServerBlockchain.addBlock(tempBlock);
				for(ServerTransaction i : tempBlock.transactions) {
					for(int b = 0; b < ServerBlockchain.blocklessPool.size(); b++) {
						if(i.transactionID.equals(ServerBlockchain.blocklessPool.get(b).transactionID)) {
							ServerBlockchain.blocklessPool.remove(b);
						}
					}
				}
				ServerBlockchain.isChainValid();
				Component frame = null;
				//default title and icon
				JOptionPane.showMessageDialog(frame,
				    "Ein Block wurde gemint! Hash:\n" + tempBlock.hash);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setVisible(false);
			}
		}
	}

}
