package kr.or.mrhi.letsgodaengdaeng.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {

    val fragmentList = ArrayList<Fragment>()
    val titleList =ArrayList<String>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return  fragmentList.get(position)
    }

    fun addFragment(fragment: Fragment,title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }
}