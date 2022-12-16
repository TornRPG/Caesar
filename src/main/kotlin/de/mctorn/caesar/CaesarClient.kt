package de.mctorn.caesar

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class CaesarClient(ip: String, port: Int) : CaesarMessenger(ip, port) {
    private var clientSocket: Socket? = null

    fun sendMessage(vararg str: String): String? {
        clientSocket = Socket()
        clientSocket!!.connect(InetSocketAddress(ip, port), 2000)
        val output = DataOutputStream(clientSocket!!.getOutputStream())
        str.forEach(output::writeUTF)
        output.writeUTF("__CaesarMessageEnd")
        val ret = try {
            DataInputStream(clientSocket!!.getInputStream()).readUTF()
        } catch (_: IOException) {
            null
        }
        clientSocket!!.close()
        clientSocket = null
        return ret
    }
}
