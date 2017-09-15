package pl.droidcon.app.ext

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy {
        val v: T = findViewById(idRes)
        v
    }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy {
        val v: T = findViewById(idRes)
        v
    }
}

fun <T : View> Fragment.bind(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy {
        val v: T = view!!.findViewById(id)
        v
    }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)