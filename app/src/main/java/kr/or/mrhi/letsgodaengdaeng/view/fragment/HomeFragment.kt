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

        /** 배너에 넣을 이미지 3개를 저장 */
        bannerList.add(R.drawable.bannerdog2)
        bannerList.add(R.drawable.bannerseoulcenter)
        bannerList.add(R.drawable.bannerkaps)

        /** 리사이클러뷰 구현 */
        val bannerAdapter = BannerAdapter(requireActivity(), bannerList)
        val recyclerView: RecyclerView = binding.BannerRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(container?.context,LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = bannerAdapter

        /** indicator 추가 리사이클러뷰 밑에 같이 연동되는 동그라미친구들 */
        /** RecyclerView를 집어넣고 Pager의 느낌을 내기 위하여 PagerSnapHelper를 이용 (뷰페이저가 아닌 리사이클러뷰의 페이지어댑터)  */
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)
        val indicator: CircleIndicator2 = binding.indicator
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper)

        /** 플래그 TRUE 변경으로 bannerJob 이 실행될 수 있도록 설정 */
        bannerJobFlag = true
        /** 자동 슬라이드 배너를 실행 */
        rollingBanner()

        /** 프레임레이아웃에 홈뷰프래그먼트를 집어넣고 바로 보이게 만든다 */
        homeViewFragment = HomeViewFragment()
        childFragmentManager.beginTransaction().replace(R.id.homeFrameLayout, homeViewFragment).commit()

        return binding.root
    }

    /** 프래그먼트가 onDestroy() 될때 bannerJob 취소 */
    override fun onDestroy() {
        super.onDestroy()
        /** 플래그 false 변경하여 종료하게끔 설정 */
        bannerJobFlag = false
        /** bannerJob을 취소한다 */
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