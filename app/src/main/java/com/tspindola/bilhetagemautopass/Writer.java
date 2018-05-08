package com.tspindola.bilhetagemautopass;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Writer extends AppCompatActivity {

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);
    }

    @Override
    protected void onResume(){
        super.onResume();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    void writeTag(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                Log.i("hey", Arrays.toString(tag.getTechList()));
                MifareClassic mfc = MifareClassic.get(tag);

                try {
                    mfc.connect();
                    byte[] AuthKey = new BigInteger("FFFFFFFFFFFF", 16).toByteArray();
                    boolean authB = mfc.authenticateSectorWithKeyB(1, MifareClassic.KEY_DEFAULT);
                    Log.i("hey", "authB : " + authB);

                    if (authB) {
                        byte[] bWrite = new byte[16];
                        byte[] hello = "hello".getBytes(StandardCharsets.US_ASCII);
                        System.arraycopy(hello, 0, bWrite, 0, hello.length);
                        mfc.writeBlock(5, bWrite);
                        Log.i("hey", "write : " + Arrays.toString(bWrite));

                        byte[] bRead = mfc.readBlock(5);
                        String str = new String(bRead, StandardCharsets.US_ASCII);
                        Log.i("hey", "read bytes : " + Arrays.toString(bRead));
                        Log.i("hey", "read string : " + str);
                        Toast.makeText(this, "read : " + str, Toast.LENGTH_SHORT).show();
                        Log.i("hey", "expected : " + new String(bWrite, StandardCharsets.US_ASCII));
                    }
                    mfc.close();

                } catch (IOException e) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.i("hey", "Error");
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("hey","onPause called.");
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("hey","onNewIntent called.");
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            //TextView tvInfo = findViewById(R.id.tvInfo);
            String uid = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            //tvInfo.setText("NFC Tag = "+ uid);
            Log.i("hey","UID= "+ uid);

            writeTag(intent);
        }
    }

    private String ByteArrayToHexString(byte [] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }
}
