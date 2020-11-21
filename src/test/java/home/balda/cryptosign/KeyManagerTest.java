package home.balda.cryptosign;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class KeyManagerTest {

    KeyManager keyManager;

    @Before
    public void setUp() throws Exception {
       keyManager = new KeyManager();
    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
    public void getKeyIds() {
        String id1 = keyManager.generateKey();
        String id2 = keyManager.generateKey();
        String id3 = keyManager.generateKey();
        Assert.assertTrue(keyManager.getKeyIds().contains(id1));
        Assert.assertTrue(keyManager.getKeyIds().contains(id2));
        Assert.assertTrue(keyManager.getKeyIds().contains(id3));

    }

    @Test
    public void getPublicKey() {
        String id = keyManager.generateKey();
        PublicKey key = keyManager.getPublicKey(id);
        Assert.assertEquals(keyManager.keysHolder.get(id).getPublic().getAlgorithm(),"RSA");
    }


    @Test
    public void getPublicKey_Fail() {
        exceptionRule.expect(NullPointerException.class);
        PublicKey key = keyManager.getPublicKey(null);

    }

    @Test
    public void getPublicKey_Fail2() {
        exceptionRule.expect(NullPointerException.class);
        PublicKey key = keyManager.getPublicKey("null");

    }

    @Test
    public void getPrivateKey() {
        String id = keyManager.generateKey();
        PrivateKey key = keyManager.getPrivateKey(id);
        Assert.assertEquals("RSA",keyManager.keysHolder.get(id).getPrivate().getAlgorithm());
    }

    @Test
    public void generateKey() {
        String id = keyManager.generateKey();
        Assert.assertEquals(keyManager.keysHolder.keySet().size(), 1);
        Assert.assertTrue(keyManager.keysHolder.containsKey(id));
        Assert.assertEquals(KeyPair.class,keyManager.keysHolder.get(id).getClass());
    }

    @Test
    public void deleteKey() {
        String id = keyManager.generateKey();
        String id2 = keyManager.generateKey();
        Assert.assertEquals(keyManager.keysHolder.size(), 2);
        keyManager.deleteKey(id);
        Assert.assertFalse(keyManager.keysHolder.containsKey(id));
        Assert.assertEquals(keyManager.keysHolder.size(), 1);
    }

    @Test
    public void deleteKey_Fail() {
        exceptionRule.expect(NullPointerException.class);
        keyManager.deleteKey(null);
    }
    @Test
    public void verifyData() {
        String id = keyManager.generateKey();
        String data = "jopa";
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(keyManager.getPrivateKey(id));
            sign.update(data.getBytes(StandardCharsets.UTF_8));

            //Create the signature
            byte[] signature = sign.sign();
            String signatureStr =  new String(Base64Utils.encode(signature), StandardCharsets.UTF_8);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            sign.initVerify(keyManager.getPublicKey(id));
            sign.update(bytes);

            //Verifying the signature
            Boolean bool =  sign.verify(Base64Utils.decode(signatureStr.getBytes(StandardCharsets.UTF_8)));
            Assert.assertTrue(bool);
        }catch(Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

}