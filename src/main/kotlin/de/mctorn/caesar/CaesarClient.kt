package de.mctorn.caesar

import de.mctorn.caesar.annotations.BlockingCall
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class CaesarClient(ip: String, port: Int) : CaesarMessenger(ip, port) {
    val clientSocket = Socket(ip, port)

    @BlockingCall
    fun connect(block: (DataInputStream, DataOutputStream) -> Boolean) {
        // Set up input and output streams for communication
        val input = DataInputStream(clientSocket.getInputStream())
        val output = DataOutputStream(clientSocket.getOutputStream())

        // Communicate with server
        while (true) {
            if (!block(input, output))
                break
        }

        // Clean up
        input.close()
        output.close()
        clientSocket.close()
    }
}
