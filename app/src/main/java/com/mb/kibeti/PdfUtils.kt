package com.mb.kibeti

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import com.mb.kibeti.room.Goal
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PdfUtils {

    fun generatePdf(context: Context, goal: Goal, savingsPlan: Map<String, String>) {
        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 16f

        // Draw the goal details on the PDF
        canvas.drawText("Goal: ${goal.goalName}", 80f, 50f, paint)
        canvas.drawText("Target Amount: $${goal.amountNeeded}", 80f, 80f, paint)
        canvas.drawText("Start Date: ${goal.startDate}", 80f, 110f, paint)
        canvas.drawText("End Date: ${goal.endDate}", 80f, 140f, paint)

        // Draw the savings plan on the PDF
        var yPosition = 170f
        for ((period, amount) in savingsPlan) {
            canvas.drawText("${period.capitalize()} Savings: $amount", 80f, yPosition, paint)
            yPosition += 30f
        }

        document.finishPage(page)

        val directoryPath = "${context.getExternalFilesDir(null)?.absolutePath}/PDFs"
        val file = File(directoryPath)
        if (!file.exists()) {
            file.mkdirs()
        }

        val targetPdf = "$directoryPath/${goal.goalName}_SavingsPlan.pdf"
        val outputFile = File(targetPdf)

        try {
            document.writeTo(FileOutputStream(outputFile))
            Toast.makeText(context, "PDF saved at $targetPdf", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        document.close()
    }
}
