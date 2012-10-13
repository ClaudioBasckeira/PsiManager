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

public class ListsFragment extends ListFragment {
    
	private static final String fields[] = {"name","points"};
	OnAddListFormListener addListener;
	OnEditListFormListener editListener;
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
            View view = inflater.inflate(R.layout.lists_fragment, container, false);
            
            PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
            SQLiteDatabase db = helper.getReadableDatabase();
            data = db.rawQuery("Select ID _id, name, points FROM lists",null );
            
            @SuppressWarnings("deprecation")
    		CursorAdapter dataSource = new SimpleCursorAdapter(getActivity(), R.layout.lists_list, data, fields, new int[] {R.id.list_name_col,R.id.power_points_col});
    		setListAdapter(dataSource);
            
            ImageButton addListButton = (ImageButton) view.findViewById(R.id.add_list);
            addListButton.setOnClickListener(addListButtonClick);
            return view;
    }
    
    View.OnClickListener addListButtonClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			addListForm();			
		}
	};
	
    public void addListForm(){
    	addListener.onAddListForm();
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_lists, new AddListFormFragment(), TabsFragment.LISTS_ADD_LIST)
        .addToBackStack("Lists")
        .commit();
    }
    
    public void editListForm(long id){
    	editListener.onEditListForm();
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
        .replace(R.id.tab_lists, EditListFormFragment.newInstance(id), TabsFragment.LISTS_EDIT_LIST)
        .addToBackStack("Lists")
        .commit();
    }
    
    public interface OnAddListFormListener {
        public void onAddListForm();
    }
    
    public interface OnEditListFormListener {
        public void onEditListForm();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addListener = (OnAddListFormListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAddListFormListener");
        }
        
        try {
            editListener = (OnEditListFormListener) activity;
        } catch (ClassCastException e) {
        	throw new ClassCastException(activity.toString() + " must implement OnEditListFormListener");
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      if (v.getId()==android.R.id.list) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(((TextView)(getListView().getChildAt(info.position)).findViewById(R.id.list_name_col)).getText().toString());
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
    	  editListForm(info.id);
      }
      else if(menuItemIndex==1){
          PsiManagerOpenHelper helper = new PsiManagerOpenHelper(getActivity());
          SQLiteDatabase db = helper.getWritableDatabase();
          db.delete("lists", "ID="+info.id,new String[]{});
          data = db.rawQuery("Select ID _id, name, points FROM lists",null );
    	  ((CursorAdapter)getListAdapter()).changeCursor(data);
      }
      return true;
    }
      
}
