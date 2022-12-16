package de.mctorn.caesar

import de.mctorn.caesar.annotations.BlockingCall
import java.net.ServerSocket
import java.io.Closeable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketException

class CaesarServer(ip: String, port: Int) : CaesarMessenger(ip, port), Closeable {
    private val serverSocket = ServerSocket(port)

    @BlockingCall
    fun waitForMessage(block: (ArrayList<String>) -> String?) {
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
                val ret = block(msg)
                if (ret != null)
                    DataOutputStream(clientSocket.getOutputStream()).writeUTF(ret)
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

    fun start(block: (ArrayList<String>) -> String?) {
        Thread {
            waitForMessage(block)
        }.start()
    }

    override fun close() {
        serverSocket.close()
    }
}