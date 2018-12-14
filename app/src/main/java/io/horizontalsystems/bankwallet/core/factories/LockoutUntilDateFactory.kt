package io.horizontalsystems.bankwallet.core.factories

import io.horizontalsystems.bankwallet.core.ICurrentDateProvider
import io.horizontalsystems.bankwallet.core.ILockoutUntilDateFactory
import java.util.*

class LockoutUntilDateFactory(private val currentDateProvider: ICurrentDateProvider) : ILockoutUntilDateFactory {

    override fun lockoutUntilDate(failedAttempts: Int, lockoutTimestamp: Long, uptime: Long): Date {

        var timeFrame: Long = 0

        when {
            failedAttempts == 5 -> timeFrame = 5 * 60 * 1000 - (uptime - lockoutTimestamp)
            failedAttempts == 6 -> timeFrame = 10 * 60 * 1000 - (uptime - lockoutTimestamp)
            failedAttempts == 7 -> timeFrame = 15 * 60 * 1000 - (uptime - lockoutTimestamp)
            failedAttempts >= 8 -> timeFrame = 30 * 60 * 1000 - (uptime - lockoutTimestamp)
        }

        val timestamp = (if (timeFrame < 0) 0 else timeFrame) + currentDateProvider.currentDate.time

        val date = currentDateProvider.currentDate
        date.time = timestamp

        return date
    }

}
