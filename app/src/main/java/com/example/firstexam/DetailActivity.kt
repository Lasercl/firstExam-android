package com.example.firstexam

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.firstexam.database.FavoriteEvent
import com.example.firstexam.databinding.ActivityDetailBinding
import com.example.firstexam.databinding.ItemRowHeroBinding
import com.example.firstexam.response.ListEventsItem
import retrofit2.http.Tag
import java.util.Objects

class DetailActivity : AppCompatActivity() {
    private var isFavorite = false
    private var menuItem: MenuItem? = null
   private var _binding :ActivityDetailBinding?=null
    private var favoriteEvent:FavoriteEvent?=null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var eventItem:ListEventsItem
    companion object{
        val DETAIL_ITEM="detail_item"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel=obtainViewModel(this@DetailActivity)

        _binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back_responsive);


        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ListEventsItem>(DETAIL_ITEM, ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListEventsItem>(DETAIL_ITEM)
        }
        if (data != null) {
            eventItem=data
            Glide.with(binding.root.context)
                .load("${data.mediaCover}")
                .into(binding.imageDetail)

            binding.titleDetail.text=data.name
            binding.descDetail.text = data.summary
//            binding.descriptionInformationDetail.text = HtmlCompat.fromHtml(
//                data.description,
//                HtmlCompat.FROM_HTML_MODE_LEGACY
//            )
            val descriptionHtml = data.description ?: ""
            binding.webDescription.loadDataWithBaseURL(null, descriptionHtml, "text/html", "utf-8", null)

//            binding.descriptionInformationDetail.movementMethod = LinkMovementMethod.getInstance()
//            binding.descriptionInformationDetail.text = data.description
            // Button untuk buka link Google
            val totalQuota = data.quota
            val registrants = data.registrants
            val progressPercent = (registrants!!.toFloat() / totalQuota!! * 100).toInt()

            binding.tvQuota.text = "Kuota: $registrants/$totalQuota"
            binding.progressQuota.progress = progressPercent
            if(progressPercent > 80) {
                binding.progressQuota.progressTintList = ColorStateList.valueOf(Color.RED)
            } else {
                binding.progressQuota.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primaryColor))
            }

            binding.buttonDetail.setOnClickListener {
                val url = data.link
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            binding.dateCreated.text = data.beginTime
            binding.nameAuthor.text = data.ownerName
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu?.findItem(R.id.action_save)

        // Cek status favorite dari DB
        detailViewModel.isFavorite(eventItem.id!!).observe(this) { favorite ->
            isFavorite = favorite
            updateFavoriteIcon()
        }


        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }else if (item.itemId == R.id.action_save) {
            if (isFavorite) {
                // Sudah favorit → hapus
                detailViewModel.deleteById(eventItem.id!!)

            } else {
                // Belum favorit → tambahkan
                favoriteEvent= FavoriteEvent()
                favoriteEvent?.id=eventItem.id!!
                favoriteEvent?.name=eventItem.name!!
                favoriteEvent?.mediaCover=eventItem.mediaCover!!
                detailViewModel.insert(favoriteEvent!!)

            }
            updateFavoriteIcon()
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateFavoriteIcon() {
        menuItem?.icon = if (isFavorite) {
            ContextCompat.getDrawable(this, R.drawable.ic_star_full)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_star)
        }
    }
}