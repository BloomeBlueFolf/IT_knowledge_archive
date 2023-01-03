package com.example.demo.Security.Auth2FA;

import com.example.demo.Security.User;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class CodeController {

    @Autowired
    private GoogleAuthenticator gAuth;

    @Autowired
    private UserService userService;


    @GetMapping("/user/generateQRCode")
    public String generate(@RequestParam ("username") String username,
                         Model model) throws IOException, WriterException {


        String issuer = "IT-Knowledge";
        String OtpAuthTotpURL;

        User loggedUser = userService.findUser(username);
        String secret = loggedUser.getSecret();


        if(loggedUser.getSecret() == null) {

            final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
            OtpAuthTotpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, username, key);
            loggedUser.setSecret(key.getKey());
            userService.saveUser(loggedUser);
        } else {
            OtpAuthTotpURL = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                    issuer, loggedUser.getUsername(), loggedUser.getSecret(), issuer);
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        System.out.println(loggedUser.getSecret());

        BitMatrix bitMatrix = qrCodeWriter.encode(OtpAuthTotpURL, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        byte[] pngData = outputStream.toByteArray();
        outputStream.close();

        ValidationObject validationObject = new ValidationObject();

        model.addAttribute(("image"), Base64.getEncoder().encodeToString(pngData));
        model.addAttribute(("key"), secret);
        model.addAttribute(("path"), OtpAuthTotpURL);
        validationObject.setUsername(username);
        model.addAttribute(("validationObject"), validationObject);

        return "qRCode";
    }

    @PostMapping("/user/validate")
    public String validateKey(@ModelAttribute ("ValidationObject") ValidationObject validationObject) {

        int password = Integer.parseInt(validationObject.getPassword());
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        User loggedUser = userService.findUser(validationObject.getUsername());
        if(gAuth.authorize(loggedUser.getSecret(), password)){
            return "valid";
        } else {
            return "invalid";
        }
    }
}
