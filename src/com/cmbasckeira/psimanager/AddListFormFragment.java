package com.cmbasckeira.psimanager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddListFormFragment extends Fragment implements android.text.TextWatcher {
	
	private EditText ETname;
	private EditText ETdescription;
	private EditText ETpoints;
	Button saveListButton;
	Cursor data;
	private static final String fields[] = {"name","duration","cost"};
	private ListView selectedPowers = null;
	private ListView unselectedPowers = null;
	
	OnSaveListListener mListener;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.add_list_form_fragment, container, false);
    	
    	PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        data = db.rawQuery("Select ID _id, name, duration, cost FROM powers",null );
        unselectedPowers = (ListView) view.findViewById (R.id.unselected_powers_list);
        
        @SuppressWarnings("deprecation")
        CursorAdapter dataSource = new SimpleCursorAdapter(getActivity(), R.layout.powers_list, data, fields, new int[] {R.id.power_name_col,R.id.power_duration_col,R.id.power_cost_col});
        unselectedPowers.setAdapter(dataSource);
        
    	// getting fields
    	ETname = (EditText) view.findViewById(R.id.list_name_field);
    	ETdescription = (EditText) view.findViewById(R.id.list_description_field);
    	ETpoints = (EditText) view.findViewById(R.id.power_points_field);
    	
    	// Adding listeners for input validation
    	ETname.addTextChangedListener(this);
    	ETdescription.addTextChangedListener(this);
    	ETpoints.addTextChangedListener(this);
    	
    	saveListButton = (Button) view.findViewById(R.id.save_list);
        saveListButton.setOnClickListener(saveListButtonClick);
        
        return view;
    }
    
    public void afterTextChanged(Editable s) {
    	
    	String name = ETname.getText().toString();
    	String points = ETpoints.getText().toString();
    	
    	if( name.length() == 0 )
    		ETname.setError( "Name is required!" );
    	if( points.length() == 0 )
    		ETpoints.setError( "Power Points is required!" );
    	
    	if(name.length()==0 || points.length()==0) {
    		saveListButton.setEnabled(false);
    	}
    	else {
    		saveListButton.setEnabled(true);
    	}
    }
    
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Auto-generated method stub
		
	}
    
    View.OnClickListener saveListButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			saveList(v);			
		}
	};
    
    public void saveList(View view){
    	
    	String name = ETname.getText().toString();
    	String description = ETdescription.getText().toString();
    	Integer points = Integer.parseInt(ETpoints.getText().toString());
    	
    	ContentValues values = new ContentValues();
    	values.put("name",name);
    	values.put("description",description);
    	values.put("points",points);
    	
    	PsiManagerOpenHelper DBHelper = new PsiManagerOpenHelper(getActivity());
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	db.insert("lists",null,values);
    	
    	mListener.onSaveList();
    	
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_lists, new ListsFragment(), TabsFragment.TAB_LISTS)
        .remove(this)
        .commit();
    }
    
    public interface OnSaveListListener {
        public void onSaveList();
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSaveListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSaveListListener");
        }
    }

}
