package com.cmbasckeira.psimanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

public class PowersFragment extends ListFragment {
	
	private static final String fields[] = {"name","duration","cost"};
	OnAddPowerFormListener addListener;
	OnEditPowerFormListener editListener;
	Cursor data;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        registerForContextMenu(getListView());
    }
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.powers_fragment, container, false);
        
        PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        data = db.rawQuery("Select ID _id, name, duration, cost FROM powers",null );
        
        @SuppressWarnings("deprecation")
		CursorAdapter dataSource = new SimpleCursorAdapter(getActivity(), R.layout.powers_list, data, fields, new int[] {R.id.power_name_col,R.id.power_duration_col,R.id.power_cost_col});
		setListAdapter(dataSource);
        
        ImageButton addPowerButton = (ImageButton) view.findViewById(R.id.add_power);
        addPowerButton.setOnClickListener(addPowerButtonClick);
        return view;
    }
    
    View.OnClickListener addPowerButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			addPowerForm();			
		}
	};
    
    public void addPowerForm(){
    	addListener.onAddPowerForm();
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_powers, new AddPowerFormFragment(), TabsFragment.POWERS_ADD_POWER)
        .addToBackStack("Powers")
        .commit();
    }
    
    public void editPowerForm(long id){
    	editListener.onEditPowerForm();
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_powers, EditPowerFormFragment.newInstance(id), TabsFragment.POWERS_EDIT_POWER)
        .addToBackStack("Powers")
        .commit();
    }
    
    public interface OnAddPowerFormListener {
        public void onAddPowerForm();
    }
    
    public interface OnEditPowerFormListener {
        public void onEditPowerForm();
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addListener = (OnAddPowerFormListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAddPowerFormListener");
        }
        
        try {
            editListener = (OnEditPowerFormListener) activity;
        } catch (ClassCastException e) {
        	throw new ClassCastException(activity.toString() + " must implement OnEditPowerFormListener");
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      if (v.getId()==android.R.id.list) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(((TextView)(getListView().getChildAt(info.position)).findViewById(R.id.power_name_col)).getText().toString());
        String[] menuItems = getResources().getStringArray(R.array.item_menu);
        for (int i = 0; i<menuItems.length; i++) {
          menu.add(Menu.NONE, i, i, menuItems[i]);
        }
      }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      int menuItemIndex = item.getItemId();
      if(menuItemIndex==0){
    	  editPowerForm(info.id);
      }
      else if(menuItemIndex==1){
          PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
          SQLiteDatabase db = helper.getWritableDatabase();
          db.delete("powers", "ID="+info.id,new String[]{});
          data = db.rawQuery("Select ID _id, name, duration, cost FROM powers",null );
    	  ((CursorAdapter)getListAdapter()).changeCursor(data);
      }
      return true;
    }
}
