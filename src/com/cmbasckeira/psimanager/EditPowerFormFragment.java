package com.cmbasckeira.psimanager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditPowerFormFragment extends Fragment implements android.text.TextWatcher {
	
	private EditText ETname;
	private EditText ETdescription;
	private EditText ETduration;
	private EditText ETcost;
	private long power_id;
	Button savePowerButton;
	
	OnUpdatePowerListener mListener;
	
	public static EditPowerFormFragment newInstance(long id){
		EditPowerFormFragment f = new EditPowerFormFragment();
		
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong("power_id", id);
        f.setArguments(args);
        
		return f;
	}
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	
    	power_id = getArguments().getLong("power_id");
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.add_power_form_fragment, container, false);
        
    	// getting fields
    	ETname = (EditText) view.findViewById(R.id.power_name_field);
    	ETdescription = (EditText) view.findViewById(R.id.power_description_field);
    	ETduration = (EditText) view.findViewById(R.id.power_duration_field);
    	ETcost = (EditText) view.findViewById(R.id.power_cost_field);
    	
    	// Loading the data into the fields
        PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor data = db.rawQuery("Select ID _id, name, description, duration, cost FROM powers where ID="+power_id,null );
    	data.moveToFirst();
        ETname.setText(data.getString(1));
    	ETdescription.setText(data.getString(2));
    	ETduration.setText(data.getString(3));
    	ETcost.setText(data.getString(4));
    	
    	// Adding listeners for input validation
    	ETname.addTextChangedListener(this);
    	ETdescription.addTextChangedListener(this);
    	ETduration.addTextChangedListener(this);
    	ETcost.addTextChangedListener(this);
    	
    	savePowerButton = (Button) view.findViewById(R.id.save_power);
        savePowerButton.setOnClickListener(savePowerButtonClick);
        
        return view;
    }
    
    public void afterTextChanged(Editable s) {
    	
    	String name = ETname.getText().toString();
    	String duration = ETduration.getText().toString();
    	String cost = ETcost.getText().toString();
    	
    	if( name.length() == 0 )
    		ETname.setError( "Name is required!" );
    	if( duration.length() == 0 )
    		ETduration.setError( "Duration is required!" );
    	if( cost.length() == 0 )
    		ETcost.setError( "Cost is required!" );
    	
    	if(name.length()==0 || duration.length()==0 || cost.length()==0) {
    		savePowerButton.setEnabled(false);
    	}
    	else {
    		savePowerButton.setEnabled(true);
    	}
    }
    
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Auto-generated method stub
		
	}
    
    View.OnClickListener savePowerButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			savePower(v);			
		}
	};
    
    public void savePower(View view){
    	
    	String name = ETname.getText().toString();
    	String description = ETdescription.getText().toString();
    	Integer duration = Integer.parseInt(ETduration.getText().toString());
    	Integer cost = Integer.parseInt(ETcost.getText().toString());
    	
    	ContentValues values = new ContentValues();
    	values.put("name",name);
    	values.put("description",description);
    	values.put("duration",duration);
    	values.put("cost",cost);
    	
    	PsiManagerOpenHelper DBHelper = new PsiManagerOpenHelper(getActivity());
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	db.update("powers",values,"ID="+power_id,null);
    	
    	mListener.onUpdatePower();
    	
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_powers, new PowersFragment(), TabsFragment.TAB_POWERS)
        .remove(this)
        .commit();
    }
    
    public interface OnUpdatePowerListener {
        public void onUpdatePower();
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUpdatePowerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUpdatePowerListener");
        }
    }

}
