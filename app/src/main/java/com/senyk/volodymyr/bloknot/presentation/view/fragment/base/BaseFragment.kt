package com.senyk.volodymyr.bloknot.presentation.view.fragment.base

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.AppBarLayoutStub
import com.senyk.volodymyr.bloknot.presentation.view.activity.base.BaseActivity
import com.senyk.volodymyr.bloknot.presentation.view.util.extensions.hideKeyboard
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.factory.ViewModelFactory
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.InfoWithAction
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @get:LayoutRes
    protected abstract val layoutRes: Int

    @get:LayoutRes
    protected open val toolbarRes: Int? = null

    @get:MenuRes
    protected open val menuRes: Int? = null

    protected abstract val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBaseObservers(viewModel)
    }

    protected open fun setBaseObservers(viewModel: BaseViewModel) {
        viewModel.toastMessage.observe(viewLifecycleOwner, { showToast(it) })
        viewModel.snackbarMessage.observe(viewLifecycleOwner, { showSnackbar(it) })
        viewModel.snackbarMessageWithAction.observe(viewLifecycleOwner, {
            showSnackbarWithAction(it)
        })
        viewModel.dialogFragment.observe(viewLifecycleOwner, { showDialogFragment(it) })
        viewModel.navigationEvent.observe(viewLifecycleOwner, {
            handleNavigationEvent(it.handleEvent())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuRes?.let { inflater.inflate(it, menu) }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        invalidateToolbar()
    }

    protected open fun handleNavigationEvent(event: NavigationEvent?) {
        when (event) {
            is StartActivityEvent -> {
                startActivity(event.intent)
                if (event.finishCurrentActivity) requireActivity().finish()
            }

            is StartActivityForResultEvent -> {
                startActivityForResult(event.intent, event.requestCode)
            }

            is RequestPermissionsEvent -> {
                requestPermissions(event.permissions.toTypedArray(), event.requestCode)
            }

            is StartServiceEvent -> {
                requireActivity().startService(event.intent)
            }

            is NavigateToFragmentEvent -> {
                findNavController().navigate(event.action)
            }

            is NavigateBackEvent -> {
                findNavController().popBackStack()
            }

            is NavigateBackToFragmentEvent -> {
                findNavController().popBackStack(event.destination, false)
            }
        }
    }

    open fun getToolbar(): Toolbar? = requireActivity().findViewById(R.id.toolbar)

    open fun <T : View> getToolbarViewById(viewId: Int): T? {
        val toolbar = getToolbar() ?: return null
        return toolbar.findViewById(viewId)
    }

    private fun invalidateToolbar() {
        toolbarRes?.let { toolbarRes ->
            val activity = requireActivity() as? BaseActivity ?: return
            val appBarLayoutStubLayout =
                activity.findViewById<AppBarLayoutStub>(R.id.toolbarViewStub)
            if (appBarLayoutStubLayout.layoutResource != toolbarRes) {
                appBarLayoutStubLayout.layoutResource = toolbarRes
                appBarLayoutStubLayout.inflate()
                val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
                activity.setSupportActionBar(toolbar)
            }
        }
        setHasOptionsMenu(menuRes != null && menuRes!! > 0)
    }

    protected open fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected open fun showSnackbar(message: String) {
        view?.let { view ->
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar.show()
        }
    }

    protected open fun showSnackbarWithAction(data: InfoWithAction) {
        view?.let { view ->
            val snackbar = Snackbar.make(view, data.text, Snackbar.LENGTH_LONG)
                .setAction(data.actionName, data.action)
            snackbar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar.show()
        }
    }

    protected open fun showDialogFragment(dialogFragment: DialogFragment) {
        dialogFragment.setTargetFragment(this, RC_DIALOG_FRAGMENT)
        dialogFragment.show(parentFragmentManager, dialogFragment.tag)
    }

    companion object {
        private const val RC_DIALOG_FRAGMENT = 1
    }
}
