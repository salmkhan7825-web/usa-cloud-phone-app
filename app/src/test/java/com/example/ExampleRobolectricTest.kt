package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.cloudphone.model.CloudRegions
import com.example.cloudphone.model.DefaultVirtualApps
import com.example.cloudphone.model.VirtualDeviceProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    assertEquals("US Cloud Phone", appName)
  }

  @Test
  fun `verify US cloud regions catalog`() {
    val regions = CloudRegions.allRegions
    assertTrue(regions.isNotEmpty())
    val usEast = regions.firstOrNull { it.code == "us-east-1" }
    assertNotNull(usEast)
    assertEquals("VA", usEast?.stateCode)
    assertEquals("America/New_York", usEast?.timezone)
  }

  @Test
  fun `verify virtual apps catalog`() {
    val apps = DefaultVirtualApps.initialApps
    assertTrue(apps.any { it.id == "browser" })
    assertTrue(apps.any { it.id == "terminal" })
    assertTrue(apps.any { it.id == "settings" })
  }

  @Test
  fun `verify virtual device profile defaults`() {
    val profile = VirtualDeviceProfile()
    assertEquals("en-US", profile.locale)
    assertEquals("MM/DD/YYYY", profile.dateFormat)
    assertTrue(profile.androidVersion.contains("14"))
    assertEquals(34, profile.apiLevel)
  }
}
