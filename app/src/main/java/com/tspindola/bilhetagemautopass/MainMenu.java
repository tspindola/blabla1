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
import java.util.List;
import java.util.Locale;

import com.tspindola.bilhetagemautopass.datamodel.*;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MainMenu extends Activity {

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

        setContentView(R.layout.activity_main_menu);

        initDatabase();
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

    private void initDatabase() {
        BoxStore boxStore = ((App) getApplication()).getBoxStore();

        initPersonDatabase(boxStore);
        initCardDatabase(boxStore);
        initFeeDatabase(boxStore);
        initCompanyDatabase(boxStore);
        initRouteDatabase(boxStore);
        initVehicleDatabase(boxStore);
    }

    //TODO: Deletar os objetos criados nas funções de inicialização
    //TODO: Mudar os relations para private

    private void initPersonDatabase(BoxStore boxStore) {
        Box<Person> personBox = boxStore.boxFor(Person.class);

        if(personBox.count() == 0) {
            Person p = new Person();

            p.setId(1);
            p.setName("Thiago Spíndola");
            p.setCpf("13222852766");
            p.setEmail("thiago.spindola@autopass.com.br");
            personBox.put(p);

            p.setId(2);
            p.setName("Renato Mattos");
            p.setCpf("99999999999");
            p.setEmail("renato.mattos@autopass.com.br");
            personBox.put(p);
        }
    }

    private void initCardDatabase(BoxStore boxStore) {
        Box<Card> cardBox = boxStore.boxFor(Card.class);
        if(cardBox.count()==0) {
            Card c = new Card();

            c.setId(1);
            c.setType("Comum");
            c.setUid("E2662043");
            c.setCredits(40.00);
            c.person.setTargetId(1);
            cardBox.put(c);

            c.setId(2);
            c.setType("Comum");
            c.setUid("340848BB");
            c.setCredits(100.00);
            c.person.setTargetId(2);
            cardBox.put(c);
        }
    }

    private void initFeeDatabase(BoxStore boxStore) {
        Box<Fee> feeBox = boxStore.boxFor(Fee.class);
        if(feeBox.count()==0) {
            Fee f = new Fee();

            f.setId(1);
            f.setValue(4.00);
            feeBox.put(f);
        }
    }

    private void initCompanyDatabase(BoxStore boxStore) {
        Box<Company> companyBox = boxStore.boxFor(Company.class);
        if(companyBox.count()==0) {
            Company c = new Company();

            c.setId(1);
            c.setName("Urubupungá");
            companyBox.put(c);

            c.setId(2);
            c.setName("CPTM");
            companyBox.put(c);
        }
    }

    private void initRouteDatabase(BoxStore boxStore){
        Box<Route> routeBox = boxStore.boxFor(Route.class);
        if(routeBox.count() == 0) {
            Route r = new Route();

            r.setId(1);
            r.setDescription("Vila Olimpia X Pinheiros");
            r.setCapacity(100);
            r.setLength(12.1);
            r.company.setTargetId(1);
            r.fee.setTargetId(1);
            routeBox.put(r);

            r.setId(2);
            r.setDescription("Vila Olimpia x Sorocaba");
            r.setCapacity(12);
            r.setLength(110);
            r.company.setTargetId(2);
            r.fee.setTargetId(1);
            routeBox.put(r);
        }
    }

    private void initVehicleDatabase(BoxStore boxStore){
        Box<Vehicle> vehicleBox = boxStore.boxFor(Vehicle.class);
        if(vehicleBox.count() == 0) {
            Vehicle v = new Vehicle();

            v.setId(1);
            v.setType("Ônibus");
            v.setCompanyObjectId("1234");
            v.setManufactureYear(1999);
            v.company.setTargetId(1);
            vehicleBox.put(v);

            v.setId(2);
            v.setType("Trem-Bala");
            v.setCompanyObjectId("0001");
            v.setManufactureYear(2018);
            v.company.setTargetId(2);
            vehicleBox.put(v);
        }
    }

    private void processTransaction(String uid){
        //TODO: Tornar estes dados configuráveis
        int route_id = 1;
        int vehicle_id = 1;

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        Box<Card> cardBox = boxStore.boxFor(Card.class);
        Box<Fee> feeBox = boxStore.boxFor(Fee.class);
        Box<Route> routeBox = boxStore.boxFor(Route.class);

        Card c = cardBox.query().equal(Card_.uid, uid.toUpperCase()).build().findFirst();
        Route r = routeBox.query().equal(Route_.id,route_id).build().findFirst();
        Double amount = r.fee.getTarget().getValue();
        Double credits = c.getCredits();

        if(credits >= amount)
        {
            credits = credits - amount;
            String name = c.person.getTarget().getName();

            c.setCredits(credits);
            cardBox.put(c);
            String temp = String.format(BRAZIL,"%1$,.2f", credits);

            TextView tvInfo = findViewById(R.id.tvInfo);
            tvInfo.setText("Bem vindo, "+name + ". Seu novo saldo é de R$"+ temp +".");
        }
    }
}