package home.balda.cryptosign;

import home.balda.cryptosign.representation.SignDataRequest;
import home.balda.cryptosign.representation.SignatureResponse;
import home.balda.cryptosign.representation.VerifyDataRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class )
@SpringBootTest
public class CryptoSignServiceTest {

    @Autowired
    CryptoSignService cryptoSignService;

    @Test
    public void getKeyIds() {
        Set<String> keys = cryptoSignService.getKeyIds();
        String id = cryptoSignService.generateKey();
        String id1 = cryptoSignService.generateKey();
        keys = cryptoSignService.getKeyIds();
        Assert.assertTrue(keys.contains(id));
        Assert.assertTrue(keys.contains(id1));
        Assert.assertFalse(keys.contains("id1"));

    }

    @Test
    public void generateKey() {
        String id = cryptoSignService.generateKey();
        Assert.assertTrue(cryptoSignService.getKeyIds().contains(id));
    }

    @Test
    public void deleteKey() {
        String id = cryptoSignService.generateKey();
        String id1 = cryptoSignService.generateKey();
        cryptoSignService.deleteKey(id);
        Assert.assertTrue(cryptoSignService.getKeyIds().contains(id1));
        Assert.assertFalse(cryptoSignService.getKeyIds().contains(id));
    }

    @Test
    public void singData() {
        String id = cryptoSignService.generateKey();
        SignDataRequest request = new SignDataRequest();
        request.setData("kuku");
        SignatureResponse response =  cryptoSignService.singData(id,request);
        Assert.assertNotNull(response);
        SignatureResponse response1 =  cryptoSignService.singData(id,request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getSignature(), response1.getSignature());
    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
    public void singData_Fail_DataNull() {
        exceptionRule.expect(NullPointerException.class);
        String id = cryptoSignService.generateKey();
        SignDataRequest request = new SignDataRequest();
        request.setData(null);
        SignatureResponse response =  cryptoSignService.singData(id,request);
    }

    @Test
    public void singData_Fail_KeyNull() {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Key does not exist");
        String id = cryptoSignService.generateKey();
        SignDataRequest request = new SignDataRequest();
        request.setData("kuku");
        SignatureResponse response =  cryptoSignService.singData("blabla",request);
    }


    @Test
    public void verifyData() {
        String id = cryptoSignService.generateKey();
        SignDataRequest request = new SignDataRequest();
        request.setData("kuku");
        SignatureResponse response =  cryptoSignService.singData(id,request);
        Assert.assertNotNull(response);
        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature(response.getSignature());
        Boolean isGood = cryptoSignService.verifyData(id, verifyDataRequest);
        Assert.assertTrue(isGood);
    }

    @Test
    public void verifyData_FailData() {
        String id = cryptoSignService.generateKey();
        SignDataRequest request = new SignDataRequest();
        request.setData("kuku");
        SignatureResponse response =  cryptoSignService.singData(id,request);
        Assert.assertNotNull(response);
        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("jopa");
        verifyDataRequest.setSignature(response.getSignature());
        Boolean isGood = cryptoSignService.verifyData(id, verifyDataRequest);
        Assert.assertFalse(isGood);
    }
    @Test
    public void verifyData_FailSignature() {
        String id = cryptoSignService.generateKey();
        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature("blabla");
        Boolean isGood = cryptoSignService.verifyData(id, verifyDataRequest);
        Assert.assertFalse(isGood);
    }

}