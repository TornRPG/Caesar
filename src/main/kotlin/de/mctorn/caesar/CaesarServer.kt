package de.mctorn.caesar

import de.mctorn.caesar.annotations.BlockingCall
import java.net.ServerSocket
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.net.SocketException

class CaesarServer(ip: String, port: Int) : CaesarMessenger(ip, port), Closeable {
    private val serverSocket = ServerSocket(port)

    @BlockingCall
    fun waitForMessage(block: (ArrayList<String>) -> Unit) {
        while (true) {
            val clientSocket = try {
                serverSocket.accept()
            } catch (e: SocketException) {
                if (e.message == "Socket closed") {
                    break
                } else {
                    throw e
                }
            }

            Thread {
                val msg = acceptMessage(clientSocket)
                block(msg)
                clientSocket.close()
            }.start()
        }
    }

    private fun acceptMessage(clientSocket: Socket): ArrayList<String> {
        val input = DataInputStream(clientSocket.getInputStream())

        val data = arrayListOf<String>()
        var cmd = input.readUTF()
        while (cmd != "__CaesarMessageEnd") {
            data.add(cmd)
            cmd = input.readUTF()
        }
        return data
    }

    fun start(block: (ArrayList<String>) -> Unit) {
        Thread {
            waitForMessage(block)
        }.start()
    }

    override fun close() {
        serverSocket.close()
    }
}