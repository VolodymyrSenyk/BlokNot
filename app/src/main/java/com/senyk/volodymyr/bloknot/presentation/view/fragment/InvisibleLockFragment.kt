package com.senyk.volodymyr.bloknot.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.databinding.FragmentInvisibleLockBinding
import com.senyk.volodymyr.bloknot.presentation.view.dialog.NoCryptoModePasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.base.BaseCryptoNotepadCryptoFragment
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.InvisibleLockViewModel

class InvisibleLockFragment : BaseCryptoNotepadCryptoFragment(),
    NoCryptoModePasswordDialogFragment.OnButtonClickListener {

    override val layoutRes = R.layout.fragment_invisible_lock
    override val toolbarRes = R.layout.toolbar_no_toolbar
    override val viewModel by viewModels<InvisibleLockViewModel>(factoryProducer = { viewModelFactory })

    private lateinit var binding: FragmentInvisibleLockBinding

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.onStart()
    }

    override fun onConfirmEmptyCryptoModePasswordClickListener() {
        viewModel.onConfirmEmptyCryptoModePasswordClick()
    }
}
