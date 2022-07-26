package com.example.submissionkedua.util

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionkedua.R
import com.example.submissionkedua.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "About Developer"

        Glide.with(this)
            .load(R.drawable.my_photo)
            .apply(RequestOptions().override(150, 150))
            .into(binding.ivPhoto)

        setUpHyperLink(binding.dicodingFundamentalCourseReference1)

        setUpHyperLink(binding.androidViewModelReference1)

        setUpHyperLink(binding.changeAndroidSearchviewTextReference1)

        setUpHyperLink(binding.coordinatorLayoutReference1)

        setUpHyperLink(binding.coordinatorLayoutReference2)

        setUpHyperLink(binding.hyperlinkTextviewReference1)

        setUpHyperLink(binding.hyperlinkTextviewReference2)

    }

    private fun setUpHyperLink(tv: TextView){
        tv.movementMethod = LinkMovementMethod.getInstance()
        tv.setLinkTextColor(Color.BLUE)
        tv.removeLinksUnderline()
    }

    private fun TextView.removeLinksUnderline() {
        val spannable = SpannableString(text)
        for (urlSpan in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(object : URLSpan(urlSpan.url) {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), 0)
        }
        text = spannable
    }

}