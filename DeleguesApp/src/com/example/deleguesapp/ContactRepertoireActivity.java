package com.example.deleguesapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleguesapp.adapter.ContactAdapter;
import com.example.deleguesapp.adapter.ContactAdapter.ViewHolder;
import com.example.deleguesapp.manager.DeleguesManager;
import com.example.deleguesapp.model.Contact;
import com.example.deleguesapp.model.User;
import com.example.deleguesapp.util.StringMatcher;
import com.example.deleguesapp.widget.IndexableListView;

public class ContactRepertoireActivity extends Activity {
	private IndexableListView listContacts;
	private ArrayList<Contact> contacts;

	private boolean listEmpty = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactrepertoire);
		loadData();
		final ContentAdapter adapter = new ContentAdapter(this,
				android.R.layout.simple_list_item_2, contacts);
		listContacts = (IndexableListView) findViewById(R.id.listContact);
		listContacts.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				// TODO Auto-generated method stub
				final Contact contact = (Contact) listContacts
						.getItemAtPosition(position);
				if (contact.getPhoneToUse() != null) {
					contact.toggleChecked();
					ViewHolder viewHolder = (ViewHolder) item.getTag();
					viewHolder.getCheckBox().setChecked(contact.isChecked());
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ContactRepertoireActivity.this);

					builder.setTitle(contact.getForname());
					// builder.setMessage("Selectionnez un numero de telephone");
					if (contact.getPhones().size()!=0){
					List<String> list = contact.getPhones();

					final CharSequence[] items = list
							.toArray(new CharSequence[list.size()]);

					builder.setSingleChoiceItems(items, 2,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									Toast.makeText(
											ContactRepertoireActivity.this,
											items[which], Toast.LENGTH_SHORT)
											.show();

								}

							});
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									ListView lw = ((AlertDialog) dialog)
											.getListView();
									String checkedItem = (String) lw
											.getAdapter()
											.getItem(
													lw.getCheckedItemPosition());
									contact.setPhoneToUse(checkedItem);
									adapter.notifyDataSetChanged();
								}
							});
					}
					else{
						builder.setMessage("Veuillez rentrer le numero de telephone");
						final EditText input = new EditText(ContactRepertoireActivity.this);
						input.setRawInputType(InputType.TYPE_CLASS_NUMBER);				
						
						input.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								input.setText("");								
							}
						});
						builder.setView(input);

						builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  Editable value = input.getText();
						  String telephone = String.valueOf(value.toString());
						  if (telephone.length()!=10)
							  Toast.makeText(ContactRepertoireActivity.this, "Numero de telephone non valide", Toast.LENGTH_SHORT).show();
						  else{
						  contact.addPhone(telephone);
						  contact.setPhoneToUse(telephone);
						  
						  }
						  }
						});

					}
				
					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});
					
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});
		listContacts.setAdapter(adapter);
		listContacts.setFastScrollEnabled(true);
		Button importContactButton = (Button) findViewById(R.id.buttonImportContact);
		Button retourButton = (Button) findViewById(R.id.buttonRetour);
		retourButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		importContactButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int iterationContacts = 0;
				
				for (Contact contact : contacts) {
					
					if (contact.isChecked()){
						User user = new User();
						user.setName(contact.getName());
						user.setForname(contact.getForname());
						user.setPhone(contact.getPhoneToUse());
						user.setEtat(0);
						DeleguesManager.getInstance(ContactRepertoireActivity.this).saveUser(user);
						iterationContacts++;
					}
					
				}
				Toast.makeText(ContactRepertoireActivity.this, "Nombre de contacts importés: "+iterationContacts, Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

	public void loadData() {

		contacts = new ArrayList<Contact>();
		ContentResolver connectApp = this.getContentResolver();
		Cursor cur = connectApp.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				Contact contact = new Contact();

				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				contact.setId((long) Integer.parseInt(id));
				String[] names = cur
						.getString(
								cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
						.split(" ");
				String forname = "";
				if (names.length != 0)
					forname = names[0];
				contact.setForname(names[0]);

				String name = "";
				if (names.length >= 2)
					name = names[1];
				contact.setName(name);
				// mItems.add(name);
				// recherche de tous les numeros d'un contact
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = connectApp.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					int iterPhone = 0;
					while (pCur.moveToNext()) {
						// Do something with phones
						String number = pCur.getString(pCur
								.getColumnIndex(Phone.NUMBER));

						number = number.replaceAll(" ", "");
						number = number.replace("+33", "0");
						Log.d("elevenum", number + "/ nom:" + name
								+ "/prenom: " + forname);
						if (number.startsWith("06") || number.startsWith("07") ){
							contact.addPhone(number);
							iterPhone++;
						}
						
					}
					if (iterPhone == 1)
						contact.setPhoneToUse(contact.getPhones().get(0));
					else {
						ArrayList<String> phonesContact = contact.getPhones();
						removeDuplicate(phonesContact);
						if (phonesContact.size()==1)
							contact.setPhoneToUse(phonesContact.get(0));
					}
					pCur.close();
				}

				contacts.add(contact);
			}
		}
		Collections.sort(contacts);
		listEmpty = false;
		// notifyDataSetChanged();

	}
	public static void removeDuplicate(ArrayList arlList)
	  {
	   HashSet h = new HashSet(arlList);
	   arlList.clear();
	   arlList.addAll(h);
	  }
	private static class ViewHolder {
		CheckBox checkbox;

		public ViewHolder(CheckBox ck) {
			checkbox = ck;
		}

		public CheckBox getCheckBox() {
			return checkbox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkbox = checkBox;
		}

	}

	private class ContentAdapter extends BaseAdapter implements SectionIndexer {

		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private List<Contact> contactsList;
		private LayoutInflater inflater;

		public ContentAdapter(Context context, int textViewResourceId,
				List<Contact> objects) {
			// super(context, textViewResourceId, objects);
			super();
			contactsList = objects;
			inflater = LayoutInflater.from(ContactRepertoireActivity.this);

		}

		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be
			// selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							String value = String.valueOf(getItem(j)
									.getForname().charAt(0));
							String key = String.valueOf(k);
							if (StringMatcher.match(value, key))
								return j;
						}
					} else {
						if (StringMatcher.match(String.valueOf(getItem(j)
								.getForname().charAt(0)), String
								.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		public int getCount() {
			return contactsList.size();
		}

		public Contact getItem(int position) {
			return contactsList.get(position);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			Contact contact = contactsList.get(position);
			CheckBox checkbox;
			if (convertView == null) {
				convertView = (LinearLayout) inflater.inflate(
						R.layout.list_contacts_item, null);
				checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
				viewHolder = new ViewHolder(checkbox);
				convertView.setTag(viewHolder);
				checkbox.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						CheckBox cb = (CheckBox) arg0;
						Contact contact = (Contact) cb.getTag();
						if (contact.getPhoneToUse() == null) {
							Toast.makeText(ContactRepertoireActivity.this,
									"pas de plop", Toast.LENGTH_LONG).show();
						}
						contact.setChecked(cb.isChecked());
					}
				});

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				checkbox = viewHolder.getCheckBox();
			}
			if (viewHolder != null) {
				String displayName = "";
				if (contact.getForname() != null)
					displayName += contact.getForname()+" ";
				if (contact.getName() != null)
					displayName += contact.getName();
				// viewHolder.checkbox.setText(displayName+ " tel:"+
				// contact.getPhoneToUse());
				viewHolder.checkbox.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(ContactRepertoireActivity.this, "plop",
								Toast.LENGTH_SHORT).show();
					}
				});

				viewHolder.checkbox.setChecked(contact.isChecked());
				viewHolder.checkbox.setText(displayName + " tel:"
						+ contact.getPhoneToUse());
			}
			return convertView;
		}

		public int getSectionForPosition(int position) {
			return 0;
		}

		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}

		public long getItemId(int position) {
			return 0;
		}

	}

}
