package de.mctorn.caesar

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class MessageConversation(
    private val clientSocket: Socket,
    private val block: (DataInputStream, DataOutputStream) -> Unit
) : Runnable {
    override fun run() {
        val input = DataInputStream(clientSocket.getInputStream())
        val output = DataOutputStream(clientSocket.getOutputStream())

        // Communicate with client
        while (true) {
            block(input, output)
        }

        // Clean up
        input.close()
        output.close()
        clientSocket.close()
    }
}

