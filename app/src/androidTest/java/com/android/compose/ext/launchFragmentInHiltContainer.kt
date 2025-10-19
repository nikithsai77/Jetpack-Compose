package com.android.compose.ext

import android.R
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import com.android.compose.HiltTestActivity

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    bundle: Bundle? = null,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: Fragment.() -> Unit = {}
) {
    val mainActivityIntent  = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(), HiltTestActivity::class.java
        )
    )

    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }

        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )

        fragment.arguments = bundle

        (fragment as T).action()

        activity.supportFragmentManager.beginTransaction()
            .add(R.id.content, fragment,"")
            .commitNow()
    }

}
