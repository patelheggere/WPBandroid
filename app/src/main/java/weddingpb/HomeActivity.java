package weddingpb;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import Adapter.ImageAdapter;
import in.webphotobooks.R;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawerLayout;
    String[] item;
    ListView drawerList;
    Button login_Button;
    ViewPager viewPager;
    ImageAdapter imageAdapter;
    int[] imagepath = {R.drawable.jass1,R.drawable.jass2,R.drawable.jass5,R.drawable.jass2};
    private ImageView _btn1, _btn2, _btn3, _btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        login_Button = (Button)findViewById(R.id.login_Btn);
        item = getResources().getStringArray(R.array.item_list);
        drawerList = (ListView)findViewById(R.id.drawerList);
        drawerList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, item));
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        /*drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });*/
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "a", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplication(), "b", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "c", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new ImageAdapter(this, imagepath));
        viewPager.setCurrentItem(0);
        initButton();
        setTab();
        onCircleButtonClick();

    }
    private void onCircleButtonClick() {
        _btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn1.setImageResource(R.drawable.fill_circle);
                viewPager.setCurrentItem(0);
            }
        });
        _btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn2.setImageResource(R.drawable.fill_circle);
                viewPager.setCurrentItem(1);
            }
        });
        _btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn3.setImageResource(R.drawable.fill_circle);
                viewPager.setCurrentItem(2);
            }
        });
        _btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn3.setImageResource(R.drawable.fill_circle);
                viewPager.setCurrentItem(3);
            }
        });
    }

    private void setTab() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int position) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                _btn1.setImageResource(R.drawable.holo_circle);
                _btn2.setImageResource(R.drawable.holo_circle);
                _btn3.setImageResource(R.drawable.holo_circle);
                _btn4.setImageResource(R.drawable.holo_circle);
                btnAction(position);
            }
        });
    }
    private void btnAction(int action) {
        switch (action) {
            case 0:
                _btn1.setImageResource(R.drawable.fill_circle);
                break;
            case 1:
                _btn2.setImageResource(R.drawable.fill_circle);
                break;
            case 2:
                _btn3.setImageResource(R.drawable.fill_circle);
                break;
            case 3:
                _btn4.setImageResource(R.drawable.fill_circle);
                break;
        }
    }
    private void initButton() {
       /* _btn1 = (ImageView)findViewById(R.id.btn1);
        _btn1.setImageResource(R.drawable.fill_circle);
        _btn2 = (ImageView) findViewById(R.id.btn2);
        _btn3 = (ImageView)findViewById(R.id.btn3);
        _btn4 = (ImageView)findViewById(R.id.btn4);
*/    }
    private void setButton(Button btn, String text, int h, int w) {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }
}
