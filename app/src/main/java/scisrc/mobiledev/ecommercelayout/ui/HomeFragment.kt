package scisrc.mobiledev.ecommercelayout.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3
import scisrc.mobiledev.ecommercelayout.ProductModel
import scisrc.mobiledev.ecommercelayout.R

class HomeFragment : Fragment() {
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var recommendedAdapter: ProductAdapter
    private val recommendedProducts = mutableListOf<ProductModel>()

    private lateinit var promotionsRecyclerView: RecyclerView
    private lateinit var promotionsAdapter: ProductAdapter
    private val promotionsList = mutableListOf<ProductModel>()

    private lateinit var bannerViewPager: ViewPager2
    private lateinit var bannerIndicator: CircleIndicator3
    private val bannerHandler = Handler(Looper.getMainLooper())

    private val bannerRunnable = object : Runnable {
        override fun run() {
            val currentItem = bannerViewPager.currentItem
            val itemCount = bannerViewPager.adapter?.itemCount ?: 0
            if (itemCount > 0) {
                bannerViewPager.currentItem = (currentItem + 1) % itemCount
            }
            bannerHandler.postDelayed(this, 3000) // เลื่อนทุก 3 วินาที
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // เชื่อม RecyclerView สินค้าแนะนำ
        recommendedRecyclerView = view.findViewById(R.id.recommendedProductsRecycler)
        recommendedRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recommendedProducts.add(ProductModel("กระเป๋าสะพาย", "กระเป๋าสะพายข้างสุดเท่", 350.00, R.drawable.ic_pen))
        recommendedProducts.add(ProductModel("กระเป๋าถือ", "กระเป๋าถือหนังแท้ทรงสวย", 450.00,  R.drawable.ic_ruler))
        recommendedProducts.add(ProductModel("กระเป๋าสตางค์", "กระเป๋าสตางค์หนังแท้", 250.00, R.drawable.ic_notebook))

        recommendedAdapter = ProductAdapter(recommendedProducts) { updateFavorites() }
        recommendedRecyclerView.adapter = recommendedAdapter

        // เชื่อม RecyclerView โปรโมชั่น
        promotionsRecyclerView = view.findViewById(R.id.promotionsRecycler)
        promotionsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        promotionsList.add(ProductModel("โปรโมชั่นกระเป๋าแพ็คคู่", "ซื้อกระเป๋า 2 ใบในราคาพิเศษ", 499.00, R.drawable.bundle_a))
        promotionsList.add(ProductModel("โปรโมชั่นลด 20%", "ลดราคาสินค้าทุกชิ้น 20%", 0.00, R.drawable.bundle_a))
        promotionsList.add(ProductModel("โปรโมชั่นสมาชิกใหม่", "รับส่วนลด 100 บาท สำหรับการสั่งซื้อครั้งแรก", 0.00, R.drawable.bundle_a))

        promotionsAdapter = ProductAdapter(promotionsList) { updateFavorites() }
        promotionsRecyclerView.adapter = promotionsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerViewPager = view.findViewById(R.id.bannerViewPager)
        bannerIndicator = view.findViewById(R.id.bannerIndicator)

        val bannerImages = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        val adapter = BannerAdapter(bannerImages)
        bannerViewPager.adapter = adapter

        bannerIndicator.setViewPager(bannerViewPager)
        bannerHandler.postDelayed(bannerRunnable, 3000)

        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bannerHandler.removeCallbacks(bannerRunnable)
                bannerHandler.postDelayed(bannerRunnable, 3000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerHandler.removeCallbacks(bannerRunnable)
    }

    private fun updateFavorites() {
        // อัปเดตรายการโปรด
    }
}
