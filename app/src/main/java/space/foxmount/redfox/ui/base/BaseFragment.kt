package space.foxmount.redfox.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class BaseFragment : Fragment() {
    inline infix fun <T> LiveData<T>.observe(crossinline action: (T) -> Unit) =
        observe(viewLifecycleOwner, Observer { it?.let(action) })

}