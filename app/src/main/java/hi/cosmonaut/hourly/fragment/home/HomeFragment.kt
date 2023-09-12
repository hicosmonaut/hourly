/*
 * MIT License
 *
 * Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package hi.cosmonaut.hourly.fragment.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.fragment.home.listener.OnAboutClickListener
import hi.cosmonaut.hourly.fragment.home.listener.OnApplyClickListener
import hi.cosmonaut.hourly.fragment.home.listener.OnCancelAllClickListener
import hi.cosmonaut.hourly.databinding.FragmentHomeBinding
import hi.cosmonaut.hourly.fragment.home.ui.compose.Home
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModel
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModelFactory
import hi.cosmonaut.hourly.picker.end.OnEndTimeClockClickListener
import hi.cosmonaut.hourly.picker.start.OnStartTimeClockClickListener
import hi.cosmonaut.hourly.tool.back.LocalOnBackPressedCallback
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setOnBackPressedCallback {
            //ignore
        }
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.let { b ->
            val viewModel = ViewModelProvider(
                this,
                HomeViewModelFactory(
                    requireActivity().application
                )
            )[HomeViewModel::class.java]

            val context = requireContext()

            b.homeCvCompose.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    Home.NoticeCard(
                        iconPainter = painterResource(id = R.drawable.icon_alert),
                        text = stringResource(id = R.string.text_notification_info),
                        containerColor = colorResource(id = R.color.colorTertiaryContainer),
                        contentColor = colorResource(id = R.color.colorOnTertiaryContainer),
                    )
                }
            }
            
            b.homeBtnApply.setOnClickListener(OnApplyClickListener(context, lifecycleScope))
            b.homeBtnCancelAll.setOnClickListener(OnCancelAllClickListener())
            b.homeEtStartTime.setOnClickListener(
                OnStartTimeClockClickListener(
                    parentFragmentManager,
                    "startTimePicker",
                    lifecycleScope
                )
            )
            b.homeEtEndTime.setOnClickListener(
                OnEndTimeClockClickListener(
                    parentFragmentManager,
                    "endTimePicker",
                    lifecycleScope
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
                    viewModel.provideTimeFlow().collect { time ->
                        binding?.let { b ->
                            b.homeEtEndTime.setText(
                                getString(
                                    R.string.label_HH_mm,
                                    time.endHours,
                                    time.endMinutes
                                )
                            )
                            b.homeEtStartTime.setText(
                                getString(
                                    R.string.label_HH_mm,
                                    time.startHours,
                                    time.startMinutes
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

    private fun setOnBackPressedCallback(callback: LocalOnBackPressedCallback){
        activity?.let { a ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                a.onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT){
                    callback.onBackInvoked()
                }
            } else {
                a.onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
                    callback.onBackInvoked()
                }
            }
        }
    }

}