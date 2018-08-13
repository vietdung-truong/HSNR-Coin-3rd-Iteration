package Server;

import java.security.PublicKey;


public class ServerTransactionOutput {

	public String ID;
	public PublicKey recipient; //wem werden die coins gehören
	public float value;
	public String parentTransactionId; //das ID der TRansaktion, von welcher die folgende Transaktion kommt
	
	
	public ServerTransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
		super();
		this.ID = ServerStringUtil.applySha256(ServerStringUtil.publicKeyToString(recipient)+Float.toString(value)+parentTransactionId);
		this.recipient = recipient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
	}
	
	public boolean isMine(PublicKey publicKey) {
		return (publicKey.equals(recipient));
	}
}