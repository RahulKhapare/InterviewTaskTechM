package com.rak_developer.interviewtaskapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rak_developer.interviewtaskapplication.R;
import com.rak_developer.interviewtaskapplication.databinding.ActivityMainBinding;
import com.rak_developer.interviewtaskapplication.util.AESOperation;
import com.rak_developer.interviewtaskapplication.util.MD5Operation;
import com.rak_developer.interviewtaskapplication.util.RSAOperation;
import com.rak_developer.interviewtaskapplication.util.SHA256Operation;
import com.rak_developer.interviewtaskapplication.util.ToastOperation;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity = this;
    private ActivityMainBinding binding;

    private String username = "Rahul Khapare";
    private String password = "Rahul@123";

    private String encrypted_username = "";
    private String encrypted_password = "";

    private RSAOperation rsa = new RSAOperation();
    private MD5Operation md5 = new MD5Operation();
    private SHA256Operation sha256 = new SHA256Operation();
    private AESOperation aes = new AESOperation();
    private ToastOperation toast = new ToastOperation();

    private boolean isEncrypted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void performEncryption(View v) {
        encryptOperation();
    }

    public void performDecryption(View v) {
        if (isEncrypted) {
            decryptOperation();
        } else {
            toast.showToast(activity, "Please perform encryption operation first.");
        }
    }

    private void encryptOperation() {
        encrypted_username = encryptValue(username);
        encrypted_password = encryptValue(password);
        isEncrypted = true;
        setEncryptedData();
    }

    private void setEncryptedData() {
        binding.txtEncryptedData.setText("Encrypted Username :\n" + encrypted_username + "\n\nEncrypted Password : \n" + encrypted_password);
    }

    synchronized private String encryptValue(String value) {
        String returnValue = "";
        try {
            //RSA Encryption
            String rasEncryptionString = rsa.Encrypt(value);
            Log.e("TAG", "rasEncryptionString: " + rasEncryptionString);

            //Hash string using MD5
            String md5HashString = md5.encryptHashString(rasEncryptionString);
            Log.e("TAG", "md5HashStringEn: " + md5HashString);

            //Hash string using SHA256
//            String sha256HashString = sha256.getSha256Hash(rasEncryptionString);
//            Log.e("TAG", "sha256HashStringEn: " + sha256HashString);

            //AES Encryption of hash string by MD5
            String aesEncryption = aes.encryptKey(md5HashString);
            Log.e("TAG", "aesEncryption: " + aesEncryption);

            //Encrypted return string
            returnValue = aesEncryption;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        }
        return returnValue;
    }

    private void decryptOperation() {
        setDecryptedData(decryptValue(encrypted_username), decryptValue(encrypted_password));
    }

    private void setDecryptedData(String decrypted_username, String decrypted_password) {
        binding.txtDecryptedData.setText("Decrypted Username :\n" + decrypted_username + "\n\nDecrypted Password : \n" + decrypted_password);
    }


    synchronized private String decryptValue(String value) {
        String returnValue = "";
        try {
            //AES Decryption of hash value
            String aesDecryption = aes.decryptKey(value);
            Log.e("TAG", "aesDecryption: " + aesDecryption);

            //Hash string MD5
            String md5HashString = aesDecryption;
            Log.e("TAG", "md5HashStringDe: " + md5HashString);

            //Hash string SHA256
//            String sha256HashString = aesDecryption;
//            Log.e("TAG", "sha256HashStringDe: " + sha256HashString);

            //RSA Decryption
            String rasDecryptionString = rsa.Decrypt(md5HashString);
            Log.e("TAG", "rasDecryptionString: " + rasDecryptionString);

            //Decrypted return string
            returnValue = rasDecryptionString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            toast.showToast(activity, e.getMessage());
        }
        return returnValue;
    }

}