package kr.or.mrhi.letsgodaengdaeng.view.fragment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentHomeBinding
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.BannerAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.home.HomeViewFragment
import me.relex.circleindicator.CircleIndicator2

class HomeFragment : Fragment() {
    val TAG = this.javaClass.simpleName
    lateinit var binding: FragmentHomeBinding

    val bannerList = mutableListOf<Int>()
    var bannerJob: Job? = null
    var bannerJobFlag = false

    var mainActivity: MainActivity? = null
    lateinit var homeViewFragment: HomeViewFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.BannerRecyclerView.isSaveEnabled = false

        binding.toolbar.bringToFront()

        bannerList.clear()

        bannerList.add(R.drawable.bannerdog2)
        bannerList.add(R.drawable.bannerseoulcenter)
        bannerList.add(R.drawable.bannerkaps)

        val bannerAdapter = BannerAdapter(requireActivity(), bannerList)
        val recyclerView: RecyclerView = binding.BannerRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(container?.context,LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = bannerAdapter

        
        /** indicator 추가 */
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        val indicator: CircleIndicator2 = binding.indicator
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper)

        bannerJobFlag = true
        rollingBanner()

        homeViewFragment = HomeViewFragment()
        childFragmentManager.beginTransaction().replace(R.id.homeFrameLayout, homeViewFragment).commit()

        return binding.root
    }

    /** 프래그먼트가 onDestroy() 될때 bannerJob 취소 */
    override fun onDestroy() {
        super.onDestroy()
        bannerJobFlag = false
        bannerJob?.cancel()
    }

    /** 자동 슬라이드 배너 기능 */
    fun rollingBanner() {
        var recyclerNumber = 0
        val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
        bannerJob = backgroundScope.launch {
                while (bannerJobFlag == true) {
                    requireActivity().runOnUiThread {
                        if (recyclerNumber == 3) {
                            recyclerNumber = 0
                        }
                        binding.BannerRecyclerView.smoothScrollToPosition(recyclerNumber)
                        recyclerNumber += 1
                    }
                    try {
                        delay(3000)
                    } catch (e: Exception) {
                        Log.d(TAG, "${e.stackTrace}")
                    }
                }
            }
    }
}