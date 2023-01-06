package com.example.demo.Security.Auth2FA;

import com.example.demo.Security.User;
import com.example.demo.Security.UserPrincipal;
import com.example.demo.Services.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class GoogleAuthController {

    @Autowired
    private GoogleAuthenticator gAuth;

    @Autowired
    private UserService userService;

    @GetMapping("/user/enable2FA")
    public String enable2fa(){

     User loggedUser = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());

     if(loggedUser.getUseMFA()){
         return "redirect:/user/showProfile";
     }

//    loggedUser.setUseMFA(false);
//    loggedUser.setGoogle2FaRequired(true);
    //userService.saveUser(loggedUser);
    return "redirect:/user/getQRCode";
    }


    @GetMapping("/user/getQRCode")
    public String generate(Model model) throws IOException, WriterException {


        String issuer = "IT-Knowledge";
        String OtpAuthTotpURL;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.findUser(username);
        String secret = loggedUser.getSecret();


        if (loggedUser.getSecret() == null) {

            final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
            OtpAuthTotpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, username, key);
            loggedUser.setSecret(key.getKey());
            loggedUser.setUseMFA(false);
            userService.saveUser(loggedUser);
        } else {

            OtpAuthTotpURL = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                    issuer, loggedUser.getUsername(), loggedUser.getSecret(), issuer);
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(OtpAuthTotpURL, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        byte[] pngData = outputStream.toByteArray();
        outputStream.close();

        ValidationObject validationObject = new ValidationObject();

        model.addAttribute(("image"), Base64.getEncoder().encodeToString(pngData));

        return "qrCodeConfirm";
    }

    @PostMapping("/user/validateCode")
    public String validateCode(@RequestParam("verifyCode") Integer verifyCode) {

        //GoogleAuthenticator gAuth = new GoogleAuthenticator();
        User loggedUser = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        if (gAuth.authorize(loggedUser.getSecret(), verifyCode)) {
            loggedUser.setUseMFA(true);
            loggedUser.setGoogle2FaRequired(false);
            userService.saveUser(loggedUser);
            return "redirect:/user/showProfile";
        }

        return "redirect:/user/getQrCode";
    }

    @GetMapping("/user/verify2FA")
    public String verify(){
        return "Hellow";
    }

    @PostMapping("/user/verify2FA") //verification of google code
    public String verify2FA(@RequestParam ("verifyCode") Integer verifyCode){

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(gAuth.authorizeUser(userPrincipal.getUsername(), verifyCode)){ // evtl only authorize + secret + code
            ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setGoogle2FaRequired(false);

            return "redirect:/user/folders";
        } else {

            return "redirect:/user/verify2FA";
        }
    }
}
