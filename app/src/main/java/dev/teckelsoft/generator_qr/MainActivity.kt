package dev.teckelsoft.generator_qr

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import dev.teckelsoft.generator_qr.ui.theme.Generator_QRTheme
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp
import java.util.*

var infoIntoTextField: String = ""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Generator_QRTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp),
                    color = MaterialTheme.colors.background,
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField()
                        ButtonGenerateQr()
                    }

                }
            }
        }
    }
}


@Composable
fun ButtonGenerateQr() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 80.dp),
        onClick = {
            Toast.makeText(context, "Clicked!!!!", Toast.LENGTH_SHORT).show()
            val bitmap: Bitmap = generateQRCode(infoIntoTextField)
            Toast.makeText(context, "Bimap created!!!!", Toast.LENGTH_SHORT).show()

            val dateFormatted = "${Timestamp(Date().time)}".replace("\\s".toRegex(), "/").substring(0, 10) + "-QR.png"

            val dirAndName =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + File.separator + dateFormatted
            Toast.makeText(context, dirAndName, Toast.LENGTH_SHORT).show()
            val file: File?
            file = File(dirAndName)
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            Toast.makeText(context, "Bimap wrote!!!!", Toast.LENGTH_SHORT).show()

        }
    ) {
        Text(text = "Generate QR code")
    }
}

private fun generateQRCode(text: String): Bitmap {
    val width = 150
    val height = 150
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val codeWriter = MultiFormatWriter()
    try {
        val bitMatrix =
            codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                bitmap.setPixel(x, y, color)
            }
        }
    } catch (e: WriterException) {
        Log.d(TAG, "generateQRCode: ${e.message}")
    }
    return bitmap
}


@Composable
fun OutlinedTextField() {
    var infoIntoQR by remember {
        mutableStateOf("This is  QR content")
    }
    androidx.compose.material.OutlinedTextField(
        modifier = Modifier
            .width(300.dp)
            .height(80.dp)
            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 10.dp),
        value = infoIntoQR,
        onValueChange = { infoIntoQR = it },
        label = { Text("QR Content") }
    )
    infoIntoTextField = infoIntoQR
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Generator_QRTheme {
        Surface(
            modifier = Modifier
                .wrapContentSize(Alignment.Center, true)
                .padding(10.dp),
            color = MaterialTheme.colors.background,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField()
                ButtonGenerateQr()
            }

        }
    }
}


