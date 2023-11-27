package com.huntdai.hungariantraindelays.ui.about

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.huntdai.hungariantraindelays.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(layoutInflater)

        val adamkapus: TextView = binding.adamkapusGithubLink
        adamkapus.movementMethod = LinkMovementMethod.getInstance()
        adamkapus.setLinkTextColor(Color.BLUE)

        val martinlenyi: TextView = binding.martinLenyiGithubLink
        martinlenyi.movementMethod = LinkMovementMethod.getInstance()
        martinlenyi.setLinkTextColor(Color.BLUE)

        val matedebreczeni: TextView = binding.mateDebreczeniGithubLink
        matedebreczeni.movementMethod = LinkMovementMethod.getInstance()
        matedebreczeni.setLinkTextColor(Color.BLUE)

        val openSource: TextView = binding.openSourceLink
        openSource.movementMethod = LinkMovementMethod.getInstance()
        openSource.setLinkTextColor(Color.BLUE)

        return binding.root
    }
}