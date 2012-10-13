package com.cmbasckeira.psimanager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cmbasckeira.psimanager.AddListFormFragment.OnSaveListListener;
import com.cmbasckeira.psimanager.AddPowerFormFragment.OnSavePowerListener;
import com.cmbasckeira.psimanager.EditListFormFragment.OnUpdateListListener;
import com.cmbasckeira.psimanager.EditPowerFormFragment.OnUpdatePowerListener;
import com.cmbasckeira.psimanager.ListsFragment.OnAddListFormListener;
import com.cmbasckeira.psimanager.ListsFragment.OnEditListFormListener;
import com.cmbasckeira.psimanager.PowersFragment.OnAddPowerFormListener;
import com.cmbasckeira.psimanager.PowersFragment.OnEditPowerFormListener;

public class MainActivity extends FragmentActivity implements OnAddPowerFormListener, 
															  OnEditPowerFormListener, 
															  OnSavePowerListener, 
															  OnUpdatePowerListener,
															  OnAddListFormListener, 
															  OnEditListFormListener,
															  OnSaveListListener, 
															  OnUpdateListListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	public void onAddPowerForm() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.POWERS_ADD_POWER);
	}
	
	public void onEditPowerForm() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.POWERS_EDIT_POWER);
	}
	
	public void onSavePower() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.BASE_TAB);
	}
	
	public void onUpdatePower() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.BASE_TAB);
	}
	
	public void onAddListForm() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.LISTS_ADD_LIST);
	}
	
	public void onEditListForm() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.LISTS_EDIT_LIST);
	}
	
	public void onSaveList() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.BASE_TAB);
	}
	
	public void onUpdateList() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.BASE_TAB);
	}
	
    //This is to correctly "flush" sub-tabs when back key is pressed
    public void onBackPressed() {
    	TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
    	tabs.setSubTab(TabsFragment.BASE_TAB);
    	super.onBackPressed();
    	return;
    }
}
