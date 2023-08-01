package hi.cosmonaut.hourly.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.activity.main.listener.OnAboutClickListener
import hi.cosmonaut.hourly.activity.main.listener.OnApplyClickListener
import hi.cosmonaut.hourly.activity.main.listener.OnCancelAllClickListener
import hi.cosmonaut.hourly.databinding.FragmentHomeBinding
import hi.cosmonaut.hourly.picker.end.OnEndTimeClockClickListener
import hi.cosmonaut.hourly.picker.start.OnStartTimeClockClickListener
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.let { b ->

            val context = requireContext()

            b.homeBtnApply.setOnClickListener(OnApplyClickListener(context))
            b.homeBtnCancelAll.setOnClickListener(OnCancelAllClickListener())
            b.homeEtStartTime.setOnClickListener(
                OnStartTimeClockClickListener(
                    context,
                    parentFragmentManager,
                    "startTimePicker",
                )
            )
            b.homeEtEndTime.setOnClickListener(
                OnEndTimeClockClickListener(
                    context,
                    parentFragmentManager,
                    "endTimePicker",
                )
            )
            b.homeTvAbout.apply {
                this.text = HtmlCompat.fromHtml(
                    this@HomeFragment.getString(
                        R.string.label_about_underline
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                this.setOnClickListener(
                    OnAboutClickListener(
                        requireContext(),
                        R.string.url_about
                    )
                )
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.timeFlow.collect { time ->
                        binding?.let { b ->
                            b.homeEtEndTime.setText(
                                getString(
                                    R.string.label_HH_mm,
                                    time["endTimeHour"],
                                    time["endTimeMinute"]
                                )
                            )
                            b.homeEtStartTime.setText(
                                getString(
                                    R.string.label_HH_mm,
                                    time["startTimeHour"],
                                    time["startTimeMinute"]
                                )
                            )
                        }
                    }
                }
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}