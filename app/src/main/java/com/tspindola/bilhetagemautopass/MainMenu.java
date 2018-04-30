package com.tspindola.bilhetagemautopass;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigInteger;
import com.tspindola.bilhetagemautopass.datamodel.*;

public class MainMenu extends Activity {

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
        Log.i("State Machine","onCreate called.");
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("State Machine","onResume called.");

        // adjusting the screen elements
        TextView tvNFCActive = findViewById(R.id.textView_explanation);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(null == nfcAdapter)
        {
            Toast.makeText(this, R.string.nfc_not_found, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (nfcAdapter.isEnabled()) {
            tvNFCActive.setTextColor(Color.GREEN);
            tvNFCActive.setText(R.string.explanation);
        } else {
            tvNFCActive.setTextColor(Color.RED);
            tvNFCActive.setText(R.string.nfc_inactive);
        }

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

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("State Machine","onPause called.");
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("State Machine","onNewIntent called.");
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            TextView tvInfo = findViewById(R.id.tvInfo);
            String uid = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            tvInfo.setText("NFC Tag = "+ uid);
            Log.i("NFC Tag",uid);
        }
    }

    private String ByteArrayToHexString(byte [] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }
}