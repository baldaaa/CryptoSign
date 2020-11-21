package home.balda.cryptosign;

import home.balda.cryptosign.representation.SignDataRequest;
import home.balda.cryptosign.representation.SignatureResponse;
import home.balda.cryptosign.representation.VerifyDataRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Class holds generated key pairs
 */
@Component
@Scope("singleton")
public class KeyManager{
    private Map<String, KeyPair> keysHolder;
    private KeyPairGenerator keyPairGen;

    public KeyManager(){
        keysHolder = new ConcurrentHashMap<>();

        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return set of all key pairs
     */
    public Set<String> getKeyIds() {
        return keysHolder.keySet();
    }


    /**
     * generates new key pair
     * @return unique id of generated key pair
     */
    public String generateKey() {
        String id  = UUID.randomUUID().toString();
        keysHolder.put(id, keyPairGen.generateKeyPair());
        return id;
    }


    /**
     * delete key pair by given id
     * @param id
     */
    public void deleteKey( String id) {
        KeyPair keyPair = keysHolder.remove(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
    }


    private PublicKey getPublicKey(String id) {
        KeyPair keyPair = keysHolder.get(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
        return keyPair.getPublic();
    }

    private PrivateKey getPrivateKey(String id) {
        KeyPair keyPair = keysHolder.get(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
        return keyPair.getPrivate();
    }
    /**
     * Verify the signature for given data using public key
     * @param id - generated key pair id
     * @param data - signed data and signature
     * @return - true/false verification result
     */
    public Boolean verifyData(String id, VerifyDataRequest data) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            byte[] bytes = data.getData().getBytes(StandardCharsets.UTF_8);
            sign.initVerify(getPublicKey(id));
            sign.update(bytes);

            //convert signature back to byte array
            byte[] signature = Base64Utils.decode(data.getSignature().getBytes(StandardCharsets.UTF_8));

            //Verifying the signature
            return sign.verify(signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }catch(SignatureException e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Calculate the Signature for given data using private key
     * @param id - generated key pair id
     * @param data - data to sign
     * @return - calculated signature
     */
    public SignatureResponse singData(String id, SignDataRequest data){

        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(getPrivateKey(id));
            sign.update(data.getData().getBytes(StandardCharsets.UTF_8));

            //Create the signature
            byte[] signature = sign.sign();
            //Convert signature byte array to string
            String signatureStr = new String(Base64Utils.encode(signature), StandardCharsets.UTF_8);
            return new SignatureResponse(signatureStr);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }
    }
}
