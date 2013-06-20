package com.example.deleguesapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleguesapp.manager.DataManager;
import com.example.deleguesapp.manager.DeleguesManager;
import com.example.deleguesapp.model.User;
import com.example.deleguesapp.widget.SegmentedControlButton;

public class AppreciationActivity extends Activity implements OnClickListener {
	private User eleve;
	private TextView textUser;
	private TextView textMoyenne;
	private Button boutonMoyenne;
	private Button boutonSms;
	private RadioGroup avisGroup;
	private SegmentedControlButton ncRButton;
	private SegmentedControlButton dfpRButton;
	private SegmentedControlButton favRbutton;
	private SegmentedControlButton tfavRButton;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciation);
		this.eleve = DataManager.getInstance().getUser();
		textUser = (TextView) findViewById(R.id.textView1);
		textMoyenne = (TextView) findViewById(R.id.textView2);
		boutonMoyenne = (Button) findViewById(R.id.button1);

		boutonSms = (Button) findViewById(R.id.buttonsms);

		avisGroup = (RadioGroup) findViewById(R.id.group_avis);
		ncRButton = (SegmentedControlButton) findViewById(R.id.avis_option_nc);
		dfpRButton = (SegmentedControlButton) findViewById(R.id.avis_option_dsp);
		favRbutton = (SegmentedControlButton) findViewById(R.id.avis_option_fav);
		tfavRButton = (SegmentedControlButton) findViewById(R.id.avis_option_t_fav);
		
			
		boutonSms.setOnClickListener(this);
		boutonMoyenne.setOnClickListener(this);
		
		
		textUser.setText(eleve.getForname()+" "+eleve.getName()+" ("+eleve.getPhone()+")");
		ncRButton.setChecked(true);		
		
		if (eleve.getMoyG() == null)
			eleve.setMoyG(new Float(0));
		
		float moyenneGenerale = eleve.getMoyG();
		if (moyenneGenerale != 0)				
			textMoyenne.setText(Float.toString(moyenneGenerale));		
		


		
		
	}
	
	
	
	private void sendSMS(String phoneNumber, String message)
	{
	
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
	new Intent(SENT), 0);

	PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
	new Intent(DELIVERED), 0);

	//—when the SMS has been sent—
	registerReceiver(new BroadcastReceiver(){
	@Override
	public void onReceive(Context arg0, Intent arg1) {
	switch (getResultCode())
	{
	case Activity.RESULT_OK:
	Toast.makeText(getBaseContext(), "SMS envoyé", 
	Toast.LENGTH_SHORT).show();
	eleve.setEtat(1);
	DeleguesManager.getInstance(AppreciationActivity.this).saveUser(eleve);
	break;
	case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	Toast.makeText(getBaseContext(), "Generic failure", 
	Toast.LENGTH_SHORT).show();
	break;
	case SmsManager.RESULT_ERROR_NO_SERVICE:
	Toast.makeText(getBaseContext(), "No service", 
	Toast.LENGTH_SHORT).show();
	break;
	case SmsManager.RESULT_ERROR_NULL_PDU:
	Toast.makeText(getBaseContext(), "Null PDU", 
	Toast.LENGTH_SHORT).show();
	break;
	case SmsManager.RESULT_ERROR_RADIO_OFF:
	Toast.makeText(getBaseContext(), "Erreur: mode avion activé", 
	Toast.LENGTH_SHORT).show();
	break;
	}
	}
	}, new IntentFilter(SENT));

	//—when the SMS has been delivered—
	registerReceiver(new BroadcastReceiver(){
	@Override
	public void onReceive(Context arg0, Intent arg1) {
	switch (getResultCode())
	{
	case Activity.RESULT_OK:
	Toast.makeText(getBaseContext(), "SMS recu", 
	Toast.LENGTH_SHORT).show();
	break;
	case Activity.RESULT_CANCELED:
	Toast.makeText(getBaseContext(), "SMS non recu", 
	Toast.LENGTH_SHORT).show();
	break; 
	}
	}
	}, new IntentFilter(DELIVERED));
	SmsManager smsManager = SmsManager.getDefault();
	ArrayList<String> messageArray = smsManager.divideMessage(message);
	ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
	for (int i = 0; i < messageArray.size(); i++)
		sentIntents.add(sentPI);
	ArrayList<PendingIntent> deliveredIntents = new ArrayList<PendingIntent>();
	for (int i = 0; i < messageArray.size(); i++)
		deliveredIntents.add(deliveredPI);
	

	smsManager.sendMultipartTextMessage(phoneNumber, null, messageArray, sentIntents, deliveredIntents);
	}



	public void onClick(View v) {
		if (v.getId() == R.id.button1){
			insertMoyenne();
		}

		else if (v.getId() == R.id.buttonsms) {
			prepareSms();
		}
		
	}



	private void prepareSms() {
		String message = "Salut "+eleve.getForname()+" "+eleve.getName()+ "! ";
		message += "Tu as une moyenne generale de "+ eleve.getMoyG()+". ";	
	int selectedRadio = (avisGroup.getCheckedRadioButtonId());
		if (selectedRadio != ncRButton.getId()){
			if (selectedRadio == dfpRButton.getId())
				message += " Avis BTS : Doit faire ses preuves.";
			else if (selectedRadio == favRbutton.getId()) 
				message += "Avis BTS : Favorable.";
			else if (selectedRadio == tfavRButton.getId())
				message += "Avis BTS : Tres favorable.";			
		}
		message += "Pour information, 25% des eleves ont un avis très favorable, 36% un avis favorable, et 39% doivent faire leurs preuves. La date de signature des livrets scolaires sera communiquée ultérieurement. ";
		
		sendSMS(eleve.getPhone(), message);
		
	}



	


	private void insertMoyenne() {
		AlertDialog.Builder alert = new AlertDialog.Builder(AppreciationActivity.this);

		alert.setTitle("Editer la moyenne");
		alert.setMessage("Rentrez la moyenne de l'eleve");

		// Set an EditText view to get user input 
		final EditText input = new EditText(AppreciationActivity.this);
		input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);				
		input.setText(textMoyenne.getText());
					
		input.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				input.setText("");
				
			}
		});
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  Editable value = input.getText();
		  float newMoyenne = Float.valueOf(value.toString());
		  if (newMoyenne<0 || newMoyenne>20)
			  Toast.makeText(AppreciationActivity.this, "Moyenne non valide", Toast.LENGTH_SHORT).show();
		  else{
		  eleve.setMoyG(newMoyenne);
		  DeleguesManager.getInstance(AppreciationActivity.this).saveUser(eleve);
		  textMoyenne.setText(value);
		  }
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
		
	} 

}
