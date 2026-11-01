package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.configureAppCheck
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("BusAdmin UNIMAID", appName)
    }

    @Test
    fun `test configureAppCheck initialization without crash`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Ensure configureAppCheck executes smoothly without throwing IllegalStateException
        configureAppCheck(context, null)
    }
}
