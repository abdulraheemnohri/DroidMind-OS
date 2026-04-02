package com.droidmind.services.vpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.droidmind.modules.adshield.AdShield
import com.droidmind.database.AdLogDao
import com.droidmind.database.AdLog
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * DroidMind OS VPN Service.
 *
 * Functional Overview:
 * This service operates as a DNS-filtering local proxy. It intercepts UDP packets on port 53 (DNS),
 * parses the domain name using [extractDomain], and checks it against the [AdShield] module.
 *
 * In this blueprint version, packets are looped back to the TUN interface to allow regular
 * OS networking to continue while blocking specifically identified DNS domains by dropping
 * their request packets.
 *
 * Note: For production-grade full traffic redirection, a native userspace TCP/IP stack (like lwIP)
 * must be integrated to forward packets between the TUN interface and real network sockets.
 */
@AndroidEntryPoint
class DroidMindVpnService : VpnService(), Runnable {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var vpnThread: Thread? = null
    private val adBlocker = AdShield()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var adLogDao: AdLogDao

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (vpnThread == null) {
            loadBlockingData()
            vpnThread = Thread(this, "DroidMindVpnThread")
            vpnThread?.start()
        }
        return START_STICKY
    }

    private fun loadBlockingData() {
        try {
            // Load the ad hosts list from the assets directory.
            assets.open("ad_hosts.txt").use { input ->
                adBlocker.loadHosts(input)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        vpnThread?.interrupt()
        vpnInterface?.close()
        super.onDestroy()
    }

    override fun run() {
        try {
            // Establish the VPN interface.
            vpnInterface = Builder()
                .setSession("DroidMindVPN")
                .addAddress("10.0.0.2", 32)
                .addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)
                .establish()

            val inputStream = FileInputStream(vpnInterface?.fileDescriptor)
            val outputStream = FileOutputStream(vpnInterface?.fileDescriptor)
            val packet = ByteBuffer.allocate(32767)

            while (!Thread.interrupted()) {
                val length = inputStream.read(packet.array())
                if (length > 0) {
                    // Check if it's a DNS packet
                    if (isDnsPacket(packet, length)) {
                        val domain = extractDomain(packet, length)
                        if (domain != null && adBlocker.isDomainBlocked(domain)) {
                            // Logic: If blocked, we "consume" the packet here and do NOT write it back.
                            // The requesting app will experience a DNS timeout for this domain.
                            logBlockedAd(domain)
                            packet.clear()
                            continue
                        }
                    }

                    // Blueprint: Write the packet back to allow OS to process non-blocked traffic.
                    // This creates a loopback for demonstration.
                    outputStream.write(packet.array(), 0, length)
                    packet.clear()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            vpnInterface?.close()
        }
    }

    private fun isDnsPacket(packet: ByteBuffer, length: Int): Boolean {
        if (length < 28) return false
        val protocol = packet.get(9).toInt() and 0xFF
        if (protocol != 17) return false // UDP
        val destPort = ((packet.get(22).toInt() and 0xFF) shl 8) or (packet.get(23).toInt() and 0xFF)
        return destPort == 53
    }

    private fun extractDomain(packet: ByteBuffer, length: Int): String? {
        try {
            // Simple DNS QNAME parser
            var pos = 28 + 12 // Skip IP/UDP headers (28 bytes) and DNS fixed header (12 bytes)
            val domain = StringBuilder()

            while (pos < length) {
                val labelLength = packet.get(pos).toInt() and 0xFF
                if (labelLength == 0) break
                pos++
                for (i in 0 until labelLength) {
                    domain.append(packet.get(pos).toInt().toChar())
                    pos++
                }
                domain.append(".")
            }
            return if (domain.isNotEmpty()) domain.toString().removeSuffix(".") else null
        } catch (e: Exception) {
            return null
        }
    }

    private fun logBlockedAd(domain: String) {
        serviceScope.launch {
            adLogDao.insertLog(AdLog(domain = domain, appName = "AdShield"))
        }
    }
}
