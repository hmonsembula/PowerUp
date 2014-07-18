package com.tecco.powerup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.tecco.powerup.adapter.PowerUpListAdapter;

public class LoadShedding extends Fragment implements OnClickListener {

	private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
	private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

	private PowerUpListAdapter listAdapter;
	private ExpandableListView myList;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_load_shedding, container,
				false);

		
		
		Spinner spinner = (Spinner) view.findViewById(R.id.department);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				container.getContext(), R.array.dept_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		// Just add some data to start with
		loadData();

		// get reference to the ExpandableListView
		myList = (ExpandableListView) view.findViewById(R.id.myList);
		// create the adapter by passing your ArrayList data
		listAdapter = new PowerUpListAdapter(container.getContext(), deptList);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);

		// expand all Groups
		// expandAll();
		collapseAll();
		// add new item to the List
		// Button add = (Button) view.findViewById(R.id.add);
		// add.setOnClickListener(this);

		// listener for child row click
		myList.setOnChildClickListener(myListItemClicked);
		// listener for group heading click
		myList.setOnGroupClickListener(myListGroupClicked);
		return view;
	}

	public void onClick(View v) {

		switch (v.getId()) {

		// add entry to the List
		/*
		 * case R.id.add:
		 * 
		 * Spinner spinner = (Spinner) view.findViewById(R.id.department);
		 * String department = spinner.getSelectedItem().toString(); EditText
		 * editText = (EditText) view.findViewById(R.id.product); String product
		 * = editText.getText().toString(); editText.setText("");
		 * 
		 * //add a new item to the list int groupPosition =
		 * addProduct(department,product); //notify the list so that changes can
		 * take effect listAdapter.notifyDataSetChanged();
		 * 
		 * //collapse all groups collapseAll(); //expand the group where item
		 * was just added myList.expandGroup(groupPosition); //set the current
		 * group to be selected so that it becomes visible
		 * myList.setSelectedGroup(groupPosition);
		 * 
		 * break;
		 * 
		 * // More buttons go here (if any) ...
		 */
		}
	}

	// method to expand all groups
	@SuppressWarnings("unused")
	private void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			myList.expandGroup(i);
		}
	}

	// method to collapse all groups
	private void collapseAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			myList.collapseGroup(i);
		}
	}

	// load some initial data into out list
	private void loadData() {

		addProduct("Block 1", "Alexandra");
		addProduct("Block 1", "Allendale");
		addProduct("Block 1", "Athol East");
		addProduct("Block 1", "Athol Gardens");

		addProduct("Block 2", "Bassonia");
		addProduct("Block 2", "Braamfontein");

	}

	// our child listener
	private OnChildClickListener myListItemClicked = new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			// get the group header
			HeaderInfo headerInfo = deptList.get(groupPosition);
			// get the child info
			DetailInfo detailInfo = headerInfo.getProductList().get(
					childPosition);
			// display it or do something with it
			Toast.makeText(
					view.getContext(),
					"Clicked on Detail " + headerInfo.getName() + "/"
							+ detailInfo.getName(), Toast.LENGTH_LONG).show();
			return false;
		}

	};

	// our group listener
	private OnGroupClickListener myListGroupClicked = new OnGroupClickListener() {

		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {

			// get the group header
			HeaderInfo headerInfo = deptList.get(groupPosition);
			// display it or do something with it
			Toast.makeText(view.getContext(),
					"Child on Header " + headerInfo.getName(),
					Toast.LENGTH_LONG).show();

			return false;
		}

	};

	// here we maintain our products in various departments
	private int addProduct(String department, String product) {

		int groupPosition = 0;

		// check the hash map if the group already exists
		HeaderInfo headerInfo = myDepartments.get(department);
		// add the group if doesn't exists
		if (headerInfo == null) {
			headerInfo = new HeaderInfo();
			headerInfo.setName(department);
			myDepartments.put(department, headerInfo);
			deptList.add(headerInfo);
		}

		// get the children for the group
		ArrayList<DetailInfo> productList = headerInfo.getProductList();
		// size of the children list
		int listSize = productList.size();
		// add to the counter
		listSize++;

		// create a new child and add that to the group
		DetailInfo detailInfo = new DetailInfo();
		detailInfo.setSequence(String.valueOf(listSize));
		detailInfo.setName(product);
		productList.add(detailInfo);
		headerInfo.setProductList(productList);

		// find the group position inside the list
		groupPosition = deptList.indexOf(headerInfo);
		return groupPosition;
	}

}
