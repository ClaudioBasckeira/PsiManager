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

public class EditListFormFragment extends Fragment implements android.text.TextWatcher {
	
	private EditText ETname;
	private EditText ETdescription;
	private EditText ETpoints;
	private long list_id;
	Button saveListButton;
	
	OnUpdateListListener mListener;
	
	public static EditListFormFragment newInstance(long id){
		EditListFormFragment f = new EditListFormFragment();
		
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong("list_id", id);
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
    	
    	list_id = getArguments().getLong("list_id");
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.add_list_form_fragment, container, false);
        
    	// getting fields
    	ETname = (EditText) view.findViewById(R.id.list_name_field);
    	ETdescription = (EditText) view.findViewById(R.id.list_description_field);
    	ETpoints = (EditText) view.findViewById(R.id.power_points_field);
    	
    	// Loading the data into the fields
        PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor data = db.rawQuery("Select ID _id, name, description, points FROM lists where ID="+list_id,null );
    	data.moveToFirst();
        ETname.setText(data.getString(1));
    	ETdescription.setText(data.getString(2));
    	ETpoints.setText(data.getString(3));
    	
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
    	db.update("lists",values,"ID="+list_id,null);
    	
    	mListener.onUpdateList();
    	
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_lists, new ListsFragment(), TabsFragment.TAB_LISTS)
        .remove(this)
        .commit();
    }
    
    public interface OnUpdateListListener {
        public void onUpdateList();
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUpdateListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUpdateListListener");
        }
    }

}
