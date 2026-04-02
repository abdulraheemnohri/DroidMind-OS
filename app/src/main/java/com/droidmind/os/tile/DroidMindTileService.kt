package com.droidmind.os.tile

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class DroidMindTileService : TileService() {
    override fun onStartListening() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        if (qsTile.state == Tile.STATE_ACTIVE) {
            qsTile.state = Tile.STATE_INACTIVE
        } else {
            qsTile.state = Tile.STATE_ACTIVE
        }
        qsTile.updateTile()
    }
}
