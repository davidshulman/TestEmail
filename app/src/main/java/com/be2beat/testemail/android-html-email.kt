import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class EmailSender {
    companion object {
        /**
         * Sends an HTML email using the device's native email client
         */
        fun sendHtmlEmail(
            context: Context,
            to: Array<String>,
            subject: String
        ) {
            try {
                // Create HTML content
                val htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            table {
                                font-family: arial, sans-serif;
                                border-collapse: collapse;
                                width: 100%;
                            }
                            td, th {
                                border: 1px solid #dddddd;
                                text-align: left;
                                padding: 8px;
                            }
                            tr:nth-child(even) {
                                background-color: #dddddd;
                            }
                        </style>
                    </head>
                    <body>
                        <table>
                            <tr>
                                <th>Name</th>
                                <th>Count</th>
                                <th>Dimensions</th>
                                <th>Price</th>
                                <th>Total</th>
                            </tr>
                            <tr>
                                <td>grass_rounded_grass02</td>
                                <td>1</td>
                                <td>(0.7853982 m²)</td>
                                <td>0 USD</td>
                                <td>0 USD</td>
                            </tr>
                            <tr>
                                <td>grass_square_grass02</td>
                                <td>1</td>
                                <td>(1 m²)</td>
                                <td>0 USD</td>
                                <td>0</td>
                            </tr>
                        </table>
                    </body>
                    </html>
                """.trimIndent()

                // Create a temporary file in the cache directory
                val tempFile = File(context.cacheDir, "email_content.html")
                tempFile.writeText(htmlContent)

                // Create content URI using FileProvider
                val contentUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempFile
                )

                // Create email intent
                val intent = Intent(Intent.ACTION_SEND).apply {
                    // Set type to HTML
                    type = "text/html"

                    // Add email recipients and subject
                    putExtra(Intent.EXTRA_EMAIL, to)
                    putExtra(Intent.EXTRA_SUBJECT, subject)

                    // Add the HTML file as attachment
                    putExtra(Intent.EXTRA_STREAM, contentUri)

                    // Grant read permission to receiving apps
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Start email client activity
                context.startActivity(Intent.createChooser(intent, "Send email using..."))
            } catch (e: Exception) {
                Log.e("EmailSender", "Error sending email", e)
                Toast.makeText(context, "Error sending email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}