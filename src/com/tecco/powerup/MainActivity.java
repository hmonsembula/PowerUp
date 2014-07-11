package com.tecco.powerup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.tecco.citypower.R;

public class MainActivity extends FragmentActivity {

	MainLayout mLayout;
	private ListView lvMenu;
	private String[] lvMenuItems;
	Button btMenu;
	TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main, null);
		setContentView(mLayout);

		lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMenu.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lvMenuItems));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}

		});

		btMenu = (Button) findViewById(R.id.button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Vendors fragment = new Vendors();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void toggleMenu(View v) {
		mLayout.toggleMenu();
	}

	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems[position];
		String currentItem = tvTitle.getText().toString();
		if (selectedItem.compareTo(currentItem) == 0) {
			mLayout.toggleMenu();
			return;
		}

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("E-Vendors") == 0) {
			fragment = new Vendors();
		} else if (selectedItem.compareTo("E-Balance") == 0) {
			fragment = new Balance();
		} else if (selectedItem.compareTo("Load Shedding Schedule") == 0) {
			fragment = new LoadShedding();
		} else if (selectedItem.compareTo("Notifications") == 0) {
			fragment = new Notifications();
		} else if (selectedItem.compareTo("Report a fault") == 0) {
			fragment = new FaultReport();
		}

		if (fragment != null) {
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();
			tvTitle.setText(selectedItem);
		}
		mLayout.toggleMenu();
	}

	@Override
	public void onBackPressed() {
		if (mLayout.isMenuShown()) {
			mLayout.toggleMenu();
		} else {
			super.onBackPressed();
		}
	}
}
