package com.tspindola.bilhetagemautopass;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import java.util.Locale;

import com.tspindola.bilhetagemautopass.datamodel.*;

public class Reader extends Activity {

    private static final Locale BRAZIL = new Locale("pt","BR");

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

        setContentView(R.layout.activity_reader);

        initDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("State Machine","onResume called.");

        TextView tvNFCActive = findViewById(R.id.textView_explanation);
        TextView tvDateTime = findViewById(R.id.tvTimer);
        TextView tvCompany = findViewById(R.id.tvCompany);
        TextView tvRoute = findViewById(R.id.tvRoute);
        TextView tvVehicle = findViewById(R.id.tvVehicle);

        tvDateTime.setText("(Adicionar hora)");
        SharedPreferences sp = ((App) getApplication()).getSharedPreferences();

        long index = sp.getLong("Company",0);
        tvCompany.setText(dbBaseFunctions.findCompanyById(index).getName());
        index = sp.getLong("Route",0);
        tvRoute.setText(dbBaseFunctions.findRouteById(index).getDescription());
        index = sp.getLong("Vehicle",0);
        tvVehicle.setText("Veículo: " + dbBaseFunctions.findVehicleById(index).getCompanyObjectId());


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
            //TextView tvInfo = findViewById(R.id.tvInfo);
            String uid = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            //tvInfo.setText("NFC Tag = "+ uid);
            Log.i("NFC Tag",uid);

            processTransaction(uid);
        }
    }

    private String ByteArrayToHexString(byte [] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }

    public void initDatabase() {
        dbBaseFunctions.start();

        initPersonDatabase();
        initCardDatabase();
        initFeeDatabase();
        initCompanyDatabase();
        initRouteDatabase();
        initVehicleDatabase();
    }

    private void initPersonDatabase() {
        dbBaseFunctions.addPerson(1,"Thiago Spíndola","13222852766",
                "27999260330","","thiago.spindola@autopass.com.br");
        dbBaseFunctions.addPerson(2,"Renato Mattos","99999999999",
                "11123456789","","renato.mattos@autopass.com.br");
    }

    private void initCardDatabase() {
        dbBaseFunctions.addCard(1,"E2662043","Comum");
        dbBaseFunctions.addCard(2,"340848BB","Comum");
        dbBaseFunctions.updateCreditsFromCard(1,100.0);
        dbBaseFunctions.updateCreditsFromCard(2,80.0);
    }

    private void initFeeDatabase() {
        dbBaseFunctions.addFee(1,4.00);
    }

    private void initCompanyDatabase() {
        dbBaseFunctions.addCompany(1,"Urubupungá");
        dbBaseFunctions.addCompany(2,"CPTM");
    }

    private void initRouteDatabase(){
        dbBaseFunctions.addRoute(1,"Vila Olimpia X Pinheiros",12.1,100,1,1);
        dbBaseFunctions.addRoute(2,"Vila Olimpia x Sorocaba",110,12,2,1);
    }

    private void initVehicleDatabase(){
        dbBaseFunctions.addVehicle(1,"Ônibus","1234",1999,1);
        dbBaseFunctions.addVehicle(2,"Trem-Bala","0001",2018,2);
    }

    private void processTransaction(String uid){
        //TODO: Tornar estes dados configuráveis
        //int route_id = 1;
        //int vehicle_id = 1;
        int fee_id = 1;

        Card c = dbBaseFunctions.findCardByUid(uid);
        Double amount = dbBaseFunctions.findFeeById(fee_id).getValue();
        Double credits = c.getCredits();

        if(credits >= amount)
        {
            credits = credits - amount;
            String name = dbBaseFunctions.findPersonById(c.getPersonID()).getName();

            dbBaseFunctions.updateCreditsFromCard(c.getId(),credits);
            String temp = String.format(BRAZIL,"%1$,.2f", credits);

            TextView tvInfo = findViewById(R.id.tvInfo);
            tvInfo.setText("Bem vindo, "+name + ". Seu novo saldo é de R$"+ temp +".");
        }
    }
}