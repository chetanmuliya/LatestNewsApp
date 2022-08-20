package learn.cm.latestnewsapp.presentation.news_detail.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import learn.cm.latestnewsapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var webUrl: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val args: HomeFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        webUrl = args.weblink
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        setUpWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webview.settings.javaScriptEnabled = true
        webUrl?.let { binding.webview.loadUrl(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}