package com.example.deleguesapp;

import com.example.deleguesapp.manager.DeleguesManager;
import com.example.deleguesapp.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactManuelActivity extends Activity {

	private User userCreated;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactmanuel);
		userCreated = new User();
		final TextView textNom = (TextView) findViewById(R.id.textnom);
		Button boutonNom = (Button) findViewById(R.id.buttonnom);
		final TextView textPrenom = (TextView) findViewById(R.id.textprenom);
		Button boutonPrenom = (Button) findViewById(R.id.buttonprenom);
		final TextView textTel = (TextView) findViewById(R.id.texttel);
		Button boutonTel = (Button) findViewById(R.id.buttontel);
		Button boutonSave = (Button) findViewById(R.id.buttonsave);

		boutonNom.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ContactManuelActivity.this);

				alert.setTitle("Ajouter un eleve");
				alert.setMessage("Rentrez le nom de l'eleve");

				// Set an EditText view to get user input
				final EditText input = new EditText(ContactManuelActivity.this);
				input.setText(textNom.getText());
				if (input.getText().toString().equals("Nom"))
					input.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							input.setText("");
						}
					});
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = input.getText();

								textNom.setText(value);
								userCreated.setName(value.toString());
								// DeleguesManager.getInstance(AppreciationActivity.this).saveUser(eleve);
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.

							}
						});
				alert.show();

			}
		});

		boutonPrenom.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ContactManuelActivity.this);

				alert.setTitle("Ajouter un eleve");
				alert.setMessage("Rentrez le prénom de l'eleve");

				// Set an EditText view to get user input
				final EditText input = new EditText(ContactManuelActivity.this);
				input.setText(textPrenom.getText());
				if (input.getText().toString().equals("Prénom"))
					input.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							input.setText("");
						}
					});
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = input.getText();

								textPrenom.setText(value);
								userCreated.setForname(value.toString());
								// DeleguesManager.getInstance(AppreciationActivity.this).saveUser(eleve);
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.

							}
						});
				alert.show();

			}
		});
		
		boutonTel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ContactManuelActivity.this);

				alert.setTitle("Ajouter un éleve");
				alert.setMessage("Rentrez le numero de telephone de l'éleve");

				// Set an EditText view to get user input
				final EditText input = new EditText(ContactManuelActivity.this);
				input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
				input.setText(textTel.getText());
				input.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						input.setText("");

					}
				});
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = input.getText();
								String phone = value.toString();
								if (phone.length() != 10)
									Toast.makeText(ContactManuelActivity.this,
											"Numero érroné",
											Toast.LENGTH_SHORT).show();
								else {
									userCreated.setPhone(phone);									
									textTel.setText(value);
								}
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();
			}
		});
		boutonSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				String message = "";
				Boolean formOk = true;
				if (userCreated.getName() == null || userCreated.getName().trim().equals("")){
					formOk= false;
					message="nom erroné ";
				}
				else if (userCreated.getForname() == null || userCreated.getForname().trim().equals("")){
					formOk= false;
					message="prénom erroné ";
				}
				else if (userCreated.getPhone() == null){
					formOk= false;
					message="numero erroné ";
				}
				
				if (formOk){
					DeleguesManager.getInstance(ContactManuelActivity.this).saveUser(userCreated);
					Toast.makeText(ContactManuelActivity.this,
							"Utilisateur enregistré",
							Toast.LENGTH_SHORT).show();
					userCreated.setEtat(0);
					finish();
				}
				else
					Toast.makeText(ContactManuelActivity.this,
							message,
							Toast.LENGTH_SHORT).show();
			}
		});
	}
}
