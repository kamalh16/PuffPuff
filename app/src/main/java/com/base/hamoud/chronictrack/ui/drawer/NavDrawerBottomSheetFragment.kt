package com.base.hamoud.chronictrack.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.base.hamoud.chronictrack.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class NavDrawerBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nav_drawer_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareNavigationView(view)
    }

    private fun prepareNavigationView(view: View) {
//        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            // Bottom Navigation Drawer menu item clicks
//            when (menuItem.itemId) {
//                R.id.dark_theme -> {
//                    this.dismiss()
//                    Toast.makeText(
//                            activity, "Dark theme menu item clicked!", Toast.LENGTH_SHORT).show()
//                }
//                R.id.about -> {
//                    this.dismiss()
//                    Toast.makeText(
//                            activity, "About menu item clicked!", Toast.LENGTH_SHORT).show()
//                }
//            }
//            true
//        }
    }
}