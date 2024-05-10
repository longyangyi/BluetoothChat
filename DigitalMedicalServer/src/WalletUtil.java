import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.FileHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arxanfintech.common.crypto.Crypto;
import com.arxanfintech.common.crypto.Hash;
import com.arxanfintech.common.rest.Client;
import com.arxanfintech.common.structs.WalletType;

public class WalletUtil {
	
    private String certpath = "C:\\digitalMedicalServerFolder\\your_cert_dir";
    String signUtilPath="C:\\digitalMedicalServerFolder\\sign-util";
	private String address = "139.198.15.132:9143";
    private String apikey = "eYE3slxTp1533798452";
    private String sign_params_creator = "did:axn:5dd53a35-63bb-4fe2-af7a-8447d262cbd6";
    private String sign_params_nonce = "nonce";
    private String sign_params_created = "1534323232";
    private String sign_params_privatekeyBase64 = "8y30dUPCXslL+Nuk7cQHVWoYnQC8ielDy+qcuuDXlxJpPfc4nIgsmhvvxyIKoLZTTPHuDwBQEi6RvZ790cZ4tQ==";
    private String strheader = "{\"Callback-Url\":\"http://something.com\",\"Crypto-Mode\":\"0\"}";
    private Boolean enableCrypto = true;
    
    
    Wallet wallet;
    JSONObject jsonheader;
    logUtil log;
    
