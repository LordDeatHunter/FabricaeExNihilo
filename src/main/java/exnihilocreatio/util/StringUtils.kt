package exnihilocreatio.util

import java.text.DecimalFormat

object StringUtils {
    val PERCENT_FORMAT = DecimalFormat("#.##%")
    /**
     * Formats a percent, displays no more than 2 decimal digits, doesn't show trailing zeros
     */
    @JvmStatic
    fun formatPercent(num: Double) = PERCENT_FORMAT.format(num)!!
}