package com.example.ch12_material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch12_material.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(OneFragment(), TwoFragment(), ThreeFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add......................................
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewpager) {
            tab, position ->
            tab.text = "Tab${(position+1)}"}.attach()

        /*
         * ## 전체 연계 과정 요약 ##
         * 1. `setSupportActionBar`: 툴바를 액션바로 만든다.
         * 2. `ActionBarDrawerToggle`: 이 액션바와 DrawerLayout(슬라이드 메뉴)을 연결할 준비를 한다.
         * 3. `syncState()`: 햄버거 아이콘과 슬라이드 메뉴의 상태를 일치시킨다.
         * 4. `MyFragmentPagerAdapter`: ViewPager2에 들어갈 페이지(프래그먼트) 목록을 준비한다.
         * 5. `binding.viewpager.adapter`: 준비된 페이지 목록을 ViewPager2에 장착한다.
         * 6. `TabLayoutMediator`: 상단의 탭 메뉴(TabLayout)와 ViewPager2를 연동시킨다.
         * -> 결과: 탭을 누르면 해당 페이지로 이동하고, 페이지를 스와이프하면 해당 탭이 선택되는 UI가 완성됩니다.
         */
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //이벤트가 toggle 버튼에서 제공된거라면..
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}