package com.epam.brn.service.statistic.progress

import com.epam.brn.dto.response.UserAccountDto
import com.epam.brn.dto.statistic.DayStudyStatistic
import com.epam.brn.dto.statistic.UserExercisingPeriod
import com.epam.brn.dto.statistic.UserExercisingProgressStatus
import com.epam.brn.model.StudyHistory
import com.epam.brn.repo.StudyHistoryRepository
import com.epam.brn.service.UserAccountService
import com.epam.brn.service.statistic.impl.UserDayStatisticService
import com.epam.brn.service.statistic.progress.status.ProgressStatusManager
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Nikolai Lazarev
 */

@ExtendWith(MockKExtension::class)
internal class UserDayStatisticServiceTest {

    @InjectMockKs
    private lateinit var userDayStatisticService: UserDayStatisticService

    @MockK
    private lateinit var studyHistoryRepository: StudyHistoryRepository

    @MockK
    private lateinit var userAccountService: UserAccountService

    @MockK
    private lateinit var userAccount: UserAccountDto

    @MockK
    private lateinit var studyHistory: StudyHistory

    @MockK
    private lateinit var progressManager: ProgressStatusManager<List<StudyHistory>>

    private val userAccountId = 1L
    private val month: Int = 2
    private val day: Int = 23
    private val year: Int = 2021
    private val hour: Int = 13
    private val minute: Int = 20
    private val studyHistoryDate = LocalDateTime.of(year, month, day, hour, minute)
    private val from: LocalDate = LocalDate.of(year, month, day)
    private val to: LocalDate = LocalDate.of(year, month.plus(2), day)
    private val exercisingSeconds = 3500
    private val userProgress = UserExercisingProgressStatus.GREAT

    @BeforeEach
    fun init() {
        every { userAccountService.getUserFromTheCurrentSession() } returns userAccount
        every { userAccount.id } returns userAccountId
    }

    @Test
    fun `getStatisticForPeriod should return statistic for a day`() {
        // GIVEN
        val studyHistories = listOf(studyHistory)
        every { progressManager.getStatus(UserExercisingPeriod.DAY, studyHistories) } returns UserExercisingProgressStatus.GREAT
        every { studyHistory.startTime } returns studyHistoryDate
        every { studyHistory.executionSeconds } returns exercisingSeconds
        every { studyHistoryRepository.getHistories(any(), any(), any()) } returns listOf(studyHistory)

        val expectedStatistic = DayStudyStatistic(
            date = studyHistoryDate.toLocalDate(),
            exercisingTimeSeconds = exercisingSeconds,
            progress = userProgress
        )

        // WHEN
        val statisticForPeriod = userDayStatisticService.getStatisticForPeriod(from, to)

        // THEN
        assertEquals(expectedStatistic, statisticForPeriod.first())
    }

    @Test
    fun `getStatisticForPeriod should return empty list when there are not histories for the period`() {
        // GIVEN
        every { studyHistoryRepository.getHistories(any(), any(), any()) } returns emptyList()

        // WHEN
        val statisticForPeriod = userDayStatisticService.getStatisticForPeriod(from, to)

        // THEN
        assertTrue(statisticForPeriod.isEmpty())
    }
}