	public void initWalletClient() {
		Client client = new Client(apikey, certpath, sign_params_creator,
		sign_params_created, sign_params_nonce,
		sign_params_privatekeyBase64, address, enableCrypto);
		wallet = new Wallet(client);
		jsonheader = JSON.parseObject(strheader);
		log=new logUtil();
	}
	public JSONObject registe(String access,String secret) {//secret includes Big ,small,number
		
		String strdata = "{\"access\":\""+access+"\",\"secret\":\""+secret+"\",\"type\":\"Person\", \"id\": \"\"}";
		JSONObject jsondata = JSON.parseObject(strdata);
		try {
			JSONObject jsonResponse = wallet.register(jsonheader, jsondata);
			log.log("registe :access:"+access+" secret:"+secret+" \nresponse:"+jsonResponse.toString());
			return jsonResponse;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject queryWalletInfomation(String walletId) {
		try{
			JSONObject response=wallet.queryWalletInfos(jsonheader,walletId);
			log.log("query wallet infomation :"+walletId+" \nresponse : "+response.toString());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject queryWalletBalance(String walletId) {
		try{
			JSONObject response=wallet.queryWalletBalance(jsonheader,walletId);
			log.log("query wallet balance :"+walletId+" \nresponse : "+response.toString());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject createPOE(String walletId,String privateKeyBase64,String name) {
		String strdata = "{\"hash\": \"\", \"name\":\""+name+"\",\"parent_id\":\"\",\"owner\":\"" + walletId
                + "\", \"id\":\"\",\"metadata\":\"\"}";

        JSONObject jsondata = JSON.parseObject(strdata);

        String created = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() / 1000);
        String nonce = "test-nonce";

        try {
            JSONObject response = wallet.createPOE(jsonheader, jsondata, walletId, created, nonce, privateKeyBase64,
                    signUtilPath+"/sign-util");
            log.log("creat POE :wallet Id:"+walletId+" \nresponse:"+response.toString());
            return response;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	public JSONObject queryPOE(String POEId) {
		try {
			JSONObject response=wallet.queryPOE(jsonheader, POEId);
			log.log("query POE :POE Id:"+POEId+" \nresponse:"+response.toString());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject uploadFile(String POEId,String filePath) {
		try{
			JSONObject response=wallet.uploadFile(jsonheader,filePath,POEId,true);
			log.log("upload file :POE Id:"+POEId+" file path:"+filePath+" \nresponse:"+response.toString());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject uploadFileAndQueryPOE(String walletId,String privateKeyBase64,String POEName,String filePath) {
		JSONObject jo=createPOE(walletId,privateKeyBase64,POEName);
		String s=jo.getJSONObject("Payload").getString("id").toString();
		try {
			Thread.sleep(3000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		uploadFile(s,filePath);
		return queryPOE(s);
	}
	public JSONObject issueTokens(String walletId,String POEId,String privateKeyBase64,String amount) {
		try {
			String created = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() / 1000);
	        String nonce = "test-nonce";
			String strdata = "{\"owner\":\"" + walletId + "\",\"asset_id\":\"" + POEId
			+ "\", \"amount\":"+amount+", \"fees\":{}, \"issuer\":\"" + walletId + "\"}";
			JSONObject jsondata = JSON.parseObject(strdata);
			JSONObject response = wallet.issueTokens(jsonheader, jsondata, walletId,
			created, nonce, privateKeyBase64,
			signUtilPath+"/sign-util");
			log.log("issue tokens :wallet Id:"+walletId+" POE Id:"+POEId+" amount:"+amount+" \nresponse:"+response.toString());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject transferTokens(String fromWalletId,String toWalletId,String tokensId,String privateKeyBase64,String amount) {
		try {
			String strdata = "{\"from\":\"" + fromWalletId + "\",\"to\":\"" + toWalletId
				+ "\",\"asset_id\":\"\",\"tokens\":[{\"token_id\":\"" + tokensId
				+ "\",\"amount\":"+amount+"}],\"fee\":{\"amount\":0},\"message\":\"transferTokens\"}";
		    JSONObject jsondata = JSON.parseObject(strdata);
		    String created = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() / 1000);
		    String nonce = "nonce";
		    JSONObject response = wallet.transferTokens(jsonheader, jsondata, fromWalletId,
		    	created, nonce,privateKeyBase64,
		    	signUtilPath+"/sign-util");
			log.log("transfer tokens :from wallet Id:"+fromWalletId+" to wallet Id:"+toWalletId+" tokens Id:"+tokensId+" amount:"+amount+" \nresponse:"+response.toString());
		    return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject issueAssets(String walletId,String POEId,String privateKeyBase64) {
		try {
			String created = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() / 1000);
		    String nonce = "nonce";
			String strdata = "{\"owner\":\"" + walletId + "\",\"asset_id\":\"" + POEId
		    + "\", \"fees\":{}, \"issuer\":\"" + walletId + "\"}";
		    JSONObject jsondata = JSON.parseObject(strdata);
		    
		    JSONObject response = wallet.issueAssets(jsonheader, jsondata, walletId,
		    created, nonce, privateKeyBase64,
		    signUtilPath+"/sign-util");
			log.log("issue assets:wallet Id:"+walletId+" POE Id:"+POEId+" \nresponse:"+response.toString());
		    return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject transferAssets(String fromWalletId,String toWalletId,String assetsId,String privateKeyBase64) {
		try {
			String created = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() / 1000);
		    String nonce = "nonce";
		    String strdata = "{\"from\":\"" + fromWalletId + "\",\"to\":\"" + toWalletId+
		    		"\",\"assets\":[\""+assetsId+"\"],\"fee\":{}}";
		    JSONObject jsondata = JSON.parseObject(strdata);
		    
		    JSONObject response =wallet.transferAssets(jsonheader, jsondata, fromWalletId,
		    		created, nonce,
		    		privateKeyBase64,
		    	    signUtilPath+"/sign-util");
			log.log("transfer assets :from wallet Id:"+fromWalletId+" to wallet Id:"+toWalletId+" assets id:"+assetsId+" \nresponse:"+response.toString());
		    return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String decrypt(String s) {
		Client client = new Client(apikey, certpath, sign_params_creator,
				sign_params_created, sign_params_nonce,
				sign_params_privatekeyBase64, address, enableCrypto);
		String privateKeyPath = client.GetCertPath() + "/users/" + client.GetApiKey() + "/" + client.GetApiKey()
        + ".key";
		String publicCertPath = client.GetCertPath() + "/tls/tls.cert";
		try{
			Crypto crypto = new Crypto(new FileInputStream(privateKeyPath), new FileInputStream(publicCertPath));
			String outcome=crypto.decryptAndVerify(s.getBytes());
			log.log("decrypt:from:"+s+" \nto:"+outcome);
			return outcome;			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void hash(String filePath) {
		try {
			FileInputStream fis=new FileInputStream(new File(filePath));
			DataInputStream dis=new DataInputStream(new BufferedInputStream(fis));
			byte[] bytes=new byte[10000];
			dis.read(bytes);
			byte[] bytes32=new byte[32];
			Hash hash=new Hash(bytes32);

			log.log("hash:from:"+filePath+" \nto:"+hash.hash(bytes).toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
/*
 * wallet applied:
 * id:person   User
 * did:axn:5a1f3cdb-a50c-495b-b76c-2bf84b70c5ca
 * 
 * endpoint:
 * 2c1051270c82d5a8dc2f5072edddacff51b8019f1cc6d9add65dc2de91110104
 * 
 * privateKey:
 * 8vrFWV9t7p0CNkV7at+GSE+pqEaIjq6wmxo1/+2Apz5SNC0Crdb4fu4MB0fK6mF6qmXSldlyy9m9cQ2mfldOow==
 * 
 * public key:
 * UjQtAq3W+H7uDAdHyupheqpl0pXZcsvZvXENpn5XTqM=
 * 
 * POE Id:
 * did:axn:5a85ebe5-cf14-4a11-9f28-536a033dabb1
 * 
 * assetId:
 * did:axn:5a85ebe5-cf14-4a11-9f28-536a033dabb1
 * 
 * 
 * **********************
 * wallet applied:  2
 * id:       Doctor
 * did:axn:f2f59a48-27b4-4d5a-935c-6aaeb77f3f79
 * 
 * endpoint:
 * c8aff14c57c33aef1f2a21f84ce4bcb5fdcd86463cdcaeff0c58d2f0d0e27355
 * 
 * privateKey:
 * hwXcfpUnluHLeCHdvxG9nEeOCrIndRKZaoxLfs1o+4BgiZOfsH3XNPbB65VIP/nZRbBXb3qWgfpHk12N4QOtug==
 * 
 * public key:
 * YImTn7B91zT2weuVSD/52UWwV296loH6R5NdjeEDrbo=
 * 
 * POE Id: // asset Id
 * did:axn:01aaf198-b5a4-4546-b90b-9507823127b5
 * 
 *****************************
 * wallet organization:
 * id:
 * did:axn:190eb5c3-7c27-4a10-aed3-908fdd3cf242
 * 
 * endPoint:
 * be702440d989f75532a62cd195b147e72de1ed2fe66d9c4e165031f65329fe61
 * 
 * private key:
 * iEIbZikjrE+g5mAkccFyMWnsEVh4O0TFJy85CpwE67jiS8ijUNSZW4wxOqyTfANO+tdLFanzQxT/5Oth//KHXQ==
 * 
 * public key:
 * 4kvIo1DUmVuMMTqsk3wDTvrXSxWp80MU/+TrYf/yh10=
 * 
 * POE Id:
 * did:axn:4b9f60de-8e65-47ea-9916-d0e5ce24decb
 * 
 * colored tokens:
 * 1b433abcae9edb7a75d45538f94c09429bfe3cbbfbcb08445f7f09398ca8c85a
 * 
 * POE Id2:
 * did:axn:b3d42b7b-329d-443b-8743-7ff59262751a
 * 
 * colored tokens:
 * 8fe6427506f82021eb5fe5a428c8454cb61381158a7009347d2fe209f26018e5
 *
 * 
 * 
 * the following for Wise Medical Server:
 * 
*/


