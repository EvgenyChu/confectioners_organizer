package ru.churkin.confectioners_organizer.extensions

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry?.viewModel(): T = viewModel(viewModelStoreOwner = checkNotNull(this, ){"NavBackStackEntry must be not null"}, key = T::class.java.name)

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): T {
    return androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = viewModelStoreOwner, key = T::class.java.name
    )
}