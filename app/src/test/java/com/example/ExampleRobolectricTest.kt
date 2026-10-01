package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DentalDataProvider
import com.example.data.ToothLayerId
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
    assertEquals("دليل تشريح وعلاج الأسنان", appName)
  }

  @Test
  fun `verify dental data provider consistency`() {
    assertTrue(DentalDataProvider.toothParts.isNotEmpty())
    assertNotNull(DentalDataProvider.toothParts.find { it.id == ToothLayerId.ENAMEL })
    assertNotNull(DentalDataProvider.toothParts.find { it.id == ToothLayerId.PULP })

    // Verify conditions
    val caries = DentalDataProvider.dentalConditions.find { it.id == "caries" }
    assertNotNull(caries)
    assertTrue(caries!!.stages.isNotEmpty())

    val abscess = DentalDataProvider.dentalConditions.find { it.id == "abscess" }
    assertNotNull(abscess)
    assertTrue(abscess!!.whatToAvoidUrgent.isNotEmpty())

    // Verify real clinical tools
    assertTrue(DentalDataProvider.dentalTools.isNotEmpty())
    assertNotNull(DentalDataProvider.dentalTools.find { it.id == "mouth_mirror" })
    assertNotNull(DentalDataProvider.dentalTools.find { it.id == "high_speed_handpiece" })

    // Verify 32 teeth
    assertEquals(32, DentalDataProvider.dentalArchTeeth.size)

    // Verify treatment simulator cases
    assertTrue(DentalDataProvider.treatmentCases.isNotEmpty())
  }
}

