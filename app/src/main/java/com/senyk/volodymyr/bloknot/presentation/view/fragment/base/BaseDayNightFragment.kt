package com.senyk.volodymyr.bloknot.presentation.view.fragment.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.DayNightModeViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.AttrValuesProvisionUtil

abstract class BaseDayNightFragment : BaseFragment() {

    protected val dayNightModeViewModel by activityViewModels<DayNightModeViewModel>(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val changeThemeMenuItem = menu.findItem(R.id.menuActionChangeDayNightTheme) ?: return
        dayNightModeViewModel.nightModeEnabled.observe(viewLifecycleOwner, Observer { inNightMode ->
            val iconAttrRes = if (inNightMode) R.attr.iconDay else R.attr.iconNight
            val currentAppTheme = requireActivity().theme
            val icon = AttrValuesProvisionUtil.getThemeDrawable(currentAppTheme, iconAttrRes)
            changeThemeMenuItem.setIcon(icon)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {

            R.id.menuActionChangeDayNightTheme -> {
                dayNightModeViewModel.onSwitchModeClick(); true
            }

            else -> super.onOptionsItemSelected(item)
        }
}
