package com.cmbasckeira.psimanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabsFragment extends Fragment implements OnTabChangeListener{
	
    private static final String TAG = "FragmentTabs";
    public static final String TAB_HOME = "Home";
    public static final String TAB_LISTS = "Lists";
    public static final String TAB_POWERS = "Powers";
    
    public static final String BASE_TAB = "base";
    public static final String POWERS_ADD_POWER = "AddNewPower";

	private View mRoot;
    private TabHost mTabHost;
    private int mCurrentTab;
    private String mSubTab;
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tabs_fragment, null);
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();
        return mRoot;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
 
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(mCurrentTab);
        // manually start loading stuff in the first tab
        updateTab(TAB_HOME, R.id.tab_home);
    }
    
    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!
        mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
        mTabHost.addTab(newTab(TAB_HOME, R.string.tab_home, R.id.tab_home));
        mTabHost.addTab(newTab(TAB_LISTS, R.string.tab_lists, R.id.tab_lists));
        mTabHost.addTab(newTab(TAB_POWERS, R.string.tab_powers, R.id.tab_powers));
    }
    
    private TabSpec newTab(String tag, int labelId, int tabContentId) {
        Log.d(TAG, "buildTab(): tag=" + tag);
 
        View indicator = LayoutInflater.from(getActivity()).inflate(
                R.layout.tab,
                (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        ((TextView) indicator.findViewById(R.id.text)).setText(labelId);
 
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }
 
    public void onTabChanged(String tabId) {
        Log.d(TAG, "onTabChanged(): tabId=" + tabId);
        if (TAB_HOME.equals(tabId)) {
            updateTab(tabId, R.id.tab_home);
            mCurrentTab = 0;
            mSubTab = BASE_TAB;
            return;
        }
        if (TAB_LISTS.equals(tabId)) {
            updateTab(tabId, R.id.tab_lists);
            mCurrentTab = 1;
            mSubTab = BASE_TAB;
            return;
        }
        if (TAB_POWERS.equals(tabId)) {
            updateTab(tabId, R.id.tab_powers);
            mCurrentTab = 2;
            return;
        }
    }
    
    public void setSubTab(String st){
    	mSubTab = st;
    }
 
    private void updateTab(String tabId, int placeholder) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment;
        String tag = tabId;

        //Decides which fragment will be loaded depending on selected tab
        if (TAB_LISTS.equals(tabId)) {
            fragment = new ListsFragment();
        }
        else if (TAB_POWERS.equals(tabId)) {
        	//This happens if the screen was rotated while on the Add Power Form
        	//In this case we want to conserve the form and all of it's current data
        	if(POWERS_ADD_POWER.equals(mSubTab)){
        		fragment = (AddPowerFormFragment)getFragmentManager().findFragmentByTag(POWERS_ADD_POWER);
        		if(fragment == null) {
        			fragment = new AddPowerFormFragment();
        		}
	            tag = POWERS_ADD_POWER;
        	}
        	else {
	            fragment = new PowersFragment();
        	}
        }
        else {
            fragment = new HomeFragment();
        }
        
        //Makes the transaction
        if(tabId != null) {
            fm.beginTransaction()
	            .replace(placeholder, fragment, tag)
	            .commit();
        }
    }
}
