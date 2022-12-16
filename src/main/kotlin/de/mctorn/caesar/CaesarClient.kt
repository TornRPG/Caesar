package de.mctorn.caesar

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class CaesarClient(ip: String, port: Int) : CaesarMessenger(ip, port) {
    private var clientSocket: Socket? = null

    fun sendMessage(vararg str: String) {
        clientSocket = Socket()
        clientSocket!!.connect(InetSocketAddress(ip, port), 2000)
        val output = DataOutputStream(clientSocket!!.getOutputStream())
        str.forEach(output::writeUTF)
        output.writeUTF("__CaesarMessageEnd")
        clientSocket!!.close()
        clientSocket = null
    }
}
