package com.tecco.powerup;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FaultReport extends Fragment implements OnClickListener {

	@SuppressWarnings("unused")
	private String[] area_items;
	@SuppressWarnings("unused")
	private Object problemType_items;

	public FaultReport() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_fault_report, container,
				false);
		area_items = getResources().getStringArray(R.array.area_items);

		problemType_items = getResources().getStringArray(
				R.array.problemType_items);

		Spinner problemTypeSpinner = (Spinner) view
				.findViewById(R.id.problemTypeSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> problemTypeAdapter = ArrayAdapter
				.createFromResource(container.getContext(), R.array.problemType_items,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		problemTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		problemTypeSpinner.setAdapter(problemTypeAdapter);

		Spinner areasSpinner = (Spinner) view.findViewById(R.id.areasSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> areasAdapter = ArrayAdapter
				.createFromResource(container.getContext(), R.array.area_items,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		areasAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		areasSpinner.setAdapter(areasAdapter);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		return view;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}
}
