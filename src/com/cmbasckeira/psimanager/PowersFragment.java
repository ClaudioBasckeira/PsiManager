package com.cmbasckeira.psimanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class PowersFragment extends ListFragment {
	
	private static final String fields[] = {"name","duration","cost"};
	OnAddPowerFormListener mListener;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.powers_fragment, container, false);
        
        PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor data = db.rawQuery("Select ID _id, name, duration, cost FROM powers",null );
        
        @SuppressWarnings("deprecation")
		CursorAdapter dataSource = new SimpleCursorAdapter(getActivity(), R.layout.powers_list, data, fields, new int[] {R.id.power_name_col,R.id.power_duration_col,R.id.power_cost_col});
		setListAdapter(dataSource);
        
        ImageButton addPowerButton = (ImageButton) view.findViewById(R.id.add_power);
        addPowerButton.setOnClickListener(addPowerButtonClick);
        return view;
    }
    
    View.OnClickListener addPowerButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			addPowerForm(v);			
		}
	};
    
    public void addPowerForm(View view){
    	mListener.onAddPowerForm();
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_powers, new AddPowerFormFragment(), TabsFragment.POWERS_ADD_POWER)
        .addToBackStack("Powers")
        .commit();
    }
    
    public interface OnAddPowerFormListener {
        public void onAddPowerForm();
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddPowerFormListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAddPowerFormListener");
        }
    }
}
