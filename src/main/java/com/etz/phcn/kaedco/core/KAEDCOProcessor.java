/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.phcn.kaedco.core;

/**
 *
 * @author jennifer.okosisi
 */

import com.etz.phcn.kaedco.util.KADEDCService;
import com.etz.phcn.utils.DiscoProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class KAEDCOProcessor extends DiscoProcessor {
  private static Logger logger = null;
  
  public static String username;
  
  public static String password;
  
  public static String baseUrl;
  
  public static String tokenId;
  
  public static String auth_url;
  
  private static Properties prop = new Properties();
  
  private static String granttype;
  
  private KADEDCService service;
  
  static {
    try {
      prop.load(new FileInputStream(new File("cfg/phcndb-config.properties")));
      String mode = prop.getProperty("RUNNING_MODE");
      username = prop.getProperty("KAD_NEW_CLIENT_ID");
      password = prop.getProperty("KAD_NEW_CLIENT_SECRET");
      baseUrl = prop.getProperty("KAD_NEW_TOKEN_BASEURL");
      tokenId = prop.getProperty("KAD_NEW_CLIENT_ID");
      auth_url = prop.getProperty("KAD_NEW_TOKEN_BASEURL");
      granttype = prop.getProperty("KAD_NEW_GRANT_TYPE");
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }
  
  public KAEDCOProcessor(Logger log) {
    logger = log;
    this.service = new KADEDCService(username, password, tokenId, baseUrl, auth_url, granttype, log);
  }
  
  public String doPostPaidCustomerInfo(JsonObject jsonData) {
    String result = "";
    String accountOrMeterNo = jsonData.get("accountNumber").getAsString();
    String uniqueTransId = jsonData.get("uniqueTransId").getAsString();
    JsonObject verificationDataObj = new JsonObject();
    verificationDataObj.addProperty("requestType", "verification");
    verificationDataObj.addProperty("disco", "KAD");
    verificationDataObj.addProperty("accountType", "Postpaid");
    verificationDataObj.addProperty("uniqueTransId", uniqueTransId);
    verificationDataObj.addProperty("accountNumber", accountOrMeterNo);
    try {
      String[] postResponse = this.service.validateMeter(accountOrMeterNo);
      logger.info("RESPONSE CODE ==== " + postResponse[0]);
      if (postResponse[0].equalsIgnoreCase("201") || postResponse[0].equalsIgnoreCase("200")) {
        logger.info("KAD POSTPAID VERIFICATION RESPONSE : " + postResponse[1]);
        JsonObject responseObj = (new JsonParser()).parse(postResponse[1]).getAsJsonObject();
        verificationDataObj.addProperty("customerName", responseObj.get("customerName").getAsString());
        String distict = "";
        try {
          String[] Str = responseObj.get("areaOffice").getAsString().split(" ");
          distict = Str[0];
        } catch (Exception exception) {}
        String customerAddress = "";
        try {
          customerAddress = responseObj.get("customerAddress").getAsString();
        } catch (Exception e) {
          customerAddress = "";
        } 
        verificationDataObj.addProperty("customerAddress", customerAddress);
        verificationDataObj.addProperty("businessUnit", distict + " BUSINESS UNIT");
        String state = "";
        try {
          state = responseObj.get("state").getAsString();
        } catch (Exception e) {
          state = "";
        } 
        verificationDataObj.addProperty("state", state);
        verificationDataObj.addProperty("minimumPurchase", "");
        verificationDataObj.addProperty("customerArrears", "0.0");
        verificationDataObj.addProperty("externalReference", "");
        verificationDataObj.addProperty("undertaking", "");
        verificationDataObj.addProperty("errorCode", "00");
        verificationDataObj.addProperty("responseCode", "00");
        verificationDataObj.addProperty("responseDesc", "Successful");
        verificationDataObj.addProperty("email", "");
        verificationDataObj.addProperty("tariff", "");
        verificationDataObj.addProperty("phoneNumber", "");
        try {
          verificationDataObj.addProperty("tariffIndex", "");
        } catch (Exception exception) {}
        verificationDataObj.addProperty("tariffCode", "");
        verificationDataObj.addProperty("customerType", "");
        verificationDataObj.addProperty("minVendAmount", "");
        verificationDataObj.addProperty("maxVendAmount", "");
      } else {
        verificationDataObj.addProperty("responseCode", "06");
        verificationDataObj.addProperty("responseDesc", "Account number details not found");
      } 
    } catch (Exception e) {
      logger.error("Error : ", e);
      verificationDataObj.addProperty("responseCode", "56");
      verificationDataObj.addProperty("responseDesc", "Account number details not found");
    } 
    result = verificationDataObj.toString();
    return result;
  }
  
  public String doPostPaidTransactionPosting(JsonObject jsonData) {
    String amount = jsonData.get("amount").getAsString();
    String accountOrMeterNo = jsonData.get("accountNumber").getAsString();
    String uniqueTransId = jsonData.get("uniqueTransId").getAsString();
    String district = jsonData.get("district").getAsString();
    JsonObject paymentDataObj = new JsonObject();
    paymentDataObj.addProperty("requestType", "payment");
    paymentDataObj.addProperty("disco", "KAD");
    paymentDataObj.addProperty("accountType", "Postpaid");
    paymentDataObj.addProperty("uniqueTransId", jsonData.get("uniqueTransId").getAsString());
    paymentDataObj.addProperty("accountNumber", jsonData.get("accountNumber").getAsString());
    double amt2 = Double.parseDouble(amount);
    try {
      String[] postResponse = this.service.vendPin(accountOrMeterNo, amount, uniqueTransId, tokenId);
      logger.info("RESPONSE CODE ==== " + postResponse[0]);
      if (postResponse[0].equalsIgnoreCase("201")) {
        logger.info("KAD POSTPAID PAYMENT RESPONSE : " + postResponse[1]);
        JsonObject responseObj = (new JsonParser()).parse(postResponse[1]).getAsJsonObject();
        JsonObject mainToken = new JsonObject();
        String recieptNumber = responseObj.get("paymentId").getAsString();
        String ModeofPayment = responseObj.get("modeofPayment").getAsString();
        String TypesofPayment = responseObj.get("typesofPayment").getAsString();
        paymentDataObj.addProperty("errorCode", postResponse[0]);
        paymentDataObj.addProperty("externalReference", recieptNumber);
        paymentDataObj.addProperty("responseCode", "00");
        paymentDataObj.addProperty("responseDesc", "Successful");
        paymentDataObj.addProperty("receiptNumber", recieptNumber);
        paymentDataObj.addProperty("units", "");
        paymentDataObj.addProperty("vat", "");
        paymentDataObj.addProperty("amount", amount);
        paymentDataObj.addProperty("undertaking", "");
        paymentDataObj.addProperty("customerName", "");
        paymentDataObj.addProperty("customerArrears", "");
        paymentDataObj.addProperty("businessUnit", district + " businessUnit");
        paymentDataObj.addProperty("responseCode", "00");
        paymentDataObj.addProperty("responseDesc", "Successful");
      } else {
        paymentDataObj.addProperty("errorCode", postResponse[0]);
        paymentDataObj.addProperty("responseCode", "06");
        paymentDataObj.addProperty("responseDesc", "");
      } 
    } catch (Exception ex) {
      logger.error("Payment Failure", ex);
      paymentDataObj.addProperty("errorCode", "Payment Failure");
      paymentDataObj.addProperty("responseCode", "06");
      paymentDataObj.addProperty("responseDesc", "");
    } 
    return paymentDataObj.toString();
  }
  
  public String doPostPaidTransactionReversal(JsonObject jo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public String doPostPaidTransactionRequery(JsonObject jsonData) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public String doPrepaidCustomerInfo(JsonObject jsonData) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public String doPrePaidTransactionPosting(JsonObject jsonData) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public String doPrePaidTransactionReversal(JsonObject jsonData) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public String doPrePaidTransactionRequery(JsonObject jsonData) {
    String accountOrMeterNo = jsonData.get("accountNumber").getAsString();
    JsonObject requeryDataObj = new JsonObject();
    requeryDataObj.addProperty("requestType", "payment");
    requeryDataObj.addProperty("disco", "ABJ");
    requeryDataObj.addProperty("accountType", "requery");
    requeryDataObj.addProperty("uniqueTransId", jsonData.get("uniqueTransId").getAsString());
    requeryDataObj.addProperty("accountNumber", jsonData.get("accountNumber").getAsString());
    try {
      String[] postResponse = this.service.requery(accountOrMeterNo);
      if (postResponse[0].equalsIgnoreCase("201") || postResponse[0].equalsIgnoreCase("200")) {
        JsonObject responseObj = (new JsonParser()).parse(postResponse[1]).getAsJsonObject();
        JsonObject mainToken = new JsonObject();
        String firstToken = responseObj.get("Token").getAsString().replaceAll("-", "");
        String vat = responseObj.get("Vat").getAsString();
        String amt = responseObj.get("Amount").getAsString();
        String unit = responseObj.get("PurchasedUnits").getAsString();
        String respcode = responseObj.get("ResponseCode").getAsString();
        String extRef = responseObj.get("Reference").getAsString();
        mainToken.addProperty("unit", unit);
        mainToken.addProperty("amount", amt);
        mainToken.addProperty("vat", vat);
        mainToken.addProperty("fixedCharge", "");
        mainToken.addProperty("token", firstToken);
        requeryDataObj.addProperty("errorCode", respcode);
        requeryDataObj.addProperty("externalReference", extRef);
        requeryDataObj.add("mainToken", (JsonElement)mainToken);
        requeryDataObj.addProperty("responseCode", "00");
        requeryDataObj.addProperty("responseDesc", "Successful");
      } else {
        requeryDataObj.addProperty("errorCode", postResponse[0]);
        requeryDataObj.addProperty("responseCode", "06");
        requeryDataObj.addProperty("responseDesc", "");
      } 
    } catch (Exception ex) {
      requeryDataObj.addProperty("errorCode", "");
      requeryDataObj.addProperty("responseCode", "06");
      requeryDataObj.addProperty("responseDesc", "");
      logger.error("CustomerInfo Failure", ex);
    } 
    return requeryDataObj.toString();
  }
}


