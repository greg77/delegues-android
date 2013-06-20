package com.example.deleguesapp;

import com.example.deleguesapp.adapter.EleveAdapter;
import com.example.deleguesapp.manager.DataManager;
import com.example.deleguesapp.manager.DeleguesManager;
import com.example.deleguesapp.model.User;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView listEleves;
	private EleveAdapter adapter;
	static final int PICK_CONTACT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button addEleveByRepertoire = (Button) findViewById(R.id.buttonaddeleverep);
		addEleveByRepertoire.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ContactRepertoireActivity.class);
				startActivity(intent);
			}
		});
		Button addEleveManuel = (Button) findViewById(R.id.buttonaddelevemanuel);
		addEleveManuel.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						ContactManuelActivity.class);
				startActivity(intent);
			}
		});
		listEleves = (ListView) findViewById(R.id.listEleves);
		adapter = new EleveAdapter(this);
		// View view= this.getLayoutInflater().inflate(R.layout.footerview_main,
		// null);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		Button footerClearAll = new Button(this);

		footerClearAll.setText("clear");
		footerClearAll.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(
						MainActivity.this);

				alert.setTitle("Confirmation");
				alert.setMessage("Voulez vous vraiment reinitialiser les données de tous les eleves?"
						+ "Cette action est irréversible");

				alert.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								int nb = adapter.clearData();
								Toast.makeText(MainActivity.this,
										"Nombre d'éleves modifiés: " + nb,
										Toast.LENGTH_SHORT).show();
							}

						});

				alert.setNegativeButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();

			}
		});

		Button footerDeleteAll = new Button(this);

		footerDeleteAll.setText("delete");
		layout.addView(footerDeleteAll);
		layout.addView(footerClearAll);
		listEleves.addFooterView(layout);
		listEleves.setAdapter(adapter);

		listEleves.setOnItemClickListener(this);
		listEleves.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		menu.setHeaderTitle("Options");
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals("Reset")) {
			adapter.clearEleveData(DataManager.getInstance().getUser());
			Toast.makeText(MainActivity.this, "mis a jour", Toast.LENGTH_LONG)
					.show();
		} else if (item.getTitle().equals("Supprimer")) {
			adapter.deleteEleve(DataManager.getInstance().getUser());
			// listEleves.invalidateViews();
			adapter.loadData();
			// adapter.notifyDataSetChanged();
			Toast.makeText(MainActivity.this, "Supprimé", Toast.LENGTH_LONG)
					.show();
		} else if (item.getTitle().equals("Editer")) {
			final User eleve = DataManager.getInstance().getUser();
			AlertDialog.Builder alert = new AlertDialog.Builder(
					MainActivity.this);

			alert.setTitle("Editer un eleve");

			// Set an EditText view to get user input
			LinearLayout lila1= new LinearLayout(this);
			lila1.setOrientation(1);
			final EditText inputNom = new EditText(MainActivity.this);
			final EditText inputPrenom = new EditText(MainActivity.this);
			final EditText inputTel = new EditText(MainActivity.this);
			inputTel.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			if (eleve.getName() != null)
				inputNom.setText(eleve.getName());
			else
				inputNom.setText("Nom");
			if (inputNom.getText().toString().equals("Nom"))
				inputNom.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						inputNom.setText("");
					}
				});
			if (eleve.getForname() != null)
				inputPrenom.setText(eleve.getForname());
			else
				inputPrenom.setText("Prenom");
			if (inputPrenom.getText().toString().equals("Prenom"))
				inputPrenom.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						inputPrenom.setText("");
					}
				});
			if (eleve.getPhone() != null)
				inputTel.setText(eleve.getPhone());
			else
				inputTel.setText("Telephone");
			if (inputTel.getText().toString().equals("Telephone"))
				inputTel.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						inputTel.setText("");
					}
				});
			lila1.addView(inputNom);
			lila1.addView(inputPrenom);
			lila1.addView(inputTel);
			alert.setView(lila1);
			

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							Editable valueName = inputNom.getText();
							Editable valuePrenom = inputPrenom.getText();
							Editable valueTel = inputTel.getText();
							eleve.setName(valueName.toString());
							eleve.setForname(valuePrenom.toString());
							eleve.setPhone(valueTel.toString());
							
							DeleguesManager.getInstance(
									MainActivity.this).saveUser(eleve);
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

		} else {
			return false;
		}
		return true;
	}

	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		User eleve = (User) adapter.getItemAtPosition(position);
		Intent intent = new Intent(this, AppreciationActivity.class);
		DataManager.getInstance().setUser(eleve);
		startActivity(intent);
		Log.d("elevehgh", eleve.getName());

	}

	@Override
	protected void onResume() {
		adapter.loadData();
		super.onResume();
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// TODO Whatever you want to do with the selected contact
					// name.
				}
			}
			break;
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		User eleve = (User) arg0.getItemAtPosition(position);
		DataManager.getInstance().setUser(eleve);
		Log.d("user", eleve.getForname());
		registerForContextMenu(arg0);
		arg0.showContextMenu();

		return true;
	}

}
