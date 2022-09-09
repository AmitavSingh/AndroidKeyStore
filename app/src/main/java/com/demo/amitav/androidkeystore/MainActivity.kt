package com.demo.amitav.androidkeystore

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.amitav.androidkeystore.ui.theme.AndroidKeyStoreTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidKeyStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyView()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MyView() {

    val context = LocalContext.current
    val cryptoManager = CryptoManager()


    var messageToEncrypt by remember {
        mutableStateOf("")
    }

    var messageToDecrypt by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        TextField(
            value = messageToEncrypt,
            onValueChange = {
                messageToEncrypt = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Enter your message")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {

                val bytes = messageToEncrypt.encodeToByteArray()
                val file = File(context.filesDir, "secret.txt")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val fos = FileOutputStream(file)

                messageToDecrypt = cryptoManager.encrypt(
                    bytes = bytes,
                    outputStream = fos
                ).decodeToString()


            }) {
                Text(text = "Encrypt")
            }
            Button(onClick = {

                val file = File(context.filesDir, "secret.txt")
                messageToEncrypt = cryptoManager.decrypt(
                    inputStream = FileInputStream(file)
                ).decodeToString()

            }) {
                Text(text = "Decrypt")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = messageToDecrypt)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidKeyStoreTheme {
        MyView()
    }
}