package sttsoft.com.br.mlkit_test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private Button btnRead;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnRead = findViewById(R.id.btn_read);


        btnRead.setOnClickListener(v -> clickRead());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickRead() {
        try {

            File teste = new File(utils.getDiretorioCarga(this, "pdf_teste.pdf"), "");

            ArrayList<Bitmap> mArr = utils.pdfToBitmap(this, teste);

            InputImage iptImg = InputImage.fromBitmap(mArr.get(0), 90);

            BarcodeScanner bScanner = BarcodeScanning.getClient();

            bScanner.process(iptImg).
                    addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(@NonNull List<Barcode> barcodes) {
                            Log.i(TAG, "onSuccess: " + barcodes);

                            for (Barcode barcode: barcodes) {
                                Log.i(TAG, "rawValue: " + barcode.getRawValue());
                                Log.i(TAG, "valueType: " + barcode.getValueType());
                            }

                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "clickRead: ", ex);
        }
    }
    /*
    private void workOne() {
        File teste = new File(utils.getDiretorioCarga(this, "teste.jpg"), "");

        //byte[] bytes = utils.readFileToBytes(teste.getPath());

        Bitmap bmp = BitmapFactory.decodeFile(teste.getPath());

        InputImage iptImg = InputImage.fromBitmap(bmp, 0);

        //InputImage iptImg = InputImage.fromByteArray(bytes, 210, 297, 0, InputImage.IMAGE_FORMAT_YV12);

        BarcodeScanner bScanner = BarcodeScanning.getClient();

        bScanner.process(iptImg).
                addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(@NonNull List<Barcode> barcodes) {
                        Log.i(TAG, "onSuccess: " + barcodes);

                        for (Barcode barcode: barcodes) {
                            Log.i(TAG, "rawValue: " + barcode.getRawValue());
                            Log.i(TAG, "valueType: " + barcode.getValueType());
                        }

                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }*/


}