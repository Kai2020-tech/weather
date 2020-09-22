package com.example.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    var fragments: ArrayList<Fragment> = arrayListOf(
        FirstFragment(),
        SecondFragment()
//        ThirdFragment(),
//        ThirdFragment(),
//        ThirdFragment(),
//        ThirdFragment(),
//        ThirdFragment(),
//        ThirdFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}