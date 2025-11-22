package com.larryyu.ui.view.gundetails.utils
fun extractCategory(assetPath: String?): String {
    if (assetPath == null) return "Unknown"
    return when {
        assetPath.contains("Pistols", ignoreCase = true) -> "Pistol 🔫"
        assetPath.contains("Rifles", ignoreCase = true) -> "Rifle 🎯"
        assetPath.contains("SMGs", ignoreCase = true) -> "SMG ⚡"
        assetPath.contains("Shotguns", ignoreCase = true) -> "Shotgun 💥"
        assetPath.contains("Snipers", ignoreCase = true) -> "Sniper 🎯"
        assetPath.contains("HvyMachineGuns", ignoreCase = true) -> "Heavy 🔥"
        assetPath.contains("Melee", ignoreCase = true) -> "Melee 🗡️"
        else -> "Unknown ❓"
    }
}
fun getRarityName(tierUuid: String): String {
    return when (tierUuid) {
        "12683d76-48d7-84a3-4e09-6985794f0445" -> "Select 🔵"
        "0cebb8be-46d7-c12a-d306-e9907bfc5a25" -> "Deluxe 💚"
        "60bca009-4182-7998-dee7-b8a2558dc369" -> "Premium 💜"
        "411e4a55-4e59-7757-41f0-86a53f101bb5" -> "Exclusive 🟡"
        "e046854e-406c-37f4-6607-19a9ba8426fc" -> "Ultra 🔴"
        else -> "Standard ⚪"
    }
}
