package de.mctorn.caesar

import java.net.ServerSocket
import kotlinx.coroutines.*
import java.io.DataInputStream
import java.io.DataOutputStream

class CaesarServer(ip: String, port: Int) : CaesarMessenger(ip, port) {
    private val serverSocket = ServerSocket(port)

    fun startBlocking(block: (DataInputStream, DataOutputStream) -> Unit) {
        while (true) {
            val clientSocket = serverSocket.accept()

            val clientHandler = MessageConversation(clientSocket, block)
            Thread(clientHandler).start()
        }
    }

    fun start(block: (DataInputStream, DataOutputStream) -> Unit) {
        runBlocking {
            launch {
                startBlocking(block)
            }
        }
    }
}