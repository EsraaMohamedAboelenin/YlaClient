package solversteam.familycab.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import solversteam.familycab.Fragments.History_frag;
import solversteam.familycab.Fragments.schdul_frag;
import solversteam.familycab.R;

/**
 * Created by mosta on 5/4/2017.
 */
 public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public ViewPagerAdapter(FragmentManager fm, Context my_rides) {
        super(fm);
        this.context=my_rides;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
           if (position == 0)
        {
            fragment = new schdul_frag();
        }
       else if (position == 1)
        {
            fragment = new History_frag();
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

         if (position == 0)
        {
            title = context.getResources().getString(R.string.nav_scheduled_title) ;
        }


       else if (position == 1)
        {
            title =context.getResources().getString(R.string.nav_history_title) ;
        }
        return title;
    }
}