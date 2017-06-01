package com.ensatus.truqartmerchant.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.fragments.InviteNewFrag;
import com.ensatus.truqartmerchant.fragments.MyCustomerFrag;

public class MyCustomersActivity extends AppCompatActivity implements
        InviteNewFrag.OnContactsInteractionListener {

    private ConstraintLayout constraintLayout1, constraintLayout2;


    // Defines a tag for identifying log entries
    private static final String TAG = "ContactsListActivity";



    // If true, this is a larger screen device which fits two panes
    private boolean isTwoPaneLayout;

    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.

    private boolean isSearchResultView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customers);

        // Check if this activity instance has been triggered as a result of a search query. This
        // will only happen on pre-HC OS versions as from HC onward search is carried out using
        // an ActionBar SearchView which carries out the search in-line without loading a new
        // Activity.
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            // Fetch query from intent and notify the fragment that it should display search
            // results instead of all contacts.
            String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
           /* InviteNewFrag mContactsListFragment = (InviteNewFrag)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list);
            // This flag notes that the Activity is doing a search, and so the result will be
            // search results rather than all contacts. This prevents the Activity and Fragment
            // from trying to a search on search results.
            isSearchResultView = true;
           mContactsListFragment.setSearchQuery(searchQuery);

            // Set special title for search results
            String title = getString(R.string.contacts_list_search_results_title, searchQuery);
            setTitle(title);*/
        }



        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.materialup_viewpager);
        TabItem tab1 = (TabItem) tabLayout.findViewById(R.id.mycust);
        constraintLayout1 = (ConstraintLayout) findViewById(R.id.contlay);
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.contlay2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if (position == 0){
                    constraintLayout1.setVisibility(View.VISIBLE);
                    constraintLayout2.setVisibility(View.GONE);
                }else if (position == 1){
                    constraintLayout1.setVisibility(View.GONE);
                    constraintLayout2.setVisibility(View.VISIBLE);
                }
                Log.e("tab",""+position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


          int n = 0;
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(n);
        tabLayout.setupWithViewPager(viewPager);



    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri) {

    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact is no longer selected.
     */
    @Override
    public void onSelectionCleared() {

    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }

    class TabsAdapter extends FragmentStatePagerAdapter {
        // private Bundle args;
        public TabsAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:

                    MyCustomerFrag tab1 = new MyCustomerFrag();

                    return tab1;
                case 1:
                    InviteNewFrag tab2 = new InviteNewFrag();

                    return tab2;

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Customers";
                case 1:
                    return "Invite New";
                default:
                    break;
            }
            return "";
        }
    }
}
