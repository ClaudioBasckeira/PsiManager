package com.cmbasckeira.psimanager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cmbasckeira.psimanager.AddPowerFormFragment.OnSavePowerListener;
import com.cmbasckeira.psimanager.PowersFragment.OnAddPowerFormListener;

public class MainActivity extends FragmentActivity implements OnAddPowerFormListener,OnSavePowerListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	public void onAddPowerForm() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.POWERS_ADD_POWER);
	}
	
	public void onSavePower() {
		TabsFragment tabs = (TabsFragment)getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
		tabs.setSubTab(TabsFragment.BASE_TAB);
	}
}
