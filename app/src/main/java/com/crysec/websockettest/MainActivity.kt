package com.crysec.websockettest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.crysec.websockettest.ui.theme.WebSocketTestTheme

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.StompConfig
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.stomp
import org.hildan.krossbow.stomp.subscribeText

import org.hildan.krossbow.websocket.WebSocketConnection
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.lang.Exception
import java.time.Duration
import java.util.concurrent.Flow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


class MainActivity : ComponentActivity() {

    //Recordar: Cambiar la version de kotlin, habilitar aaceso a red y parámetro de cleartext, imprtar subdependencias, uso de okHttpClient. No acepta conexiones a localhost!!!
    suspend fun conectaSocket(){
        Log.i("stomp","Corutina iniciada")
        try {
            val okHttpClient = OkHttpClient.Builder()
                .callTimeout(Duration.ofMinutes(1))
                .pingInterval(Duration.ofSeconds(10))
                .connectTimeout(Duration.ofSeconds(10))
                .build()

            val test = OkHttpWebSocketClient(okHttpClient)
            val connection = test.connect("ws://172.24.112.1:8080/chat")
            val config = StompConfig()
            //config.connectWithStompCommand = true
            //config.connectionTimeout = 15.seconds
            val stomp = connection.stomp(config)

//            val subscription = stomp.subscribeText("/topic/messages")

//            subscription.collect { msg ->
//                println("Received: $msg")
//            }

            stomp.sendText("/app/test","FUNCIONA YA")

            //val client = StompClient(OkHttpWebSocketClient())
            // session = client.connect("ws://127.0.0.1:8080/chat")
            //session.sendText("/topic/test","FUNCIONA YA")
        }
        catch (e:Exception){e.printStackTrace()}

        //session.sendText("/topic/test", "Basic text message")

//        mStompClient.topic("/topic/messages").subscribe { topicMessage: StompMessage ->
//            println(topicMessage.payload)
//        }


        //mStompClient.send("/app/test","prueba").subscribe()
        Log.i("stomp","mensaje enviado")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebSocketTestTheme {
                // A surface container using the 'background' color from the theme
                val materialBlue700= Color(0xFF1976D2)
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = { TopAppBar(title = {Text("TopAppBar")},backgroundColor = materialBlue700)  },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = { FloatingActionButton(onClick = {
//                        Log.i("stomp","Botón de conexión pulsado")
                        runBlocking { conectaSocket() }
                    }){
                        Text("X")
                    } },
                    drawerContent = { Text(text = "drawerContent") },
                    content = { Text("BodyContent") },
                    bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
/*

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WebSocketTestTheme {
        Greeting("Android")
    }
}*/
