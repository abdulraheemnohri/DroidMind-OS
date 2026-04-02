package com.droidmind.modules.adblockerx

import java.io.InputStream
import java.util.HashSet

/**
 * AdBlockerX logic for DNS-level domain filtering.
 */
class AdBlockerX {
    private val blockedDomains = HashSet<String>()

    fun loadHosts(inputStream: InputStream) {
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                    val parts = trimmed.split(Regex("\\s+"))
                    if (parts.size >= 2) {
                        blockedDomains.add(parts[1])
                    }
                }
            }
        }
    }

    fun isDomainBlocked(domain: String): Boolean {
        // Direct match
        if (blockedDomains.contains(domain)) return true

        // Subdomain matching
        var current = domain
        while (current.contains(".")) {
            if (blockedDomains.contains(current)) return true
            current = current.substringAfter(".")
        }
        return false
    }

    fun getBlockedDomainsCount(): Int = blockedDomains.size
}
