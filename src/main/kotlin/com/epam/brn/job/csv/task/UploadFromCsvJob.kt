package com.epam.brn.job.csv.task

import com.epam.brn.exception.FileFormatException
import org.springframework.web.multipart.MultipartFile
import java.io.File

interface UploadFromCsvJob {

    @Throws(FileFormatException::class)
    fun uploadTasks(file: MultipartFile)

    @Throws(FileFormatException::class)
    fun uploadTasks(file: File)
}