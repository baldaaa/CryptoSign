package home.balda.cryptosign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.balda.cryptosign.representation.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CryptoSingControllerTest {
    @Autowired
    private MockMvc mvc;

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss.SSSZ").create();
    }
    @Test
    public void generateKey() throws Exception {
        //WHEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);
        assertNotNull(keyResult);
    }

    @Test
    public void getKeyIds() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult1 = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),IdResponse.class);
        //WHEN
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.get("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());
        List<String> keys = Arrays.asList(gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(), String[].class));
        assertNotNull(keys);
        assertTrue(keys.contains(keyResult.getKeyId()));
        assertTrue(keys.contains(keyResult1.getKeyId()));
    }




    @Test
    public void deleteKey() throws Exception{
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);
        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/crypto/keys/"+keyResult.getKeyId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.get("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());
        List<String> keys = Arrays.asList(gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(), String[].class));
        assertNotNull(keys);
        assertFalse(keys.contains(keyResult.getKeyId()));

    }
    @Test
    public void deleteKey_FailBadKey() throws Exception{
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);
        //WHEN
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.delete("/crypto/keys/kuku")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        CryptoSignErrorResponse response = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),CryptoSignErrorResponse.class);
        assertFalse(response.errorMessage == "Key not exist");

    }

    @Test
    public void signData() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        SignDataRequest signDataRequest = new SignDataRequest();
        signDataRequest.setData("kuku");
        //WHEN
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/sign")
                .content(gson.toJson(signDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isOk());
        SignatureResponse signatureResponse = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),SignatureResponse.class);
        assertNotNull(signatureResponse.getSignature());

    }

    @Test
    public void signData_FailNullData() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        SignDataRequest signDataRequest = new SignDataRequest();
        signDataRequest.setData(null);
        //WHEN
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/sign")
                .content(gson.toJson(signDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isBadRequest());
        CryptoSignErrorResponse response = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),CryptoSignErrorResponse.class);
        assertFalse(response.errorMessage == "Bad data");
    }

    @Test
    public void verifyData() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        SignDataRequest signDataRequest = new SignDataRequest();
        signDataRequest.setData("kuku");
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/sign")
                .content(gson.toJson(signDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        SignatureResponse signatureResponse = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),SignatureResponse.class);
        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature(signatureResponse.getSignature());

        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/verify")
                .content(gson.toJson(verifyDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        BooleanResponse response = gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(),BooleanResponse.class);
        assertTrue(response.verified);
    }

    @Test
    public void verifyData_FailBadSignature64() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

       VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature("jsrgm^&*\"\"\"bvczu'''''ygrtleiaufb$^");
        //WHEN
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/verify")
                .content(gson.toJson(verifyDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isBadRequest());
        CryptoSignErrorResponse response = gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(),CryptoSignErrorResponse.class);

    }


    @Test
    public void verifyData_FailBadSignature() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature("anNyZ21eJioiIiJidmN6dScnJycneWdydGxlaWF1ZmIkXg==");
        //WHEN
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/verify")
                .content(gson.toJson(verifyDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        BooleanResponse response = gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(),BooleanResponse.class);
        //THEN
        assertFalse(response.verified);
    }
    @Test
    public void verifyData_FailNullSignature() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("kuku");
        verifyDataRequest.setSignature(null);
        //WHEN
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/blabla/verify")
                .content(gson.toJson(verifyDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //THEN
                .andExpect(status().isBadRequest());
        CryptoSignErrorResponse response = gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(),CryptoSignErrorResponse.class);
        assertFalse(response.errorMessage == "Bad signature");
    }

    @Test
    public void verifyData_FailWrongData() throws Exception {
        //GIVEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        IdResponse keyResult = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(),IdResponse.class);

        SignDataRequest signDataRequest = new SignDataRequest();
        signDataRequest.setData("kuku");
        ResultActions resultActions1 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/sign")
                .content(gson.toJson(signDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        SignatureResponse signatureResponse = gson.fromJson(resultActions1.andReturn().getResponse().getContentAsString(),SignatureResponse.class);
        VerifyDataRequest verifyDataRequest = new VerifyDataRequest();
        verifyDataRequest.setData("jopa");
        verifyDataRequest.setSignature(signatureResponse.getSignature());

        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.post("/crypto/keys/"+keyResult.getKeyId()+"/verify")
                .content(gson.toJson(verifyDataRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        BooleanResponse response = gson.fromJson(resultActions2.andReturn().getResponse().getContentAsString(),BooleanResponse.class);
        assertFalse(response.verified);
    }
}