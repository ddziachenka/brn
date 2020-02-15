package com.epam.brn.job.csv.task.impl

import com.epam.brn.job.csv.task.UploadFromCsvStrategy
import com.epam.brn.model.Task
import com.epam.brn.service.SeriesService
import com.epam.brn.service.TaskService
import com.epam.brn.service.parsers.csv.CsvMappingIteratorParser
import com.epam.brn.service.parsers.csv.converter.impl.firstSeries.TaskCsvToTaskModelConverter
import com.epam.brn.service.parsers.csv.firstSeries.TaskCSVParserService
import java.io.InputStream
import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Component

@Component
class UploadFromCsvFirstSeriesStrategy(
    private val csvMappingIteratorParser: CsvMappingIteratorParser,
    private val taskService: TaskService,
    private val taskCsvToTaskModelConverter: TaskCsvToTaskModelConverter,
    private val seriesService: SeriesService,
    private val taskCSVParserService: TaskCSVParserService
) : UploadFromCsvStrategy {
    private val log = logger()

    override fun uploadFile(inputStream: InputStream): Map<String, String> {
        val tasks = csvMappingIteratorParser.parseCsvFile(inputStream, taskCsvToTaskModelConverter, taskCSVParserService)
        tasks.forEach { task -> setExerciseSeries(task.value.first) }
        return saveTasks(tasks)
    }

    private fun setExerciseSeries(taskFile: Task?) {
        taskFile?.exercise?.series = seriesService.findSeriesForId(1)
    }

    private fun saveTasks(tasks: Map<String, Pair<Task?, String?>>): Map<String, String> {
        val notSavingTasks = mutableMapOf<String, String>()

        tasks.forEach {
            val key = it.key
            val task = it.value.first
            try {
                if (task != null)
                    taskService.save(task)
                else
                    it.value.second?.let { errorMessage -> notSavingTasks[key] = errorMessage }
            } catch (e: Exception) {
                notSavingTasks[key] = e.localizedMessage
                log.warn("Failed to insert : $key ", e)
            }
            log.debug("Successfully inserted line: $key")
        }
        return notSavingTasks
    }
}